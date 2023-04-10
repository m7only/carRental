package m7.only.carrental.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import m7.only.carrental.entity.User;
import m7.only.carrental.entity.user.PersonalityData;
import m7.only.carrental.entity.user.PersonalityDataDTO;
import m7.only.carrental.entity.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.Optional;

public interface UserService {
    User findByUsername(String username);

    String register(@Valid User user,
                    @NotBlank String confirmedPassword,
                    Model model);

    Optional<PersonalityData> getPersonalityByUserId(@NotBlank Long userId);

    Optional<PersonalityData> getCurrentUserPersonalityData();

    User updatePersonalityData(Long userId, @Valid PersonalityDataDTO dto);

    Page<User> getAllUsers(@Positive Integer page, @Min(0) @Max(100) Integer atPage);

    User getCurrentUser();

//    User setClientType(Long userId, ClientType clientType);

    User setUserActive(Long userId, boolean isActive);

    User updateRoles(Long userId, Role[] roles);
}
