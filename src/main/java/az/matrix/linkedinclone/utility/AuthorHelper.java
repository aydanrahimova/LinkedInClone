package az.matrix.linkedinclone.utility;

import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.OrganizationRepository;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.dto.response.AuthorResponse;
import az.matrix.linkedinclone.enums.AuthorType;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.exception.UnauthorizedException;
import az.matrix.linkedinclone.service.OrganizationAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorHelper {
    private final OrganizationAdminService organizationAdminService;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public boolean validatePermission(AuthorType authorType, Long authorId, User user) {
        log.info("Validating permission for user {} to manipulate entity with authorId {} and authorType {} started", user.getId(), authorId, authorType);
        if (authorType == AuthorType.ORGANIZATION) {
            return organizationAdminService.isAdmin(user, authorId);
        } else {
            return authorId.equals(user.getId());
        }
    }

    public Pair<Long, AuthorType> determineAuthorDetails(User user, Long organizationId) {
        if (organizationId != null) {
            if (!organizationAdminService.isAdmin(user, organizationId)) {
                log.warn("User with ID {} is not authorized to act as organization {}", user.getId(), organizationId);
                throw new UnauthorizedException("User is not authorized to act as the organization.");
                //accessdenied
            }
            return Pair.of(organizationId, AuthorType.ORGANIZATION);
        } else {
            return Pair.of(user.getId(), AuthorType.USER);
        }
    }

    public AuthorResponse getAuthor(Long authorId, AuthorType authorType) {
        if (authorType == AuthorType.ORGANIZATION) {
            Organization organization = organizationRepository.findByIdAndStatus(authorId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
            return new AuthorResponse(organization.getId(), organization.getLogoPath(), organization.getName(), authorType);
        } else {
            User user = userRepository.findByIdAndStatus(authorId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(User.class));
            return new AuthorResponse(user.getId(), user.getPhotoUrl(), String.join(" ", user.getFirstName(), user.getLastName()), authorType);
        }
    }
}