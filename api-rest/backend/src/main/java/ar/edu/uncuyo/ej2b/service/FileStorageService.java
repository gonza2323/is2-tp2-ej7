package ar.edu.uncuyo.ej2b.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FileStorageService {

    private static final Path BASE_PATH = Paths.get(System.getProperty("user.dir"), "uploads");

    public String saveFile(MultipartFile file, String nombreLibro) {
        try {
            Path dirPath = Paths.get(BASE_PATH.toUri());
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String fileName = "libro_" + nombreLibro.replaceAll("\\s+", "_") + "_.pdf";
            Path filePath = dirPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo PDF", e);
        }
    }
}
