package m7.only.carrental.entity.car;

import jakarta.persistence.*;
import lombok.*;
import m7.only.carrental.entity.Car;

/**
 * Сущность фотографии {@linkplain Car автомобиля}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars_photo")
public class CarPhoto {
    /**
     * Идентификатор фотографии.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название файла.
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * Автомобиль, которому принадлежат фотографии.
     */
    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Car car;

    /**
     * Флаг главной фотографии.
     */
    @Column(name = "cover")
    private Boolean cover;

    public CarPhoto(Car car, String fileName, Boolean cover) {
        this.car = car;
        this.fileName = fileName;
        this.cover = cover;
    }
}
