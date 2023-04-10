package m7.only.carrental.entity.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import m7.only.carrental.entity.Car;

/**
 * Enum класса {@linkplain Car автомобиля} с текстовым представлением.
 */
@Getter
@AllArgsConstructor
public enum Classification {
    CLASS_ECONOMY("Эконом"),
    CLASS_COMFORT("Комфорт"),
    CLASS_BUSINESS("Бизнес"),
    CLASS_SUV("Кроссовер"),
    CLASS_PREMIUM("Премиум"),
    CLASS_SPORT("Спорт");
    private final String classificationType;
}
