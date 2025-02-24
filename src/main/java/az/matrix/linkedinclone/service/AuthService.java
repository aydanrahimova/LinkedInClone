package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.AuthRequest;
import az.matrix.linkedinclone.dto.request.RecoveryPassword;
import az.matrix.linkedinclone.dto.request.UserRequest;
import az.matrix.linkedinclone.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponse register(UserRequest userRequest);

    AuthResponse login(AuthRequest authRequest);

    void requestPasswordReset(String email);

    void resetPassword(String token, RecoveryPassword recoveryPassword);
}
