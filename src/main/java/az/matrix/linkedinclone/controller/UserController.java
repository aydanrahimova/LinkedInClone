package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.*;
import az.matrix.linkedinclone.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> getUserById(@PathVariable Long id) {
        UserDetailsResponse userDetails = userService.getById(id);
        return ResponseEntity.ok(userDetails);
    }

    @PutMapping
    public ResponseEntity<UserDetailsResponse> editUserInfo(@Validated @RequestBody UserRequest userDetailsRequest) {
        UserDetailsResponse updatedUser = userService.editUserInfo(userDetailsRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(value = "/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> changeProfilePhoto(@RequestPart MultipartFile file) throws IOException {
        userService.changeProfilePhoto(file);
        return ResponseEntity.ok("Profile photo updated successfully");
    }

    @DeleteMapping("/profile-photo")
    public ResponseEntity<String> deleteProfilePhoto() {
        userService.deleteExistingProfilePhoto();
        return ResponseEntity.ok("Profile photo deleted successfully");
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivateUser(@RequestBody String password) {
        userService.deactivateUser(password);
        return ResponseEntity.ok("User deactivated successfully");
    }

    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PatchMapping("/activate-by-admin/{id}")
    public ResponseEntity<String> activateByAdmin(@PathVariable Long id) {
        userService.activateUserByAdmin(id);
        return ResponseEntity.ok("User activated by admin successfully");
    }

    @PatchMapping("/deactivate-by-admin/{id}")
    public ResponseEntity<String> deactivateByAdmin(@PathVariable Long id) {
        userService.deactivateUserByAdmin(id);
        return ResponseEntity.ok("User deactivated by admin successfully");
    }
}
