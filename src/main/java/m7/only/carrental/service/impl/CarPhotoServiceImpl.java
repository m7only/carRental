package m7.only.carrental.service.impl;

import m7.only.carrental.entity.Car;
import m7.only.carrental.entity.car.CarPhoto;
import m7.only.carrental.repository.CarPhotoRepository;
import m7.only.carrental.service.CarPhotoService;
import m7.only.carrental.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CarPhotoServiceImpl implements CarPhotoService {
    private final FileService fileService;
    private final CarPhotoRepository carPhotoRepository;

    /**
     * Путь к хранилищу фотографий {@linkplain Car автомобилей}.
     */
    @Value("${path.to.cars.photos.storage}")
    private String carsPhotosPath;

    public CarPhotoServiceImpl(FileService fileService, CarPhotoRepository carPhotoRepository) {
        this.fileService = fileService;
        this.carPhotoRepository = carPhotoRepository;
    }

    /**
     * Сохранение главной фотографии {@linkplain Car автомобиля}.
     *
     * @param file фотография автомобиля
     * @param car  автомобиль, к которому сохраняем фотографию
     * @return {@code CarPhoto} сохраненная фотография в случае успеха, null - если сохранение проавалилось
     */
    @Override
    public CarPhoto saveCarPhotoCover(MultipartFile file, Car car) {
        if (!file.isEmpty()) {
            carPhotoRepository.findByCarAndCover(car, true).ifPresent(this::deleteCarPhoto);
            Path path = saveCarPhotoToFileStorage(file, car.getId());
            if (path == null) {
                return null;
            }
            CarPhoto carPhoto = new CarPhoto(
                    car,
                    path.getFileName().toString(),
                    true
            );
            carPhotoRepository.save(carPhoto);
            return carPhoto;
        }
        return null;
    }

    /**
     * Сохранение фотографий {@linkplain Car автомобиля}.
     *
     * @param files массив с фотографиями
     * @param car   автомобиль, к которому сохраняем фотографии
     * @return {@code Set<CarPhoto>} список фотографий в случае успеха, null - если сохранение провалилось
     */
    @Override
    public Set<CarPhoto> saveCarPhotos(MultipartFile[] files, Car car) {
        Set<CarPhoto> carPhotos = new HashSet<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Path path = saveCarPhotoToFileStorage(file, car.getId());
                if (path == null) {
                    return null;
                }
                CarPhoto carPhoto = new CarPhoto(
                        car,
                        path.getFileName().toString(),
                        false
                );
                carPhotoRepository.save(carPhoto);
                carPhotos.add(carPhoto);
            }
        }
        return carPhotos;
    }

    /**
     * Поиск главной фотографии {@linkplain Car автомобиля}.
     *
     * @param car автомобиль
     * @return {@code Optional<CarPhoto>} фотография
     */
    @Override
    public Optional<CarPhoto> getPhotoCoverByCar(Car car) {
        return carPhotoRepository.findByCarAndCover(car, true);
    }

    /**
     * Удаление фотографии {@linkplain Car автомобиля} по фотографии.
     *
     * @param carPhoto удаляемая фотография
     * @return {@code CarPhoto} удаленная фотография
     */
    @Override
    public CarPhoto deleteCarPhoto(CarPhoto carPhoto) {
        if (carPhotoRepository.findById(carPhoto.getId()).isPresent()) {
            fileService.delete(
                    Path.of(
                            carsPhotosPath,
                            carPhoto.getCar().getId().toString(),
                            carPhoto.getFileName())
            );
            fileService.delete(
                    Path.of(
                            carsPhotosPath,
                            carPhoto.getCar().getId().toString(),
                            "p_" + carPhoto.getFileName())
            );
            carPhotoRepository.delete(carPhoto);
        }
        return carPhoto;
    }

    /**
     * Удаление фотографии {@linkplain Car автомобиля} по идентификатору.
     *
     * @param photoId идентификатор удаляемой фотографии
     * @return {@code CarPhoto} удаленная фотография
     */
    @Override
    public CarPhoto deleteCarPhoto(Long photoId) {
        Optional<CarPhoto> carPhotoOptional = carPhotoRepository.findById(photoId);
        if (carPhotoOptional.isPresent()) {
            fileService.delete(
                    Path.of(
                            carsPhotosPath,
                            carPhotoOptional.get().getCar().getId().toString(),
                            carPhotoOptional.get().getFileName())
            );
            fileService.delete(
                    Path.of(
                            carsPhotosPath,
                            carPhotoOptional.get().getCar().getId().toString(),
                            "p_" + carPhotoOptional.get().getFileName())
            );
            carPhotoRepository.delete(carPhotoOptional.get());
            return carPhotoOptional.get();
        }
        return null;
    }

    /**
     * Сохранение фотографии в файловое хранилище с назначением файлу уникального названия.
     *
     * @param file  фотография
     * @param carId идентификатор {@linkplain Car автомобиля}, к которому сохраняется фотография
     * @return {@code Path} путь к сохранненой фотографии
     */
    @Override
    public Path saveCarPhotoToFileStorage(MultipartFile file, Long carId) {
        String fileName = UUID.randomUUID() + "." + "jpg";
        String filesPath = carsPhotosPath + "/" + carId.toString();
        Path path = Path.of(filesPath, fileName);
        if (!fileService.createAndSavePreview(file, filesPath, fileName)) {
            return null;
        }
        fileService.upload(file, path);
        return path;
    }

    /**
     * Формирование {@linkplain Path пути} для сохранения фотографии.
     *
     * @param carIdPath идентификатор {@linkplain Car автомобиля}, к которому сохраняется фотография
     * @param fileName  название файла фотографии
     * @return {@code Path} путь для сохранения фотографии
     */
    @Override
    public Path getPathToCarPhoto(Long carIdPath, String fileName) {
        return Path.of(carsPhotosPath, carIdPath.toString(), fileName);
    }
}
