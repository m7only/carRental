package m7.only.carrental.controller.front;

import m7.only.carrental.entity.Car;
import m7.only.carrental.entity.car.CarBody;
import m7.only.carrental.entity.car.Classification;
import m7.only.carrental.service.CarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Контроллер для отображения списка атомобилей, доступных к аренде
 */
@Controller
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Поиск и отоброжение автомобилей, доступных к аренде в указанные пользователем даты.
     * В качестве фильтра используются {@linkplain CarBody кузов} и {@linkplain Classification класс} автомобиля.<br/>
     * Доступ к методу имеют все пользователи, в т.ч. не прошедшие аутентификацию.
     *
     * @param start           дата начала аренды
     * @param end             дата окончания аренды
     * @param classifications массив класса авто, искомых пользователем
     * @param carBodies       массив типов кузова, искомых пользователем
     * @param model           {@code Model} модель для передачи данных в представление
     * @return {@code String} шаблон страницы автомобилей
     */
    @GetMapping
    public String carsView(@RequestParam(name = "startDate", required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                           @RequestParam(name = "endDate", required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                           @RequestParam(name = "classifications", required = false) Classification[] classifications,
                           @RequestParam(name = "carBodies", required = false) CarBody[] carBodies,
                           Model model) {
        List<Car> list;
        if (start == null || end == null) {
            list = carService.findAllCars(classifications, carBodies);
        } else {
            list = carService.findAllFreeCarsByOptions(start, end, classifications, carBodies);
        }
        model.addAttribute("carsList", list);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("checkedClassifications", classifications != null ? Arrays.stream(classifications).toList() : null);
        model.addAttribute("checkedCarBodies", carBodies != null ? Arrays.stream(carBodies).toList() : null);
        return "/front/cars";
    }
}
