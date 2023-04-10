package m7.only.carrental.service;

import java.nio.file.Path;

public interface PersonalityDataService {
    Path getPersonalityDataPath(Long userId);

    Path getNewPersonalityDataPath(Long userId);

    Path getPersonalityDataPath(Long userId, String fileName);
}
