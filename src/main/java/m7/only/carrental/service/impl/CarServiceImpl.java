package m7.only.carrental.service.impl;

import jakarta.validation.Valid;
import m7.only.carrental.entity.Car;
import m7.only.carrental.entity.car.CarBody;
import m7.only.carrental.entity.car.CarDTO;
import m7.only.carrental.entity.car.Classification;
import m7.only.carrental.repository.CarRepository;
import m7.only.carrental.service.CarPhotoService;
import m7.only.carrental.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarPhotoService carPhotoService;

    public CarServiceImpl(CarRepository carRepository, CarPhotoService carPhotoService) {
        this.carRepository = carRepository;
        this.carPhotoService = carPhotoService;
    }

    /**
     * Поиск всех {@linkplain Car автомобилей} с пагинацией.
     *
     * @param page   текущая страница
     * @param atPage количество автомобилей на странице
     * @return {@linkplain Page Страница} с автомобилями
     */
    @Override
    public Page<Car> findAllCarsPage(Integer page, Integer atPage) {
        return carRepository.findAll(
                PageRequest.of(
                        page,
                        atPage,
                        Sort.by("id").descending()
                ));
    }

    /**
     * Поиск всех автомобилей с указанными {@linkplain Classification классами} и {@linkplain CarBody типами кузово}.
     *
     * @param classifications массив классов
     * @param carBodies       массив типов кузова
     * @return List с найдеными автомобилями
     */
    @Override
    public List<Car> findAllCars(Classification[] classifications, CarBody[] carBodies) {
        return filterByOptions(
                carRepository.findAll(),
                classifications,
                carBodies);
    }

    /**
     * Поиск {@linkplain Car автомобилей}, свободных в указанные даты и подходящие по {@linkplain Classification классам}
     * и {@linkplain CarBody типам кузова}.
     *
     * @param start           дата начала аренды
     * @param end             дата окончания аренды
     * @param classifications массив классов
     * @param carBodies       массив типов кузова
     * @return List с найдеными автомобилями
     */
    @Override
    public List<Car> findAllFreeCarsByOptions(LocalDate start,
                                              LocalDate end,
                                              Classification[] classifications,
                                              CarBody[] carBodies) {
        return filterByOptions(carRepository
                        // тащим все машины из базы, с которыми не пересекаются даты других заказов, ищем по ...
                        .findAllFreeCarsByDate(
                                // ... по дате начала...
                                start.atStartOfDay(),
                                // ...и дате завершения
                                end.atStartOfDay()),
                // фильтруем по классификации...
                classifications,
                // ...и кузовам
                carBodies);
    }

    /**
     * Фильтрация входящего списка {@code List<Car> carList}: должен содержать {@linkplain Classification классы}
     * из массива {@code Classification[] classifications} и {@linkplain CarBody типы кузовов} из
     * {@code CarBody[] carBodies}.
     *
     * @param carList         список для фильтрации
     * @param classifications искомые классы автомобиля
     * @param carBodies       искомые типы кузова
     * @return List с подходящими автомобилями
     */
    @Override
    public List<Car> filterByOptions(List<Car> carList, Classification[] classifications,
                                     CarBody[] carBodies) {
        // по умолчанию берем весь список классов Classification.values()
        Classification[] finalClassifications =
                classifications == null || classifications.length == 0
                        ? Classification.values()
                        : classifications;
        // по умолчанию берем весь список кузовов CarBody.values()
        CarBody[] finalCarBody =
                carBodies == null || carBodies.length == 0
                        ? CarBody.values()
                        : carBodies;
        return carList.stream()
                // фильтруем по списку искомых классов
                // если есть хоть одно пересечение искомых классов finalCarBody
                // с классами конкретной машины .getClassifications()
                .filter(
                        car -> Arrays.stream(finalClassifications).anyMatch(
                                classification -> car
                                        .getClassifications()
                                        .contains(classification)))
                // фильтруем по списку искомых кузовов
                // если есть хоть одно перемечение со списком искомых кузовов finalCarBody
                // с кузовом конкретной машины .getCarBody()
                .filter(
                        car -> Arrays.stream(finalCarBody).anyMatch(
                                carBody -> car
                                        .getCarBody()
                                        .equals(carBody)))
                .collect(Collectors.toList());
    }

    /**
     * Добавление нового {@linkplain Car автомобиля} в базу.
     *
     * @param carDTO {@linkplain CarDTO DTO} автомобиля
     * @return созданный автомобиль
     */
    @Override
    public Car addCar(@Valid CarDTO carDTO) {
        if (carRepository.findByPlateIgnoreCase(carDTO.getPlate()).isPresent()) {
            return null;
        }
        Car car = new Car();
        car.setFromDto(carDTO);
        car.setActive(true);
        carRepository.save(car);
        car.setPhotos(carPhotoService.saveCarPhotos(carDTO.getPhotos(), car));
        car.getPhotos().add(carPhotoService.saveCarPhotoCover(carDTO.getCover(), car));
        return carRepository.save(car);
    }

    /**
     * Поиск автомобиля по идентификатору.
     *
     * @param carId идентификатор автомобиля
     * @return найденый автомобиль, пустой экземпляр если авто не найден
     */
    @Override
    public Car findCarById(Long carId) {
        return carRepository.findById(carId).orElse(new Car());
    }

    /**
     * Обновление автомобиля по его идентификатору.
     *
     * @param carId  идентификатор обновляемого автомобиля
     * @param carDTO {@linkplain CarDTO DTO} автомобиля
     * @return обновленный автомобиль
     */
    @Override
    public Car updateCarById(Long carId, @Valid CarDTO carDTO) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isEmpty()) {
            return null;
        }
        Car car = optionalCar.get();
        car.setFromDto(carDTO);
        if (!carDTO.getCover().isEmpty()) {
            carPhotoService.getPhotoCoverByCar(car).ifPresent(carPhoto -> carPhotoService.deleteCarPhoto(carPhoto));
            carPhotoService.saveCarPhotoCover(carDTO.getCover(), car);
        }
        carPhotoService.saveCarPhotos(carDTO.getPhotos(), car);
        return carRepository.save(car);
    }

    /**
     * Изменение состояния уделания автомобиля. true - автомобиль НЕ удален, flase - автомобиль удален.
     *
     * @param carId    идентификатор автомобиля
     * @param isActive устанавливаемое состояние
     * @return измененный экземпляр автомобиля
     */
    @Override
    public Car setCarActive(Long carId, boolean isActive) {
        Optional<Car> carOptional = carRepository.findById(carId);
        if (carOptional.isPresent()) {
            carOptional.get().setActive(isActive);
            return carRepository.save(carOptional.get());
        }
        return null;
    }

    /**
     * Удаление всех {@linkplain m7.only.carrental.entity.car.CarPhoto фотографий} автомобиля
     *
     * @param photosId массив фотографий автомобиля
     */
    @Override
    public void carPhotoDelete(Long[] photosId) {
        Arrays.stream(photosId).forEach(photoId -> carPhotoService.deleteCarPhoto(photoId));
    }
}
