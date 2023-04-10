package m7.only.carrental.entity;

import jakarta.persistence.*;
import lombok.*;
import m7.only.carrental.entity.order.OrderStatus;

import java.time.LocalDateTime;

/**
 * Сущность заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    /**
     * Идентификатор заказа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Дата начала аренды.
     */
    @Column(name = "start")
    private LocalDateTime start;

    /**
     * Дата окончания аренды.
     */
    @Column(name = "ending")
    private LocalDateTime end;

    /**
     * Текущий {@linkplain OrderStatus статус} заказа.
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * Итоговая стоимость заказа.
     */
    @Column(name = "total_price")
    private Integer totalPrice;

    /**
     * Флаг отмены заказа.
     */
    @Column(name = "active")
    private Boolean active;

    /**
     * Заказанный {@linkplain Car автомобиль}.
     */
    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Car car;

    /**
     * {@linkplain User Пользователь}, оформивший заказ.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    public Order(LocalDateTime start, LocalDateTime end, OrderStatus status, Integer totalPrice, Boolean active, Car car, User user) {
        this.start = start;
        this.end = end;
        this.status = status;
        this.totalPrice = totalPrice;
        this.active = active;
        this.car = car;
        this.user = user;
    }
}
