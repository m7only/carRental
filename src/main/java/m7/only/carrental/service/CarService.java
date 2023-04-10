package m7.only.carrental.service;

import m7.only.carrental.entity.Car;
import m7.only.carrental.entity.car.CarBody;
import m7.only.carrental.entity.car.CarDTO;
import m7.only.carrental.entity.car.Classification;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface CarService {
    Page<Car> findAllCarsPage(Integer page, Integer atPage);

    List<Car> findAllFreeCarsByOptions(LocalDate start, LocalDate end, Classification[] classifications, CarBody[] carBodies);

    List<Car> findAllCars(Classification[] classifications, CarBody[] carBodies);

    List<Car> filterByOptions(List<Car> carList, Classification[] classifications,
                              CarBody[] carBodies);

    Car addCar(CarDTO carDTO);

    Car findCarById(Long carId);

    Car updateCarById(Long carId, CarDTO carDTO);

    Car setCarActive(Long carId, boolean b);

    void carPhotoDelete(Long[] photoId);

}
