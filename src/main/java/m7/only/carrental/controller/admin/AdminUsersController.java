package m7.only.carrental.controller.admin;

import m7.only.carrental.entity.user.PersonalityData;
import m7.only.carrental.entity.user.PersonalityDataDTO;
import m7.only.carrental.entity.user.Role;
import m7.only.carrental.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@EnableMethodSecurity
public class AdminUsersController {

    private final UserService userService;

    public AdminUsersController(UserService userService) {
        this.userService = userService;
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
     * Отомбражение списка всех пользователей с пагинацией.<br/>
     * Доступ к методу имеют только пользователи, имеющие доступ
     * к админ панели {@linkplain m7.only.carrental.config.WebSecurityConfig см. WebSecurityConfig}
     *
     * @param page   текущая страница
     * @param atPage количество пользователей на странице
     * @param model  {@code Model} модель для передачи данных в представление
     * @return {@code String} шаблон списка пользователей в админ панели
     */
    @GetMapping
    public String adminPanelUsersView(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "atPage", required = false, defaultValue = "25") Integer atPage,
                                      Model model) {
        model.addAttribute("usersList", userService.getAllUsers(page, atPage));
        return "/admin/adminPanelUsers";
    }

    /**
     * Форма обновления пользователя.<br/>
     * Доступ к методу имеют только пользователи с ролью {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     *
     * @param userId идентификатор пользователя
     * @param model  {@code Model} модель для передачи данных в представление
     * @return {@code String} шаблон формы обновления пользователя в админ панели
     */
    @GetMapping("/update/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String adminPanelUserUpdateView(@PathVariable Long userId, Model model) {
        model.addAttribute(
                "personalityData",
                userService.getPersonalityByUserId(userId).orElse(new PersonalityData()));
        return "/admin/adminPanelUserUpdate";
    }

    /**
     * Обновление пользователя. Добавлен аттрибут {@code message} в модель для отображения итога операции обновления.<br/>
     * Доступ к методу имеют только пользователи с ролью {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     *
     * @param userId             идентификатор пользователя
     * @param personalityDataDTO DTO {@linkplain PersonalityData}
     * @param roles              массив установленных ролей
     * @param redirectAttributes модель для передачи данных в представление
     * @return {@code String} шаблон формы обновления данных о пользователе в админ панели
     */
    @PostMapping("/update/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String adminPanelUserUpdate(@PathVariable Long userId,
                                       @ModelAttribute PersonalityDataDTO personalityDataDTO,
                                       @RequestParam Role[] roles,
                                       RedirectAttributes redirectAttributes) {
        if (userService.updatePersonalityData(userId, personalityDataDTO) == null
                || userService.updateRoles(userId, roles) == null) {
            redirectAttributes.addFlashAttribute("message", "Произошла ошибка при обновлении");
            return "redirect:/admin/users/update/" + userId;
        }
        redirectAttributes.addFlashAttribute("message", "Аккаунт обновлен");
        return "redirect:/admin/users/update/" + userId;
    }

    /**
     * Удаление пользователя. Удаление происходит путем установки
     * флага {@linkplain m7.only.carrental.entity.User#setActive(boolean)} в false.<br/>
     * Доступ к методу имеют только пользователи с ролью {@linkplain  Role#ROLE_ADMIN ADMIN}
     *
     * @param userId             идентификатор пользователя
     * @param redirectAttributes модель для передачи данных в представление
     * @return {@code String} шаблон списка всех пользователей в админ панели
     */
    @GetMapping("/remove/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String adminPanelUserRemove(@PathVariable Long userId,
                                       RedirectAttributes redirectAttributes) {
        if (userService.setUserActive(userId, false) == null) {
            redirectAttributes.addFlashAttribute("message", "Удалить не удалось");
        }
        redirectAttributes.addFlashAttribute("message", "Пользователь ID=" + userId + " помечен как удаленный");
        return "redirect:/admin/users";
    }

    /**
     * Восстановление удаленного пользователя. Восстановление происходит путем установки
     * флага {@linkplain m7.only.carrental.entity.User#setActive(boolean)} в true.<br/>
     * Доступ к методу имеют только пользователи с ролью {@linkplain  Role#ROLE_ADMIN ADMIN}
     *
     * @param userId             идентификатор пользователя
     * @param redirectAttributes модель для передачи данных в представление
     * @return {@code String} шаблон списка пользователей в админ панели
     */
    @GetMapping("/reduce/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String adminPanelUserReduce(@PathVariable Long userId,
                                       RedirectAttributes redirectAttributes) {
        if (userService.setUserActive(userId, true) == null) {
            redirectAttributes.addFlashAttribute("message", "Восстановить не удалось");
        }
        redirectAttributes.addFlashAttribute("message", "Пользователь ID=" + userId + " восстановлен");
        return "redirect:/admin/users";
    }

    /**
     * Изменение ролей у пользователя.<br/>
     * Доступ к методу имеют только пользователи с ролью {@linkplain  Role#ROLE_ADMIN ADMIN}
     *
     * @param userId             идентификатор пользователя
     * @param roles              массив устанавливаемых ролей
     * @param redirectAttributes модель для передачи данных в представление
     * @return {@code String} шаблон списка пользователей в админ панели
     */
    @PostMapping("/update/setRoles/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String adminPanelUserSetRoles(@PathVariable Long userId,
                                         @RequestParam Role[] roles,
                                         RedirectAttributes redirectAttributes) {
        if (userService.updateRoles(userId, roles) == null) {
            redirectAttributes.addFlashAttribute("message", "Произошла ошибка при обновлении");
        }
        redirectAttributes.addFlashAttribute("message", "Аккаунт ID=" + userId + " обновлен");
        return "redirect:/admin/users";
    }
}
