package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Authority;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.AuthorityRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.AuthRequest;
import az.matrix.linkedinclone.dto.request.UserRequest;
import az.matrix.linkedinclone.dto.response.AuthResponse;
import az.matrix.linkedinclone.enums.ProfileStatus;
import az.matrix.linkedinclone.exception.AlreadyExistException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.exception.UnauthorizedException;
import az.matrix.linkedinclone.service.AuthService;
import az.matrix.linkedinclone.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthorityRepo authorityRepo;

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
            User user = userRepo.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> {
                        log.warn("User with email {} not found.", authRequest.getEmail());
                        return new ResourceNotFoundException("USER_NOT_FOUND");
                    });

            if (user.getStatus() == ProfileStatus.DEACTIVATED) {
                log.info("User with email {} is deactivated. Reactivating the account.", authRequest.getEmail());
                user.setStatus(ProfileStatus.ACTIVE);
                user.setDeactivationDate(null);
                userRepo.save(user);
            }

            if (user.getStatus() == ProfileStatus.DELETED) {
                log.warn("User with email {} is deleted. Cannot authenticate.", authRequest.getEmail());
                throw new ResourceNotFoundException("USER_NOT_FOUND");
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
//        catch (InternalAuthenticationServiceException ex) {
//            log.warn("User not found for email: {}", authRequest.getEmail());
//            throw new UnauthorizedException("Invalid email or password.");
//        }
        catch (Exception ex) {
            log.error("An unexpected error occurred during login for email: {}", authRequest.getEmail(), ex);
            throw new RuntimeException("An error occurred while processing the login request.");
        }
    }


    @Override
    public AuthResponse register(UserRequest userRequest) {
        log.info("Registration process for user is started.");
        if (userRepo.existsByEmail(userRequest.getEmail())) {
            log.warn("User with email {} already exist.", userRequest.getEmail());
            throw new AlreadyExistException("USER_ALREADY_EXIST");
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
                .status(ProfileStatus.ACTIVE)
                .build();
        Authority defaultRole = authorityRepo.findByName("USER");
        if (defaultRole == null) {
            defaultRole = new Authority(null, "USER", null);
            authorityRepo.save(defaultRole);
        }
        user.setAuthorities(List.of(defaultRole));
        userRepo.save(user);

        var jwtToken = jwtUtil.createToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

}
