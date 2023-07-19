package m7.only.carrental.controller.front;

import m7.only.carrental.entity.Order;
import m7.only.carrental.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * Контроллер для работы с {@linkplain Order заказами} со стороны клиента
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Создание заказа пользователем. Возвращаем в аккаут к якорю созданного заказа, если успех.<br/>
     * Доступ к методу имеют только пользователи
     * с ролью {@linkplain  m7.only.carrental.entity.user.Role#ROLE_APPROVED_CLIENT ROLE_APPROVED_CLIENT}
     *
     * @param carId              идентификатор автомобиля
     * @param start              дата начала аренды
     * @param end                дата окончания аренды
     * @param redirectAttributes модель для передачи данных в представление
     * @return {@code String} шаблон аккаунта пользователя
     */
    @PostMapping
    @PreAuthorize("hasRole('APPROVED_CLIENT')")
    public String orderAdd(@RequestParam Long carId,
                           @RequestParam(name = "startDate", required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                           @RequestParam(name = "endDate", required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                           RedirectAttributes redirectAttributes) {
        Order order = orderService.addOrder(carId, start, end);
        if (order != null) {
            redirectAttributes.addFlashAttribute("orderCreated", true);
            return "redirect:account#order_" + order.getId();
        }
        redirectAttributes.addFlashAttribute("orderCreated", false);
        return "redirect:account";
    }

    /**
     * Отображение запроса об уверенности пользователя о необходимости удалить заказ.<br/>
     * Доступ к методу имеют только пользователи
     * с ролью {@linkplain  m7.only.carrental.entity.user.Role#ROLE_APPROVED_CLIENT ROLE_APPROVED_CLIENT}
     *
     * @param orderId идентификатор заказа
     * @param model   модель для передачи данных в представление
     * @return {@code String} шаблон запроса удаления заказа
     */
    @GetMapping("/cancel/{orderId}")
    @PreAuthorize("hasAnyRole('APPROVED_CLIENT', 'MANAGER', 'ADMIN')")
    public String orderCancelView(@PathVariable Long orderId, Model model) {
        model.addAttribute("order", orderService.getOrderById(orderId).orElse(new Order()));
        return "front/orderCancel";
    }

    /**
     * Отмена заказа пользователем.<br/>
     * Доступ к методу имеют только пользователи
     * с ролью {@linkplain  m7.only.carrental.entity.user.Role#ROLE_APPROVED_CLIENT ROLE_APPROVED_CLIENT}
     *
     * @param orderId            идентификатор заказа
     * @param redirectAttributes модель для передачи данных в представление
     * @return {@code String} шаблон аккаунта пользователя с якорем на отмененный заказ
     */
    @PostMapping("/cancel/{orderId}")
    @PreAuthorize("hasAnyRole('APPROVED_CLIENT', 'MANAGER', 'ADMIN')")
    public String orderCancel(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        if (orderService.cancelOrder(orderId) != null) {
            redirectAttributes.addFlashAttribute("message", "Заказ №" + orderId + " отменен успешно.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Заказ №" + orderId + " отменить не удалось");
        }
        return "redirect:account#order_" + orderId;
    }
}
