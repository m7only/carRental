package m7.only.carrental.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

public interface FileService {

    Path save(String data, Path path);

    Optional<String> read(Path path);

    void delete(Path path);

    boolean upload(MultipartFile file, Path path);

    boolean createAndSavePreview(MultipartFile file, String filesPath, String originalFileName);
}
