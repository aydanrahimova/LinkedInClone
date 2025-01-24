package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.OrganizationAdmin;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.OrganizationAdminRepo;
import az.matrix.linkedinclone.dao.repo.OrganizationRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import az.matrix.linkedinclone.enums.OrganizationPermission;
import az.matrix.linkedinclone.enums.OrganizationRole;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.exception.UnauthorizedException;
import az.matrix.linkedinclone.mapper.OrganizationAdminMapper;
import az.matrix.linkedinclone.service.OrganizationAdminService;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationAdminServiceImpl implements OrganizationAdminService {
    private final OrganizationAdminRepo organizationAdminRepo;
    private final UserRepo userRepo;
    private final OrganizationRepo organizationRepo;
    private final OrganizationAdminMapper organizationAdminMapper;
    private final AuthHelper authHelper;

    @Override
    public boolean isAdmin(User admin, Organization organization) {
        return organizationAdminRepo.existsByAdminAndOrganization(admin, organization);
    }

    @Override
    @Transactional
    public OrganizationAdminResponse addAdmin(Long id, Long userId, OrganizationRole role) {
        User admin = authHelper.getAuthenticatedUser();

        log.info("Operation of adding new admin of organization with ID {} by user with email {} started.", id, admin.getEmail());

        User newAdmin = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(User.class));

        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganizationId(admin, id)
                .orElseThrow(() -> {
                    log.warn("Failed to add new admin to organization page with ID {}:User {} is not admin of that page", id, admin.getEmail());
                    return new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });

        if (!organizationAdmin.getRole().hasPermission(OrganizationPermission.MANAGE_ROLES)) {
            log.warn("Failed to add new admin to organization page with ID {}:For adding new admin the admin must be SuperAdmin", id);
            throw new UnauthorizedException("INSUFFICIENT_PERMISSIONS");
        }

        Organization organization = organizationAdmin.getOrganization();

        OrganizationAdmin organizationAdmin1 = OrganizationAdmin.builder()
                .admin(newAdmin)
                .organization(organization)
                .role(role)
                .build();

//        OrganizationAdmin newOrganizationAdmin = new OrganizationAdmin();
//        newOrganizationAdmin.setOrganization(organization);
//        newOrganizationAdmin.setAdmin(newAdmin);
//        newOrganizationAdmin.setRole(role);
//
//        organization.getAdmins().add(newOrganizationAdmin);
//        organizationRepo.save(organization);
        organizationAdminRepo.save(organizationAdmin1);
        log.info("New admin added successfully to organization with ID {}", id);

        return organizationAdminMapper.toDto(organizationAdmin);
    }

    @Override
    @Transactional
    public OrganizationAdminResponse changeAdminRole(Long id, Long userId, OrganizationRole organizationRole) {
        User admin = authHelper.getAuthenticatedUser();
        log.info("Operation of changing admin role for user with ID {} in organization with ID {} by user {} started.", userId, id, admin.getEmail());

        User adminToChange = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Failed to change admin role for organization with ID {}: User with ID {} not found", id, userId);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });

        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganizationId(admin, id)
                .orElseThrow(() -> {
                    log.warn("User {} is not an admin of organization with ID {}", admin.getEmail(), id);
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
            log.warn("User {} does not have permission to manage roles in organization with ID {}", admin.getEmail(), id);
            throw new UnauthorizedException("INSUFFICIENT_PERMISSIONS");
        }
        return organizationAdminMapper.toDto(organizationAdmin);
    }

    @Override
    public void deleteAdmin(Long id, Long userId) {
        User admin = authHelper.getAuthenticatedUser();
        log.info("Operation of deleting admin with ID {} from organization with ID {} by user {} started.", userId, id, admin.getEmail());

        User adminToDelete = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(User.class));

        // Ensure the admin to be deleted is an admin in the specified organization
        OrganizationAdmin organizationAdmin = organizationAdminRepo.findByAdminAndOrganizationId(admin, id)
                .orElseThrow(() -> {
                    log.warn("User with ID {} is not an admin of organization with ID {}", userId, id);
                    return new ResourceNotFoundException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });
        OrganizationAdmin organizationAdminToDelete = organizationAdminRepo.findByAdminAndOrganizationId(adminToDelete, id)
                .orElseThrow(() -> {
                    log.warn("User with ID {} is not an admin of organization with ID {}", userId, id);
                    return new ResourceNotFoundException("USER_NOT_ADMIN_OF_ORGANIZATION");
                });

        if (organizationAdmin.getRole().hasPermission(OrganizationPermission.MANAGE_ROLES)) {
            if (admin.getId().equals(adminToDelete.getId()) && organizationAdminRepo.existsByOrganizationIdAndRole(id, OrganizationRole.SUPER_ADMIN)) {
                log.warn("User {} cannot delete themselves as an admin in organization with ID {}: as there is not another Super Admin", admin.getEmail(), id);
                throw new UnauthorizedException("CANNOT_DELETE_SELF_AS_ADMIN");
            }

            organizationAdminRepo.delete(organizationAdminToDelete);
            log.info("Successfully deleted admin with ID {} from organization with ID {}.", userId, id);

        } else {
            log.warn("User {} does not have permission to manage roles in organization with ID {}", admin.getEmail(), id);
            throw new UnauthorizedException("INSUFFICIENT_PERMISSIONS");
        }
    }

    @Override
    public Page<OrganizationAdminResponse> getAllAdmins(Long organizationId, Pageable pageable) {

        User admin = authHelper.getAuthenticatedUser();

        Organization organization = organizationRepo.findById(organizationId).orElseThrow(() -> new ResourceNotFoundException(Organization.class));

        if (!isAdmin(admin, organization)) {
            throw new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
        }
        Page<OrganizationAdmin> admins = organizationAdminRepo.findAllByOrganizationId(organizationId, pageable);

        Page<OrganizationAdminResponse> adminResponses = admins.map(organizationAdminMapper::toDto);

        log.info("All admins of organization with ID {} returned successfully", organizationId);

        return adminResponses;

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
