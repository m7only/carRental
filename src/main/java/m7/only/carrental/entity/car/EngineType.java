package m7.only.carrental.entity.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import m7.only.carrental.entity.Car;

/**
 * Enum типа двигателя {@linkplain Car автомобиля} с текстовым представлением.
 */
@Getter
@AllArgsConstructor
public enum EngineType {
    ENGINE_PETROL("Бензин"),
    ENGINE_DIESEL("Дизель"),
    ENGINE_ELECTRIC("Электро");

    private final String engineType;
}
