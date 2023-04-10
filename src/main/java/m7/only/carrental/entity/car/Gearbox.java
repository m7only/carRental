package m7.only.carrental.entity.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import m7.only.carrental.entity.Car;

/**
 * Enum типа коробки передач {@linkplain Car автомобиля} с текстовым представлением.
 */
@Getter
@AllArgsConstructor
public enum Gearbox {
    AUTO("автоматическая"),
    MECHANIC("механическая");

    private final String gearboxType;
}
