package az.matrix.linkedinclone.utility;

import az.matrix.linkedinclone.exception.FileIOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
public class MediaUploadUtil {

    public static String uploadPhoto(MultipartFile file, String UPLOAD_DIR) {
        return uploadFile(file, UPLOAD_DIR, MimeTypeUtil.IMAGES);
    }

    public static String uploadResume(MultipartFile file, String UPLOAD_DIR) {
        return uploadFile(file, UPLOAD_DIR, MimeTypeUtil.RESUMES);
    }

    public static String uploadMediaForPost(MultipartFile file, String UPLOAD_DIR) {
        return uploadFile(file, UPLOAD_DIR, MimeTypeUtil.MEDIA_FOR_POST);
    }

//    @SneakyThrows
    private static String uploadFile(MultipartFile file, String UPLOAD_DIR, List<String> validTypes) {
        try {
            validateFile(file, validTypes);
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            return filePath.toString();
        } catch (Exception e) {
            throw new FileIOException("File upload failed: " + e.getMessage());
        }
    }

    private static void validateFile(MultipartFile file, List<String> validTypes) {
        if (file.isEmpty()) {
            throw new FileIOException("FILE_CANT_BE_EMPTY");
        }
        String contentType = file.getContentType();
        if (contentType == null || validTypes.stream().noneMatch(contentType::startsWith)) {
            System.out.println(validTypes);
            throw new FileIOException("UNSUPPORTED_FILE_TYPE");
        }
    }

    private static String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

}