package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Authority;
import az.matrix.linkedinclone.dao.entity.PasswordResetToken;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.AuthorityRepository;
import az.matrix.linkedinclone.dao.repo.PasswordResetTokenRepository;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.dto.request.AuthRequest;
import az.matrix.linkedinclone.dto.request.RecoveryPassword;
import az.matrix.linkedinclone.dto.request.UserRequest;
import az.matrix.linkedinclone.dto.response.AuthResponse;
import az.matrix.linkedinclone.enums.EmailTemplate;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.AlreadyExistException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.exception.UnauthorizedException;
import az.matrix.linkedinclone.service.AuthService;
import az.matrix.linkedinclone.service.EmailSenderService;
import az.matrix.linkedinclone.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthorityRepository authorityRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        log.info("Attempting to authenticate user with email: {}", authRequest.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
            User user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> {
                        log.warn("User with email {} not found.", authRequest.getEmail());
                        return new ResourceNotFoundException(User.class);
                    });

            if (user.getStatus() == EntityStatus.DEACTIVATED && !user.getDeactivatedByAdmin()) {
                log.info("User with email {} is deactivated. Reactivating the account.", authRequest.getEmail());
                user.setStatus(EntityStatus.ACTIVE);
                user.setDeactivationDate(null);
                userRepository.save(user);
            }

            if (user.getStatus() == EntityStatus.DELETED) {
                log.warn("User with email {} is deleted. Cannot authenticate.", authRequest.getEmail());
                throw new ResourceNotFoundException(User.class);
            }

            String jwtToken = jwtUtil.createToken(user);

            log.info("Authentication successful for user with email: {}", authRequest.getEmail());

            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();

        } catch (BadCredentialsException ex) {
            log.warn("Invalid login attempt for email: {}", authRequest.getEmail());
            throw new UnauthorizedException("Invalid email or password.");
        }
    }


    @Override
    public AuthResponse register(UserRequest userRequest) {
        log.info("Registration process for user is started.");
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            log.warn("User with email {} already exist.", userRequest.getEmail());
            throw new AlreadyExistException(User.class);
        }
        if (!userRequest.getPassword().equals(userRequest.getPasswordConfirm())) {
            log.warn("Password and password confirmation don't match.");
            throw new IllegalArgumentException("PASSWORD_MISMATCHING");
        }

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .status(EntityStatus.ACTIVE)
                .build();
        Authority defaultRole = authorityRepository.findByName("USER");
        if (defaultRole == null) {
            defaultRole = new Authority(null, "USER", null);
            authorityRepository.save(defaultRole);
        }
        user.setAuthorities(List.of(defaultRole));
        userRepository.save(user);

        var jwtToken = jwtUtil.createToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }


    @Override
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(User.class));
        log.info("Requesting password reset started by user with ID {}", user.getId());
        String token = generateToken();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expirationTime(LocalDateTime.now().plusMinutes(15))
                .build();
        passwordResetTokenRepository.save(passwordResetToken);
        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + token;
        Map<String, String> placeholders = Map.of("userName", user.getFirstName(), "resetUrl", resetUrl);
        emailSenderService.sendEmail(email, EmailTemplate.PASSWORD_RESET, placeholders);
    }

    @Override
    public void resetPassword(String token, RecoveryPassword recoveryPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException(PasswordResetToken.class));
        User user = resetToken.getUser();
        if (isExpired(resetToken)) {
            throw new IllegalArgumentException("TOKEN_EXPIRED");
        }
        if (!recoveryPassword.getNewPassword().equals(recoveryPassword.getRetryPassword())) {
            throw new IllegalArgumentException("PASSWORDS_MISMATCHING");
        }
        user.setPassword(passwordEncoder.encode(recoveryPassword.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
        log.info("Password was successfully recovered");

    }

    private boolean isExpired(PasswordResetToken resetToken) {
        return resetToken.getExpirationTime().isBefore(LocalDateTime.now());
    }

    public static String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}
