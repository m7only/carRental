package m7.only.carrental.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер отображения прочих страниц приложения
 */
@Controller
public class MainController {

    /**
     * Отображение главной страницы приложения.<br/>
     * Доступ имеют все пользователи приложения, в т.ч. не прошедшие аутентификацию.
     *
     * @return {@code String} шаблон главной страницы сайта
     */
    @GetMapping("/")
    public String main() {
        return "front/main";
    }

    /**
     * Отображение страницы приложения с ценами на аренду.<br/>
     * Доступ имеют все пользователи приложения, в т.ч. не прошедшие аутентификацию.
     *
     * @return {@code String} шаблон страницы сайта с ценами
     */
    @GetMapping("/prices")
    public String prices() {
        return "front/prices";
    }

    /**
     * Отображение страницы приложения с условиями заключения договора аренды.<br/>\
     * Доступ имеют все пользователи приложения, в т.ч. не прошедшие аутентификацию.
     *
     * @return {@code String} шаблон страницы сайта с условиями аренды
     */
    @GetMapping("/terms")
    public String terms() {
        return "front/terms";
    }

    /**
     * Отображение страницы приложения с информацией о предприятии.<br/>
     * Доступ имеют все пользователи приложения, в т.ч. не прошедшие аутентификацию.
     *
     * @return {@code String} шаблон страницы сайта с информацией о предприятии
     */
    @GetMapping("/about")
    public String about() {
        return "front/about";
    }

}
