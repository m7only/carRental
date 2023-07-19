package m7.only.carrental.service.impl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import m7.only.carrental.entity.User;
import m7.only.carrental.entity.user.DocumentPhoto;
import m7.only.carrental.entity.user.PersonalityData;
import m7.only.carrental.entity.user.PersonalityDataDTO;
import m7.only.carrental.entity.user.Role;
import m7.only.carrental.repository.UserRepository;
import m7.only.carrental.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DocumentPhotoServiceImpl documentPhotoService;
    private final HttpServletRequest httpServletRequest;

    public UserServiceImpl(UserRepository userRepository,
                           DocumentPhotoServiceImpl documentPhotoService, HttpServletRequest httpServletRequest) {
        this.userRepository = userRepository;
        this.documentPhotoService = documentPhotoService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Поиск {@linkplain User пользователя} по логину.
     *
     * @param username логин пользователя
     * @return {@code User} если найден, иначе пустую сущность
     */
    @Override
    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElse(new User());
    }

    /**
     * Регистрация нового {@linkplain User пользователя}. Проверка на существование пользователя с таким же логином.
     * Проверка на совпадение паролей.
     *
     * @param user              регистрируемый пользователь
     * @param confirmedPassword поле подтверждения пароля
     * @param model             {@code Model} модель для передачи данных в представление
     * @return {@code String} строковое представление шаблона аккаунта пользователя или
     * строковое представление шаблона формы регистрации в случае провала
     */
    @Override
    public String register(@Valid User user,
                           @NotBlank String confirmedPassword,
                           Model model) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("alreadyExist", true);
            return "registration";
        }
        if (!confirmedPassword.equals(user.getUsername())) {
            model.addAttribute("passwordNotConfirmed", true);
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Set.of(Role.ROLE_CLIENT));
        user.setCreated(LocalDateTime.now());
        userRepository.save(user);
        try {
            httpServletRequest.login(user.getUsername(), user.getPassword());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return "redirect:account";
    }

    /**
     * Поиск {@linkplain PersonalityData персональных данных} пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return {@code Optional<PersonalityData>} с персональными данными
     */
    @Override
    public Optional<PersonalityData> getPersonalityByUserId(@NotBlank Long userId) {
        return Optional.ofNullable(userRepository.findById(userId).orElse(new User()).getPersonalityData());
    }

    /**
     * Поиск {@linkplain PersonalityData персональных данных} текущего пользователя.
     *
     * @return {@code Optional<PersonalityData>} с персональными данными
     */
    @Override
    public Optional<PersonalityData> getCurrentUserPersonalityData() {
        return Optional.ofNullable(findByUsername(
                httpServletRequest
                        .getUserPrincipal()
                        .getName()
        ).getPersonalityData());
    }

    /**
     * Обновление {@linkplain PersonalityData персональных данных}.
     *
     * @param userId идентификатор пользователя
     * @param dto    {@linkplain PersonalityDataDTO DTO} персональных данных
     * @return {@code User} с обновленными персональными данными
     */
    @Override
    public User updatePersonalityData(Long userId, @Valid PersonalityDataDTO dto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        PersonalityData personalityData = getPersonalityByUserId(userId).orElse(new PersonalityData());
        user.setPersonalityData(personalityData);
        Set<DocumentPhoto> documentPhotos = documentPhotoService.loadNewDocumentPhoto(user, dto.getFiles());
        personalityData.setFromDTO(dto);
        personalityData.setDocs(documentPhotos);
        user.getRoles().clear();
        user.getRoles().add(Role.ROLE_CLIENT);
        user.setUpdated(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    /**
     * Получение списка {@linkplain User пользователей} с пагинацией.
     *
     * @param page   текущая страница
     * @param atPage количество пользователей на странице
     * @return {@code Page} с пользователями
     */
    @Override
    public Page<User> getAllUsers(@Positive Integer page, @Min(0) @Max(100) Integer atPage) {
        return userRepository.findAll(
                PageRequest.of(
                        page,
                        atPage,
                        Sort.by("id").descending()
                ));
    }

    /**
     * Получить текущего {@linkplain User пользователя}.
     *
     * @return {@code User} текущий пользователь
     */
    @Override
    public User getCurrentUser() {
        Principal principal = httpServletRequest.getUserPrincipal();
        return principal != null
                ? findByUsername(principal.getName())
                : null;
    }

    /**
     * Удаление/восстановление {@linkplain User пользователя}. Удаление происходит посредством установки
     * {@linkplain User#setActive(boolean) флага} в false.
     *
     * @param userId   идентификатор пользователя
     * @param isActive состояние пользователя, false - удален
     * @return {@code User} пользователь
     */
    @Override
    public User setUserActive(Long userId, boolean isActive) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userOptional.get().setActive(isActive);
            return userRepository.save(userOptional.get());
        }
        return null;
    }

    /**
     * Обновить {@linkplain Role роли} {@linkplain User пользователя}.
     *
     * @param userId идентификатор пользователя
     * @param roles  массив устанавливаемых ролей
     * @return {@code User} пользователь с изменными ролями
     */
    @Override
    public User updateRoles(Long userId, Role[] roles) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            if (!getCurrentUser().getRoles().contains(Role.ROLE_ADMIN)) {
                roles = Arrays.stream(roles).filter(p -> p != Role.ROLE_ADMIN).toArray(Role[]::new);
            }
            userOptional.get().getRoles().clear();
            userOptional.get().getRoles().addAll(Arrays.asList(roles));
            return userRepository.save(userOptional.get());
        }
        return null;
    }
}
