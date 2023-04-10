package m7.only.carrental.entity.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO для сущности {@linkplain PersonalityData PersonalityData}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalityDataDTO {
    /**
     * Имя пользователя.
     */
    @NotBlank
    private String name;

    /**
     * Фамилия  пользователя.
     */
    @NotBlank
    private String lastName;

    /**
     * Отчество пользователя.
     */
    @NotBlank
    private String patronymic;

    /**
     * Номер прав пользователя.
     */
    @NotBlank
    private String driverLicenseNumber;

    /**
     * Номер паспорта пользователя.
     */
    @NotBlank
    private String passportNumber;

    /**
     * Телефон пользователя.
     */
    @NotBlank
    private String phone;

    /**
     * {@linkplain DocumentPhoto Фотографии} персональных данных пользователя.
     */
    @NotBlank
    private MultipartFile[] files;
}
