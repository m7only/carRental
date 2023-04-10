package m7.only.carrental.controller;

import m7.only.carrental.entity.user.Role;
import m7.only.carrental.service.CarPhotoService;
import m7.only.carrental.service.DocumentPhotoService;
import m7.only.carrental.service.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Контроллер для отправки фотографий через {@code ResponseEntity} из хранилища файлов.
 * Реализован для сокрытия персональных данных (документов) пользователей.
 */
@RestController
public class FileStorageController {
    private final DocumentPhotoService documentPhotoService;
    private final UserService userService;
    private final CarPhotoService carPhotoService;

    public FileStorageController(DocumentPhotoService documentPhotoService,
                                 UserService userService,
                                 CarPhotoService carPhotoService) {
        this.documentPhotoService = documentPhotoService;
        this.userService = userService;
        this.carPhotoService = carPhotoService;
    }

    /**
     * Метод отправки фотографии персональных данных с проверкой о принадлежности запрашиваемых фотографий
     * пользователю.<br/>
     * Персональные данные(фото документов) доступны только пользователю,
     * к которому они относятся, и пользователям с ролями {@linkplain Role#ROLE_ADMIN ADMIN}
     * и {@linkplain Role#ROLE_MANAGER MANAGER}
     *
     * @param userId   иденификатор пользователя, чья фотография запрашиваются
     * @param fileName имя запрашиваемого файла
     * @return {@code ResponseEntity<InputStreamResource>} изображение {@code MediaType.IMAGE_JPEG}
     * @throws IOException в случае, если изображение не найдено
     */
    @GetMapping(value = "/account/personalData/{userId}/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> getDocumentsPhoto(@PathVariable Long userId,
                                                                 @PathVariable String fileName) throws IOException {
        Path path = documentPhotoService.getPathToDocumentPhoto(userService.getCurrentUser(), userId, fileName);
        return path != null
                ? ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(Files.size(path))
                .body(new InputStreamResource(Files.newInputStream(path)))
                : ResponseEntity.notFound().build();
    }

    /**
     * Выдача фото автомобилей по аналогии с выдачей фото персональных данных.<br/>
     * Доступ имеют все пользователи приложения, в т.ч. не прошедшие аутентификацию.
     *
     * @param carId    идентификатор аатомобиля
     * @param fileName имя файла
     * @return {@code ResponseEntity<InputStreamResource>} изображение {@code MediaType.IMAGE_JPEG}
     * @throws IOException в случае, если изображение не найдено
     */
    @GetMapping(value = "/cars/{carId}/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> getPhotoCar(@PathVariable Long carId,
                                                           @PathVariable String fileName) throws IOException {
        Path path = carPhotoService.getPathToCarPhoto(carId, fileName);
        return path != null
                ? ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(Files.size(path))
                .body(new InputStreamResource(Files.newInputStream(path)))
                : ResponseEntity.notFound().build();
    }
}
