package m7.only.carrental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import m7.only.carrental.entity.car.*;

import java.util.Set;

/**
 * Сущность автомобиля.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
    /**
     * Идентификатор автомобиля.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Производитель автомобиля.
     */
    @NotBlank
    @Column(name = "manufacturer")
    private String manufacturer;

    /**
     * Модель автомобиля.
     */
    @NotBlank
    @Column(name = "model")
    private String model;

    /**
     * Год производства автомобиля.
     */
    @NotNull
    @Min(1900)
    @Column(name = "year")
    private Integer year;

    /**
     * Цвет автомобиля.
     */
    @NotBlank
    @Column(name = "color")
    private String color;

    /**
     * Госномер автомобиля.
     */
    @NotBlank
    @Column(name = "plate")
    private String plate;

    /**
     * Флаг удаления автомобиля, true - удален.
     */
    @NotNull
    @Column(name = "active")
    private Boolean active;

    /**
     * {@linkplain Gearbox Тип коробки передач} автомобиля.
     */
    @NotNull
    @Column(name = "gearbox")
    @Enumerated(EnumType.STRING)
    private Gearbox gearbox;

    /**
     * {@linkplain CarBody Кузов} автомобиля.
     */
    @NotNull
    @Column(name = "car_body")
    @Enumerated(EnumType.STRING)
    private CarBody carBody;

    /**
     * Цена аренды автомобиля.
     */
    @NotNull
    @Column(name = "price")
    private Integer price;

    /**
     * {@linkplain EngineType Тип двигателя} автомобиля.
     */
    @NotNull
    @Column(name = "engine")
    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    /**
     * Мощность автомобиля.
     */
    @NotNull
    @Column(name = "power")
    private Integer power;

    /**
     * {@linkplain WheelDriveType Привод} автомобиля.
     */
    @NotNull
    @Column(name = "wheel_drive")
    @Enumerated(EnumType.STRING)
    private WheelDriveType wheelDriveType;

    /**
     * {@linkplain Classification Классы} автомобиля.
     */
    @ElementCollection(targetClass = Classification.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "cars_classifications", joinColumns = @JoinColumn(name = "car_id"))
    @JoinTable(
            name = "cars_classifications",
            joinColumns = @JoinColumn(name = "car_id", referencedColumnName = "id")
    )
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Classification> classifications;

    /**
     * {@linkplain Order Заказы}, в которых используется автомобиль.
     */
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Order> orders;

    /**
     * {@linkplain CarPhoto Фотографии} автомобиля.
     */
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CarPhoto> photos;

    /**
     * Установка данных автомобиля из {@linkplain CarDTO DTO}.
     *
     * @param carDTO {@linkplain CarDTO DTO} автомобиля
     */
    public void setFromDto(CarDTO carDTO) {
        setManufacturer(carDTO.getManufacturer());
        setModel(carDTO.getModel());
        setYear(carDTO.getYear());
        setColor(carDTO.getColor());
        setPlate(carDTO.getPlate());
        setGearbox(carDTO.getGearbox());
        setPrice(carDTO.getPrice());
        setCarBody(carDTO.getCarBody());
        setClassifications(carDTO.getClassifications());
        setEngineType(carDTO.getEngineType());
        setPower(carDTO.getPower());
        setWheelDriveType(carDTO.getWheelDriveType());
    }
}
