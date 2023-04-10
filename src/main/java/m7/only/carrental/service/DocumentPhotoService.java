package m7.only.carrental.service;

import m7.only.carrental.entity.User;
import m7.only.carrental.entity.user.DocumentPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Set;

public interface DocumentPhotoService {
    Set<DocumentPhoto> loadNewDocumentPhoto(User user, MultipartFile[] files);

    Path getPathToDocumentPhoto(User user, Long userIdPath, String fileName);
}
