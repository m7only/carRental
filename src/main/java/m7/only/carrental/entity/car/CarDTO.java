package m7.only.carrental.entity.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * DTO для сущности {@linkplain m7.only.carrental.entity.Car Car}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    /**
     * Производитель автомоблия.
     */
    @NotBlank
    private String manufacturer;

    /**
     * Модель автомобиля.
     */
    @NotBlank
    private String model;

    /**
     * Год производства автомобиля.
     */
    @NotBlank
    private Integer year;

    /**
     * Цвет автомобиля.
     */
    @NotBlank
    private String color;

    /**
     * Госномер автомобиля.
     */
    @NotBlank
    private String plate;

    /**
     * Тип коробки передач автомобиля.
     */
    @NotNull
    private Gearbox gearbox;

    /**
     * Кузов автомобиля.
     */
    @NotNull
    private CarBody carBody;

    /**
     * Цена аренды автомобиля.
     */
    @NotNull
    private Integer price;

    /**
     * Тип двигателя автомобиля.
     */
    @NotNull
    private EngineType engineType;

    /**
     * Мощность автомобиля.
     */
    @NotNull
    private Integer power;

    /**
     * Привод автомобиля.
     */
    @NotNull
    private WheelDriveType wheelDriveType;

    /**
     * Класс автомобиля.
     */
    @NotNull
    private Set<Classification> classifications;

    /**
     * Главная фотография автомобиля (отображается первой).
     */
    private MultipartFile cover;

    /**
     * Массив фотографий автомобиля.
     */
    private MultipartFile[] photos;

}
