package az.matrix.linkedinclone.utility;

import az.matrix.linkedinclone.exception.FileIOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public class UploadPathUtility {
    public static String uploadPath(MultipartFile file, String UPLOAD_DIR, Long userId, String userName, String userSurname) throws FileIOException {
        try {
            String forFolderUserName = userName.replaceAll("[^a-zA-Z0-9]", "_");
            String forFolderUserSurname = userSurname.replaceAll("[^a-zA-Z0-9]", "_");
            String forFolderUserId = "user_" + userId;
            String uniqueDirName = forFolderUserId + "_" + forFolderUserName + "_" + forFolderUserSurname;

            Path userDir = Paths.get(UPLOAD_DIR, uniqueDirName);
            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }

            String originalFilename = file.getOriginalFilename();
            Path targetPath = Paths.get(UPLOAD_DIR, originalFilename);

            while (Files.exists(targetPath)) {
                String baseName = FilenameUtils.getBaseName(originalFilename);
                String extension = FilenameUtils.getExtension(originalFilename);
                String newFilename = baseName + "_" + UUID.randomUUID() + "." + extension;
                targetPath = userDir.resolve(newFilename);
            }
            Files.copy(file.getInputStream(), targetPath);
            return targetPath.toString();
        } catch (Exception e) {
            log.error("error due to " + e.getMessage());
            throw new FileIOException("sdfdsnj");
        }
    }
}