package m7.only.carrental.repository;

import m7.only.carrental.entity.Car;
import m7.only.carrental.entity.car.CarPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarPhotoRepository extends JpaRepository<CarPhoto, Long> {
    Optional<CarPhoto> findByCarAndCover(Car car, Boolean cover);
}
