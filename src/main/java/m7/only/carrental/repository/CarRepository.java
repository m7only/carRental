package m7.only.carrental.repository;

import m7.only.carrental.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByPlateIgnoreCase(String plate);
    //List<Car> findByClassificationsAndCarBodyIn(Set<Classification> classifications, Collection<@NotNull CarBody> carBody);

    @Query(
            value = "SELECT * FROM cars WHERE id NOT IN (SELECT car_id FROM orders WHERE (start < ?1 OR ending > ?2) AND active!=false AND status!='CANCELED')",
            nativeQuery = true)
    List<Car> findAllFreeCarsByDate(LocalDateTime start, LocalDateTime end);
}
