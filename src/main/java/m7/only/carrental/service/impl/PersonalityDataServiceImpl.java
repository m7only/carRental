package m7.only.carrental.service.impl;

import m7.only.carrental.service.PersonalityDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.UUID;

@Service
public class PersonalityDataServiceImpl implements PersonalityDataService {
    /**
     * Путь к хранилищу {@linkplain m7.only.carrental.entity.user.PersonalityData фотографий}
     * {@linkplain m7.only.carrental.entity.user.PersonalityData пользователей}.
     */
    @Value("${path.to.personalData.storage}")
    private String personalityDataPath;

    /**
     * Формирование пути к хранилищу {@linkplain m7.only.carrental.entity.user.DocumentPhoto фотографий}.
     *
     * @param userId идентификатор пользователя
     * @return {@code Path} к хранилищу с фотографиями пользователя
     */
    @Override
    public Path getPersonalityDataPath(Long userId) {
        String dataPath = personalityDataPath + "/" + userId.toString() + "/";
        return Path.of(dataPath);
    }

    /**
     * Формирования пути к новой {@linkplain m7.only.carrental.entity.user.DocumentPhoto фотографии}
     * с присвоением ей уникального названия.
     *
     * @param userId идентификатор пользователя
     * @return {@code Path} к фотографии
     */
    @Override
    public Path getNewPersonalityDataPath(Long userId) {
        String fileName = UUID.randomUUID() + "." + "jpg";
        return Path.of(getPersonalityDataPath(userId).toString(), fileName);
    }

    /**
     * Формирование полного пути к файлу {@linkplain m7.only.carrental.entity.user.DocumentPhoto фотографии}.
     *
     * @param userId   идентификатор пользователя
     * @param fileName название файла фотографии
     * @return {@code Path} к фотографии
     */
    @Override
    public Path getPersonalityDataPath(Long userId, String fileName) {
        return Path.of(getPersonalityDataPath(userId).toString(), fileName);
    }
}
