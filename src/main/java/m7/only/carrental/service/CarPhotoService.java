package m7.only.carrental.service;

import m7.only.carrental.entity.Car;
import m7.only.carrental.entity.car.CarPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public interface CarPhotoService {
    CarPhoto saveCarPhotoCover(MultipartFile file, Car car);

    Set<CarPhoto> saveCarPhotos(MultipartFile[] files, Car car);

    Optional<CarPhoto> getPhotoCoverByCar(Car car);

    CarPhoto deleteCarPhoto(CarPhoto carPhoto);

    CarPhoto deleteCarPhoto(Long photoId);

    Path getPathToCarPhoto(Long carIdPath, String fileName);

    Path saveCarPhotoToFileStorage(MultipartFile file, Long carId);
}
