package m7.only.carrental.service.impl;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import m7.only.carrental.entity.Car;
import m7.only.carrental.entity.Order;
import m7.only.carrental.entity.User;
import m7.only.carrental.entity.order.OrderStatus;
import m7.only.carrental.repository.OrderRepository;
import m7.only.carrental.service.CarService;
import m7.only.carrental.service.OrderService;
import m7.only.carrental.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CarService carService;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, CarService carService, UserService userService) {
        this.orderRepository = orderRepository;
        this.carService = carService;
        this.userService = userService;
    }

    /**
     * Получение списка всех {@linkplain Order заказов} с пагинацией.
     *
     * @param page   текущая страница
     * @param atPage количество заказов на странице
     * @return {@code Page} страница с заказами
     */
    @Override
    public Page<Order> getAllOrders(@Positive Integer page, @Positive Integer atPage) {
        return orderRepository.findAll(
                PageRequest.of(
                        page,
                        atPage,
                        Sort.by("id").descending()
                ));
    }

    /**
     * Добавление {@linkplain Order заказа}.
     *
     * @param carId идентификатор автомобиля, на который оформлен заказ
     * @param start дата начала аренды
     * @param end   дата окончания аренды
     * @return {@code Order} созданный заказ
     */
    @Override
    public Order addOrder(@Positive Long carId, @Future LocalDate start, @Future LocalDate end) {
        Car car = carService.findCarById(carId);
        if (car == null || start.isAfter(end)) {
            return null;
        }
        Integer totalPrice = car.getPrice() * (int) start.until(end, ChronoUnit.DAYS);
        Order order = new Order(
                start.atTime(12, 0),
                end.atTime(12, 0),
                OrderStatus.CREATED,
                totalPrice,
                true,
                car,
                userService.getCurrentUser()
        );
        return orderRepository.save(order);
    }

    /**
     * Поиск {@linkplain Order заказов} по {@linkplain User пользователю}.
     *
     * @param user пользователь
     * @return {@code List<Order>} заказов
     */
    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findAllByUser(user);
    }

    /**
     * Поиск {@linkplain Order заказа} по идентификатору.
     *
     * @param orderId идентификатор заказа
     * @return {@code Optional<Order>} заказ
     */
    @Override
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * Отмена {@linkplain Order заказа} с проверкой права доступа к заказу.<br/>
     * Доступ к отмене заказа имею {@linkplain User пользователь}, которому принадлежит заказ или пользователи
     * с ролями {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_MANAGER MANAGER}
     *
     * @param orderId отменяемый заказ
     * @return {@code Order} отменный заказ, null - если заказ не найден или у пользователя нет прав на его изменение
     */
    @Override
    public Order cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return null;
        }
        Order order = optionalOrder.get();
        User currentUser = userService.getCurrentUser();
        if (!currentUser.equals(order.getUser()) &&
                currentUser
                        .getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ADMIN") || a.getAuthority().equals("MANAGER")
                        )) {
            return null;
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return order;
    }
}
