package m7.only.carrental.controller.admin;

import m7.only.carrental.service.OrderService;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для эндпоинтов, связанных с {@linkplain m7.only.carrental.entity.Order Order} в админ панели
 */
@Controller
@RequestMapping("/admin/orders")
@EnableMethodSecurity
public class AdminOrdersController {

    private final OrderService orderService;

    public AdminOrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Добавление флага к экзепляру модели о том, что данная модель относится к админ панели.
     * Используется для отображения админ меню в навигации.
     *
     * @param model {@code Model} модель для передачи данных в представление
     */
    @ModelAttribute
    public void addAdminPanelFlag(Model model) {
        model.addAttribute("isAdminPanel", Boolean.TRUE);
    }

    /**
     * Отомбражение списка всех заказов с пагинацией.<br/>
     * Доступ к методу имеют только пользователи, имеющие доступ
     * к админ панели {@linkplain m7.only.carrental.config.WebSecurityConfig см. WebSecurityConfig}
     *
     * @param page   текущая страница
     * @param atPage количество заказов на странице
     * @param model  {@code Model} модель для передачи данных в представление
     * @return {@code String} шаблон списка заказов в админ панели
     */
    @GetMapping
    public String adminPanelOrders(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                   @RequestParam(name = "atPage", required = false, defaultValue = "25") Integer atPage,
                                   Model model) {
        model.addAttribute("ordersList", orderService.getAllOrders(page, atPage));
        return "/admin/adminPanelOrders";
    }

}
