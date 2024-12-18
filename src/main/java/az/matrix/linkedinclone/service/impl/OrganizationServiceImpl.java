package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.OrganizationAdmin;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.OrganizationAdminRepo;
import az.matrix.linkedinclone.dao.repo.OrganizationRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;
import az.matrix.linkedinclone.enums.OrganizationPermission;
import az.matrix.linkedinclone.enums.OrganizationRole;
import az.matrix.linkedinclone.enums.ProfileStatus;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.exception.UnauthorizedException;
import az.matrix.linkedinclone.mapper.OrganizationAdminMapper;
import az.matrix.linkedinclone.mapper.OrganizationMapper;
import az.matrix.linkedinclone.service.OrganizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepo organizationRepo;
    private final OrganizationMapper organizationMapper;
    private final UserRepo userRepo;
    private final OrganizationAdminRepo organizationAdminRepo;
    private final OrganizationAdminMapper organizationAdminMapper;

    @Override
    public OrganizationResponse getOrganization(Long id) {
        log.info("Operation of getting organization with ID {} started.", id);
        Organization organization = organizationRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to get organization: Organization with ID {} not found.", id);
                    return new ResourceNotFoundException("ORGANIZATION_NOT_FOUND");
                });
        log.info("Organization with ID {} successfully returned", id);
        return organizationMapper.toDto(organization);
    }

    @Transactional
    @Override
    public OrganizationResponse createOrganization(OrganizationRequest organizationRequest) {
        String authenticatedUseEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of creating new organization by user {} started.", authenticatedUseEmail);
        User user = userRepo.findByEmail(authenticatedUseEmail).orElseThrow(() -> {
            log.info("Failed to create new organization by user {}: User not found.", authenticatedUseEmail);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        Organization organization = organizationMapper.toEntity(organizationRequest);
        organization.setStatus(ProfileStatus.ACTIVE);

        OrganizationAdmin organizationAdmin = new OrganizationAdmin();
        organizationAdmin.setOrganization(organization);
        organizationAdmin.setAdmin(user);
        organizationAdmin.setRole(OrganizationRole.SUPER_ADMIN);

        organization.getAdmins().add(organizationAdmin);

        organizationRepo.save(organization);
        log.info("New organization is successfully created.");
        return null;
    }

    @Transactional
    @Override
    public OrganizationResponse editOrganization(Long id, OrganizationRequest organizationRequest) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of editing organization with ID {} by user {} started.", id, authenticatedUserEmail);
        User admin = userRepo.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to edit organization page with ID {}: User {} not found", id, authenticatedUserEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });
//        Organization organization = organizationRepo.findById(id)
//                .orElseThrow(() -> {
//                    log.warn("Failed to edit organization page: Organization with ID {} not found", id);
//                    return new ResourceNotFoundException("ORGANIZATION_NOT_FOUND");
//                });
//        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganizationId(admin, id)
//                .orElseThrow(() -> {
//                    log.warn("Failed to edit organization page with ID {}:User {} is not admin of that page", id, authenticatedUserEmail);
//                    return new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
//                });
//     organization ucun ayri yoxlama? yoxsa umumi yoxlamaliyiq??
        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganizationId(admin, id)
                .orElseThrow(() -> {
                    log.warn("Failed to edit organization with ID {}: User {} is not an admin or organization not found.", id, authenticatedUserEmail);
                    return new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });
        if (!organizationAdmin.getRole().hasPermission(OrganizationPermission.EDIT_ORGANIZATION_PROFILE)) {
            log.warn("Failed to edit organization page with ID {}: For editing the page admin must be SuperAdmin", id);
            throw new UnauthorizedException("INSUFFICIENT_PERMISSIONS");
        }
        Organization organization = organizationAdmin.getOrganization();
        organizationMapper.mapForUpdate(organization, organizationRequest);
        organizationRepo.save(organization);
        log.info("Organization with ID {} successfully updated", id);
        return organizationMapper.toDto(organization);
    }

    @Override
    @Transactional
    //tekrarlanir edit ile
    public void deactivateOrganization(Long id) {
        String authenticatedUseEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of deactivation organization with ID {} by user {} started.", id, authenticatedUseEmail);
        User admin = userRepo.findByEmail(authenticatedUseEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to deactivate organization page with ID {}: User {} not found", id, authenticatedUseEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });
        Organization organization = organizationRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to deactivate organization page: Organization with ID {} not found", id);
                    return new ResourceNotFoundException("ORGANIZATION_NOT_FOUND");
                });
        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganization(admin, organization)
                .orElseThrow(() -> {
                    log.warn("Failed to deactivate organization page with ID {}:User {} is not admin of that page", id, authenticatedUseEmail);
                    return new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });
        if (!organizationAdmin.getRole().hasPermission(OrganizationPermission.EDIT_ORGANIZATION_PROFILE)) {
            log.warn("Failed to deactivate organization page with ID {}: For editing the page admin must be SuperAdmin", id);
            throw new UnauthorizedException("INSUFFICIENT_PERMISSIONS");
        }
        organization.setStatus(ProfileStatus.DEACTIVATED);
        organizationRepo.save(organization);
    }

    @Override
    @Transactional
    public void addAdmin(Long id, Long userId, OrganizationRole role) {
        String authenticatedUseEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of adding new admin of organization with ID {} by user {} started.", id, authenticatedUseEmail);
        User admin = userRepo.findByEmail(authenticatedUseEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to add new admin to organization page with ID {}: User {} not found", id, authenticatedUseEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });
        User newAdmin = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Failed to add new admin to organization page with ID {}: User {} not found for adding new admin", id, authenticatedUseEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });
//        Organization organization = organizationRepo.findById(id)
//                .orElseThrow(() -> {
//                    log.warn("Failed to add new admin to organization page: Organization with ID {} not found", id);
//                    return new ResourceNotFoundException("ORGANIZATION_NOT_FOUND");
//                });
        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganizationId(admin, id)
                .orElseThrow(() -> {
                    log.warn("Failed to add new admin to organization page with ID {}:User {} is not admin of that page", id, authenticatedUseEmail);
                    return new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });

        if (!organizationAdmin.getRole().hasPermission(OrganizationPermission.MANAGE_ROLES)) {
            log.warn("Failed to add new admin to organization page with ID {}:For adding new admin the admin must be SuperAdmin", id);
            throw new UnauthorizedException("INSUFFICIENT_PERMISSIONS");
        }

        Organization organization = organizationAdmin.getOrganization();

        OrganizationAdmin newOrganizationAdmin = new OrganizationAdmin();
        newOrganizationAdmin.setOrganization(organization);
        newOrganizationAdmin.setAdmin(newAdmin);
        newOrganizationAdmin.setRole(role);

        organization.getAdmins().add(newOrganizationAdmin);
        organizationRepo.save(organization);
        log.info("New admin added successfully to organization with ID {}", id);


    }

    @Override
    @Transactional
    public void changeAdminRole(Long id, Long userId, OrganizationRole organizationRole) {
        String authenticatedUseEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of changing admin role for user with ID {} in organization with ID {} by user {} started.", userId, id, authenticatedUseEmail);

        User admin = userRepo.findByEmail(authenticatedUseEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to change admin role for organization with ID {}: User {} not found", id, authenticatedUseEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });

        User adminToChange = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Failed to change admin role for organization with ID {}: User with ID {} not found", id, userId);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });

        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganizationId(admin, id)
                .orElseThrow(() -> {
                    log.warn("User {} is not an admin of organization with ID {}", authenticatedUseEmail, id);
                    return new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });

        OrganizationAdmin organizationAdminToChange = organizationAdminRepo.findByAdminAndOrganizationId(adminToChange, id)
                .orElseThrow(() -> {
                    log.warn("User with ID {} is not an admin of organization with ID {}", userId, id);
                    return new ResourceNotFoundException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });

        if (organizationAdmin.getRole().hasPermission(OrganizationPermission.MANAGE_ROLES)) {
            organizationAdminToChange.setRole(organizationRole);  // Change the admin role
            organizationAdminRepo.save(organizationAdminToChange); // Save the updated admin
            log.info("Successfully changed the role of user {} in organization with ID {}.", userId, id);
        } else {
            log.warn("User {} does not have permission to manage roles in organization with ID {}", authenticatedUseEmail, id);
            throw new UnauthorizedException("INSUFFICIENT_PERMISSIONS");
        }
    }

    @Override
    public void deleteAdmin(Long id, Long userId) {
        String authenticatedUseEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of deleting admin with ID {} from organization with ID {} by user {} started.", userId, id, authenticatedUseEmail);

        User admin = userRepo.findByEmail(authenticatedUseEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to delete admin for organization with ID {}: User {} not found", id, authenticatedUseEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });

        User adminToDelete = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Failed to delete admin for organization with ID {}: User with ID {} not found", id, userId);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });

        // Ensure the authenticated user is an admin in the specified organization
        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganizationId(admin, id)
                .orElseThrow(() -> {
                    log.warn("User {} is not an admin of organization with ID {}", authenticatedUseEmail, id);
                    return new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });

        // Ensure the admin to be deleted is an admin in the specified organization
        OrganizationAdmin organizationAdminToDelete = organizationAdminRepo.findByAdminAndOrganizationId(adminToDelete, id)
                .orElseThrow(() -> {
                    log.warn("User with ID {} is not an admin of organization with ID {}", userId, id);
                    return new ResourceNotFoundException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });

        if (organizationAdmin.getRole().hasPermission(OrganizationPermission.MANAGE_ROLES)) {
            if (admin.getId().equals(adminToDelete.getId()) && organizationAdminRepo.existsByOrganizationIdAndRole(id, OrganizationRole.SUPER_ADMIN)) {
                log.warn("User {} cannot delete themselves as an admin in organization with ID {}: as there is not another Super Admin", authenticatedUseEmail, id);
                throw new UnauthorizedException("CANNOT_DELETE_SELF_AS_ADMIN");
            }

            organizationAdminRepo.delete(organizationAdminToDelete);
            log.info("Successfully deleted admin with ID {} from organization with ID {}.", userId, id);

        } else {
            log.warn("User {} does not have permission to manage roles in organization with ID {}", authenticatedUseEmail, id);
            throw new UnauthorizedException("INSUFFICIENT_PERMISSIONS");
        }
    }

    @Override
    public Page<OrganizationAdminResponse> getAllAdmins(Long organizationId, Pageable pageable) {
        String authenticatedUseEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of getting all admins of organization with ID {} by user {} started.", organizationId, authenticatedUseEmail);

        User admin = userRepo.findByEmail(authenticatedUseEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to get admins for organization with ID {}: User {} not found", organizationId, authenticatedUseEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });

        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganizationId(admin, organizationId)
                .orElseThrow(() -> {
                    log.warn("User {} is not an admin of organization with ID {}", authenticatedUseEmail, organizationId);
                    return new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });

        return organizationAdminRepo.findAllByOrganizationId(organizationId, pageable)
                .map(organizationAdminMapper::toDto);
    }


//    private OrganizationAdmin validateAdminAccess(Long organizationId, String currentUserEmail, OrganizationPermission requiredPermission) {
//        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminEmailAndOrganizationId(currentUserEmail, organizationId)
//                .orElseThrow(() -> {
//                    log.warn("User {} is not an admin of organization with ID {}", currentUserEmail, organizationId);
//                    return new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
//                });
//
//        if (!organizationAdmin.getRole().hasPermission(requiredPermission)) {
//            log.warn("User {} does not have sufficient permissions to perform this action.", currentUserEmail);
//            throw new UnauthorizedException("INSUFFICIENT_PERMISSIONS");
//        }
//
//        return organizationAdmin;
//    }


}
