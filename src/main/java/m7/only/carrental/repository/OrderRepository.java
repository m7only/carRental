package m7.only.carrental.repository;

import m7.only.carrental.entity.Order;
import m7.only.carrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
}
