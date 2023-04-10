package m7.only.carrental.controller.admin;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для админ панели.
 */
@Controller
@RequestMapping("/admin")
@EnableMethodSecurity
public class AdminController {
    @ModelAttribute
    public void addAdminPanelFlag(Model model) {
        model.addAttribute("isAdminPanel", Boolean.TRUE);
    }

    /**
     * Отображение главной страницы админ панели.<br/>
     * Доступ к методу имеют только пользователи, имеющие доступ
     * к админ панели {@linkplain m7.only.carrental.config.WebSecurityConfig WebSecurityConfig}
     *
     * @return {@code String} шаблон главной страницы админ панели
     */
    @GetMapping
    public String adminPanelView() {
        return "/admin/adminPanel";
    }
}
