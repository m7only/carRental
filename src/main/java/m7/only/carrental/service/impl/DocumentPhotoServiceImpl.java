package m7.only.carrental.service.impl;

import m7.only.carrental.entity.User;
import m7.only.carrental.entity.user.DocumentPhoto;
import m7.only.carrental.entity.user.PersonalityData;
import m7.only.carrental.entity.user.Role;
import m7.only.carrental.service.DocumentPhotoService;
import m7.only.carrental.service.FileService;
import m7.only.carrental.service.PersonalityDataService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DocumentPhotoServiceImpl implements DocumentPhotoService {
    private final PersonalityDataService personalityData;
    private final FileService fileService;

    public DocumentPhotoServiceImpl(PersonalityDataService personalityData, FileService fileService) {
        this.personalityData = personalityData;
        this.fileService = fileService;
    }

    /**
     * Загрузка новых {@linkplain DocumentPhoto фотографий документов} {@linkplain PersonalityData персональных данных} в файловое хранилище.
     *
     * @param user  пользователь, которому загружаются фотографии
     * @param files массив фотографий
     * @return {@code Set<DocumentPhoto>} загруженных фотографий
     */
    @Override
    public Set<DocumentPhoto> loadNewDocumentPhoto(User user, MultipartFile[] files) {
        PersonalityData pd = Optional.ofNullable(user.getPersonalityData()).orElse(new PersonalityData());
        Set<DocumentPhoto> documentPhotos = Optional.ofNullable(pd.getDocs()).orElse(new HashSet<>());
        if (Arrays.stream(files).anyMatch(f -> !f.isEmpty())) {
            if (documentPhotos.size() != 0) {
                documentPhotos.forEach(oldPhoto -> fileService
                        .delete(
                                personalityData.getPersonalityDataPath(
                                        user.getId(),
                                        oldPhoto.getFileName()))
                );
                documentPhotos.clear();
            }
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    Path path = personalityData.getNewPersonalityDataPath(user.getId());
                    fileService.upload(file, path);
                    documentPhotos.add(new DocumentPhoto(user.getPersonalityData(), path.getFileName().toString()));
                }
            }
        }
        return documentPhotos;
    }

    /**
     * Получение пути к {@linkplain DocumentPhoto фотографии} с проверкой, имеет ли переданный пользователь
     * право доступа к фотографиям.<br/>
     * Доступ к {@linkplain PersonalityData персональным данным} имеет только пользователь,
     * к которому персональные данные относятся и пользователи
     * с ролями {@linkplain  m7.only.carrental.entity.user.Role#ROLE_ADMIN ADMIN}
     * и {@linkplain  m7.only.carrental.entity.user.Role#ROLE_MANAGER MANAGER}
     *
     * @param user       пользователь, пытающийся получить путь к фотографии
     * @param userIdPath идентификатор пользователя, к фотографиям которого пытаются получить путь
     * @param fileName   название файла фотографии
     * @return {@code Path} Путь к фотографии, null - если доступ запрещен
     */
    @Override
    public Path getPathToDocumentPhoto(User user, Long userIdPath, String fileName) {
        return user != null && (userIdPath.equals(user.getId())
                || user.getRoles().stream().anyMatch(Set.of(Role.ROLE_ADMIN, Role.ROLE_MANAGER)::contains))
                ? personalityData.getPersonalityDataPath(userIdPath, fileName)
                : null;
    }
}
