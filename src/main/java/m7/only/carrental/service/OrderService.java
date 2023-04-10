package m7.only.carrental.service;

import m7.only.carrental.entity.Order;
import m7.only.carrental.entity.User;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    Page<Order> getAllOrders(Integer page, Integer atPage);

    Order addOrder(Long carId, LocalDate start, LocalDate end);

    List<Order> getOrdersByUser(User user);

    Optional<Order> getOrderById(Long orderId);

    Order cancelOrder(Long orderId);
}
