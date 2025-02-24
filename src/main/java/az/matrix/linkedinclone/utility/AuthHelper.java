package az.matrix.linkedinclone.utility;

import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHelper {

    private final UserRepository userRepository;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("No authenticated user found in the security context.");
            throw new UnauthorizedException("No authenticated user found");
        }

        String authenticatedEmail = authentication.getName();
        log.debug("Authenticated user email: {}", authenticatedEmail);

        return userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> {
                    log.error("User with email {} not found in the database", authenticatedEmail);
                    return new ResourceNotFoundException(User.class);
                });
    }
}

