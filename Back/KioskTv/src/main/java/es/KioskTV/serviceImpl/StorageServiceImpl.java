package es.KioskTV.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.KioskTV.service.StorageService;
import jakarta.annotation.PostConstruct;

/**
 * Implementation of the StorageService interface for file storage operations.
 */
@Service
public class StorageServiceImpl implements StorageService {

    /**
     * The directory where files will be uploaded, retrieved from application properties.
     */
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Initializes the storage service by creating the upload directory if it does not exist.
     */
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    /**
     * Stores a file in the upload directory.
     *
     * @param file the file to store
     * @return the path to the stored file
     * @throws Exception if the file cannot be stored
     */
    @Override
    public String store(MultipartFile file) throws Exception {
        try {
            Path destinationFile = Paths.get(uploadDir).resolve(
                    Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(Paths.get(uploadDir).toAbsolutePath())) {
                throw new Exception("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return destinationFile.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    /**
     * Loads a file from the upload directory.
     *
     * @param filename the name of the file to load
     * @return the path to the loaded file
     */
    @Override
    public Path load(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }
}