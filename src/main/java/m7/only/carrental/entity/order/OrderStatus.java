package m7.only.carrental.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum текущего состояния {@linkplain m7.only.carrental.entity.Order заказа}.
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    CREATED("Создан"),
    CANCELED("Отменен"),
    PAID("Оплачен"),
    STARTED("Начат"),
    EXPIRED("Просрочен"),
    ENDED("Выполнен");

    private final String status;
}
