package m7.only.carrental.controller.admin;

import m7.only.carrental.entity.Car;
import m7.only.carrental.entity.car.CarDTO;
import m7.only.carrental.service.CarService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

/**
 * Контроллер для эндпоинтов, связанных с {@linkplain  Car Car} в админ панели.
 */
@Controller
@RequestMapping("/admin/cars")
@EnableMethodSecurity
public class AdminCarsController {

    private final CarService carService;

    public AdminCarsController(CarService carService) {
        this.carService = carService;
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
     * Отображение списка всех автомобилей с пагинацией.
     * Доступ к методу имеют только пользователи, имеющие доступ
     * к админ панели {@linkplain m7.only.carrental.config.WebSecurityConfig см. WebSecurityConfig}
     *
     * @param page   текущая страница
     * @param atPage количество {@link Car} на странице
     * @param model  {@code Model} модель для передачи данных в представление
     * @return {@code String} шаблон страницы списка автомобилей в админ панели
     */
    @GetMapping
    public String adminPanelCarsView(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                     @RequestParam(name = "atPage", required = false, defaultValue = "25") Integer atPage,
                                     Model model) {
        model.addAttribute("carsList", carService.findAllCarsPage(page, atPage));
        return "admin/adminPanelCars";
    }

    /**
     * Отображение формы добавления автомобиля.
     * Доступ к методу имеют только пользователи, имеющие доступ
     * к админ панели {@linkplain m7.only.carrental.config.WebSecurityConfig см. WebSecurityConfig}
     *
     * @return {@code String} шаблон страницы добавления автомобиля в админ панели
     */
    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String adminPanelAddCarView() {
        return "admin/adminPanelCarAdd";
    }

    /**
     * Добавление автомабиля в базу. Проверка на наличие автомобиля с таким же госномером в базе,
     * отказ в добавлении если такой уже существует.
     * Доступ к методу имеют только пользователи с ролью {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_MANAGER MANAGER}
     *
     * @param carDTO             {@link CarDTO} автомобиля
     * @param redirectAttributes модель для передачи данных через redirect
     * @return {@code String} шаблон страницы апдейта  в админ панели с добавленым авто, если успех,
     * и возврат к форме добавления в админ панели, если отказ
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String adminPanelAddCar(@ModelAttribute CarDTO carDTO,
                                   RedirectAttributes redirectAttributes) {
        Car car = carService.addCar(carDTO);
        if (car == null) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Автомобиль с таким госномером уже существует");
            return "redirect:admin/cars/add";
        }
        return "redirect:admin/cars/update/" + car.getId();
    }

    /**
     * Отображение формы обновления данных об автомобиле.<br/>
     * Доступ к методу имеют только пользователи с ролями {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_MANAGER MANAGER}
     *
     * @param carId идентификатор автомобиля
     * @param model {@code Model} модель для передачи данных в представление
     * @return {@code String} шаблон страницы обновления автомобиля в админ панели
     */
    @GetMapping("/update/{carId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String adminPanelUpdateCarView(@PathVariable Long carId, Model model) {
        model.addAttribute(
                "car",
                carService.findCarById(carId));
        return "admin/adminPanelCarUpdate";
    }

    /**
     * Обновление данных об автомобиле. Возврат на страницу обновления автомобиля
     * с флагом успех в случае успеха, либо с флагом ошибки, в случае отказа при обновлении.<br/>
     * Доступ к методу имеют только пользователи с ролями {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_MANAGER MANAGER}
     *
     * @param carDTO             {@link CarDTO} DTO автомобиля
     * @param carId              идентификатор автомобиля
     * @param redirectAttributes модель для передачи данных через redirect
     * @return {@code String} шаблон страницы обновления автомобиля в админ панели
     */
    @PostMapping("/update/{carId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String adminPanelUpdateCar(@ModelAttribute CarDTO carDTO,
                                      @PathVariable Long carId,
                                      RedirectAttributes redirectAttributes) {
        if (carService.updateCarById(carId, carDTO) == null) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Произошла ошибка при обновлении");
            return "redirect:admin/cars/update/" + carId;
        }
        redirectAttributes.addFlashAttribute(
                "message",
                "Автомобиль обновлен");
        return "redirect:admin/cars/update/" + carId;
    }

    /**
     * Удаление автомобиля в базе. Удаление происходит путем установки флага {@link Car#setActive(Boolean)} в false.<br/>
     * Доступ к методу имеют только пользователи с ролями {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_MANAGER MANAGER}
     *
     * @param carId              идентификатор автомобиля
     * @param redirectAttributes модель для передачи данных через redirect
     * @return {@code String} шаблон списка автомобилей в админ панели
     */
    @GetMapping("/remove/{carId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String adminPanelCarRemove(@PathVariable Long carId,
                                      RedirectAttributes redirectAttributes) {
        if (carService.setCarActive(carId, false) == null) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Удалить не удалось");
        }
        redirectAttributes.addFlashAttribute(
                "message",
                "Автомобиль ID=" + carId + " помечен как удаленный");
        return "redirect:admin/cars";
    }

    /**
     * Восстановление удаленного автомобиля. Восстановление происходит
     * путем установки флага {@link Car#setActive(Boolean)} в true.<br/>
     * Доступ к методу имеют только пользователи с ролями {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_MANAGER MANAGER}
     *
     * @param carId              идентификатор автомобиля
     * @param redirectAttributes модель для передачи данных через redirect
     * @return {@code String} шаблон списка автомобилей в админ панели
     */
    @GetMapping("/reduce/{carId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String adminPanelCarReduce(@PathVariable Long carId,
                                      RedirectAttributes redirectAttributes) {
        if (carService.setCarActive(carId, true) == null) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Восстановить не удалось");
        }
        redirectAttributes.addFlashAttribute(
                "message",
                "Автомобиль ID=" + carId + " восстановлен");
        return "redirect:admin/cars";
    }

    /**
     * Удаление фотографий автомобиля.<br/>
     * Доступ к методу имеют только пользователи с ролями {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_MANAGER MANAGER}
     *
     * @param carId              идентификатор автомобиля
     * @param deletePhotoId      массив идентификаторов фотографий
     * @param redirectAttributes модель для передачи данных через redirect
     * @return {@code String} шаблон страницы обновления автомобиля в админ панели
     */
    @PostMapping("/update/photo/delete/{carId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String adminPanelCarPhotoDelete(@PathVariable Long carId,
                                           @RequestParam Long[] deletePhotoId,
                                           RedirectAttributes redirectAttributes) {
        carService.carPhotoDelete(deletePhotoId);
        redirectAttributes.addFlashAttribute(
                "message",
                "Фото ID=" + Arrays.toString(deletePhotoId) + " уделены");

        return "redirect:admin/cars/update/" + carId;
    }
}
