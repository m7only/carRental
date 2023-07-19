package m7.only.carrental.controller.front;

import jakarta.validation.Valid;
import m7.only.carrental.entity.User;
import m7.only.carrental.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для регистрации пользователя
 */
@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отображение формы регистрации пользователя. <br/>
     * Доступ имеют все пользователи приложения, в т.ч. не прошедшие аутентификацию.
     *
     * @return {@code String} шаблон формы регистрации пользователя
     */
    @GetMapping("/registration")
    public String showRegistration() {
        return "front/registration";
    }

    /**
     * Регистрация пользователя.<br/>
     * Доступ имеют все пользователи приложения, в т.ч. не прошедшие аутентификацию.
     *
     * @param user              {@linkplain User User} данные пользователя
     * @param confirmedPassword дубль поля пароль для проверки совпадения
     * @param model             {@code Model} модель для передачи данных в представление
     * @return {@code String} шаблон аккаунта пользователя в случае успеха или форма регистрации в случае отказа
     */
    @PostMapping("/registration")
    public String registerUser(@Valid User user,
                               @RequestParam String confirmedPassword,
                               Model model) {
        return userService.register(user, confirmedPassword, model);
    }
}
