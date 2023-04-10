package m7.only.carrental.entity.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import m7.only.carrental.entity.Car;

/**
 * Enum типа привода {@linkplain Car автомобиля} с текстовым представлением.
 */
@Getter
@AllArgsConstructor
public enum WheelDriveType {
    ALL_WHEEL_DRIVE("Полный"),
    FRONT_WHEEL_DRIVE("Передний"),
    REAR_WHEEL_DRIVE("Задний");

    private final String wheelDrive;
}
