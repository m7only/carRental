package m7.only.carrental.entity.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum кузовов {@linkplain m7.only.carrental.entity.Car автомобиля} с текстовым представлением.
 */
@Getter
@AllArgsConstructor
public enum CarBody {
    CAR_BODY_SEDAN("Седан"),

    CAR_BODY_HATCHBACK("Хетчбек"),

    CAR_BODY_COUPE("Купе"),

    CAR_BODY_WAGON("Универсал"),

    CAR_BODY_SUV("Внедорожник"),

    CAR_BODY_CROSSOVER("Кроссовер"),

    CAR_BODY_TRUCK("Пикап"),

    CAR_BODY_VAN("Фургон"),

    CAR_BODY_MINIVAN("Минивэн");

    private final String carBodyType;
}
