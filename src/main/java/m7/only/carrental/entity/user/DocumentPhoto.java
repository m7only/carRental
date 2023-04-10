package m7.only.carrental.entity.user;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность фотографии {@linkplain m7.only.carrental.entity.User пользователя}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personality_docs_photo")
public class DocumentPhoto {
    /**
     * Идентификатор фотографии.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Персональные данные пользователя.
     */
    @ManyToOne
    @JoinColumn(name = "data_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PersonalityData data;

    /**
     * Название файла.
     */
    private String fileName;

    public DocumentPhoto(PersonalityData personalityData, String fileName) {
        this.data = personalityData;
        this.fileName = fileName;
    }
}
