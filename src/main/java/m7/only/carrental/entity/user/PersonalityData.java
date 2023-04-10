package m7.only.carrental.entity.user;

import jakarta.persistence.*;
import lombok.*;
import m7.only.carrental.entity.User;

import java.util.Set;

/**
 * Персональные данные {@linkplain User пользователя}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personality_data")
public class PersonalityData {
    /**
     * Идентификатор персональных данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Фамилия пользователя.
     */
    private String lastName;

    /**
     * Отчество пользователя.
     */
    private String patronymic;

    /**
     * Номер прав пользователя.
     */
    private String driverLicenseNumber;

    /**
     * Номер паспорта пользователя.
     */
    private String passportNumber;

    /**
     * Телефон пользователя.
     */
    private String phone;

    /**
     * Пользователь, к которому относятся персональные данные.
     */
    @OneToOne(mappedBy = "personalityData")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    /**
     * {@linkplain DocumentPhoto Фотографии документов} пользователя.
     */
    @OneToMany(mappedBy = "data", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<DocumentPhoto> docs;

    /**
     * Метод для инициализации полей из {@linkplain PersonalityDataDTO DTO}.
     *
     * @param dto {@linkplain PersonalityDataDTO DTO} персональных данных
     */
    public void setFromDTO(PersonalityDataDTO dto) {
        setName(dto.getName());
        setLastName(dto.getLastName());
        setPatronymic(dto.getPatronymic());
        setPassportNumber(dto.getPassportNumber());
        setDriverLicenseNumber(dto.getDriverLicenseNumber());
        setPhone(dto.getPhone());
    }
}
