package m7.only.carrental.controller.front;

import m7.only.carrental.entity.User;
import m7.only.carrental.entity.user.PersonalityData;
import m7.only.carrental.entity.user.PersonalityDataDTO;
import m7.only.carrental.service.OrderService;
import m7.only.carrental.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Контроллер управления аккаунтом пользователя во фронт части приложения
 */
@Controller
public class AccountController {
    private final UserService userService;
    private final OrderService orderService;

    public AccountController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    /**
     * Отображение страницы с личным аккаунтом пользователя. Форма для изменения данных.<br/>
     * Доступ к методу имеют только пользователи с ролью
     * {@linkplain  m7.only.carrental.entity.user.Role#ROLE_CLIENT ROLE_CLIENT}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_APPROVED_CLIENT ROLE_APPROVED_CLIENT}
     *
     * @param model {@code Model} модель для передачи данных в представление
     * @return {@code String} шаблон страницы аккаунта
     */
    @GetMapping("/account")
    public String getPersonality(Model model) {
        model.addAttribute(
                "personalityData",
                userService.getCurrentUserPersonalityData().orElse(new PersonalityData()));
        model.addAttribute("ordersList", orderService.getOrdersByUser(userService.getCurrentUser()));
        return "front/account";
    }

    /**
     * Обновления данных в аккаунте.
     * Доступ к методу имеют только пользователи с ролью
     * {@linkplain  m7.only.carrental.entity.user.Role#ROLE_CLIENT ROLE_CLIENT}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_APPROVED_CLIENT ROLE_APPROVED_CLIENT}
     *
     * @param personalityDataDTO DTO {@linkplain PersonalityDataDTO PersonalityData}
     * @param redirectAttributes модель для передачи данных в представление
     * @return {@code String} шаблон страницы аккаунта
     */
    @PostMapping("/account")
    public String updatePersonalityData(@ModelAttribute PersonalityDataDTO personalityDataDTO,
                                        RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser();
        if (user == null || userService.updatePersonalityData(user.getId(), personalityDataDTO) == null) {
            redirectAttributes.addFlashAttribute("message", "Произошла ошибка при обновлении");
            return "redirect:account";
        }
        redirectAttributes.addFlashAttribute("message", "Аккаунт обновлен");
        return "redirect:account";
    }
}
