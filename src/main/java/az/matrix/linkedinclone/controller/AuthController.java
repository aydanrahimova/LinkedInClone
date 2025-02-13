package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.AuthRequest;
import az.matrix.linkedinclone.dto.request.RecoveryPassword;
import az.matrix.linkedinclone.dto.request.UserRequest;
import az.matrix.linkedinclone.dto.response.AuthResponse;
import az.matrix.linkedinclone.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse register(@Valid @RequestBody UserRequest userRequest) {
        return authService.register(userRequest);
    }

    @PostMapping("/sign-in")
    public AuthResponse login(@Valid @RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/forgot-password")
    public void requestPasswordReset(@Valid @NotBlank @RequestParam String email) {
        authService.requestPasswordReset(email);
    }

    @PatchMapping("/reset-password")
    public void resetPassword(@RequestParam @NotBlank String token, @Valid @RequestBody RecoveryPassword recoveryPassword) {
        authService.resetPassword(token,recoveryPassword);
    }
}
