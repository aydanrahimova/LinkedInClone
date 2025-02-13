package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.OrganizationAdmin;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.OrganizationAdminRepository;
import az.matrix.linkedinclone.dao.repo.OrganizationRepository;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.enums.OrganizationPermission;
import az.matrix.linkedinclone.enums.OrganizationRole;
import az.matrix.linkedinclone.exception.ForbiddenException;
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
    private final OrganizationAdminRepository organizationAdminRepository;
    private final UserRepository userRepository;
    private final OrganizationAdminMapper organizationAdminMapper;
    private final AuthHelper authHelper;
    private final OrganizationRepository organizationRepository;

    @Override
    public boolean isAdmin(User admin, Long organizationId) {
        return organizationAdminRepository.existsByAdminAndOrganizationId(admin, organizationId);
    }

    @Override
    public Page<OrganizationAdminResponse> getAllAdmins(Long organizationId, Pageable pageable) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Getting all admins of organization with ID {} started by user with ID {}", organizationId, authenticatedUser.getId());
        if (!isAdmin(authenticatedUser, organizationId)) {
            throw new UnauthorizedException("USER_NOT_ADMIN_OF_ORGANIZATION");
        }
        Page<OrganizationAdmin> admins = organizationAdminRepository.findAllByOrganizationId(organizationId, pageable);

        Page<OrganizationAdminResponse> adminResponses = admins.map(organizationAdminMapper::toDto);

        log.info("All admins of organization with ID {} returned successfully", organizationId);

        return adminResponses;

    }

    @Override
    @Transactional
    public OrganizationAdminResponse addAdmin(Long organizationId, Long userId, OrganizationRole role) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Adding new admin for organization with ID {} by user with ID {} started.", organizationId, authenticatedUser.getId());
        User newAdmin = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(User.class));
        Organization organization = organizationRepository.findByIdAndStatus(organizationId,EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        validateAdminPermission(organizationId, authenticatedUser, OrganizationPermission.MANAGE_ROLES);
        OrganizationAdmin newOrganizationAdmin = OrganizationAdmin.builder()
                .admin(newAdmin)
                .organization(organization)
                .role(role)
                .build();
        organizationAdminRepository.save(newOrganizationAdmin);
        OrganizationAdminResponse response = organizationAdminMapper.toDto(newOrganizationAdmin);
        log.info("New admin added successfully to organization with ID {}", organizationId);

        return response;
    }

    @Override
    @Transactional
    public OrganizationAdminResponse changeRole(Long organizationId, Long organizationAdminId, OrganizationRole organizationRole) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Changing admin role {} in organization {} by user {} started.", organizationAdminId, organizationId, authenticatedUser.getId());
        Organization organization = organizationRepository.findByIdAndStatus(organizationId,EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        OrganizationAdmin organizationAdminToChange = organizationAdminRepository.findByIdAndOrganizationId(organizationAdminId, organizationId).orElseThrow(() -> new ResourceNotFoundException(OrganizationAdmin.class));
        validateAdminPermission(organizationId, authenticatedUser, OrganizationPermission.MANAGE_ROLES);
        if (organizationAdminToChange.getAdmin().getId().equals(organization.getCreatedBy().getId())) {
            log.error("Failed to change role: Organization creator's role can't be changed.");
            throw new ForbiddenException("ROLE_CAN'T_BE_CHANGED");
        }
        organizationAdminToChange.setRole(organizationRole);
        log.info("Successfully changed the role of admin {} in organization {}.", organizationAdminId, organizationId);
        return organizationAdminMapper.toDto(organizationAdminToChange);
    }


    @Override
    @Transactional
    public void deleteAdmin(Long organizationAdminId, Long organizationId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Deleting admin {} of organization {} started by user {}", organizationAdminId, organizationId, authenticatedUser.getId());

        Organization organization = organizationRepository.findByIdAndStatus(organizationId,EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));


        OrganizationAdmin organizationAdminToDelete = organizationAdminRepository.findById(organizationAdminId)
                .orElseThrow(() -> new ResourceNotFoundException(OrganizationAdmin.class));

        if (!organizationAdminToDelete.getOrganization().getId().equals(organizationId)) {
            throw new ForbiddenException("Admin does not belong to this organization.");
        }

        validateAdminPermission(organizationId, authenticatedUser, OrganizationPermission.MANAGE_ROLES);

        if (organizationAdminToDelete.getAdmin().getId().equals(organization.getCreatedBy().getId())) {
            log.error("Failed to delete admin: Organization creator can't be deleted.");
            throw new ForbiddenException("CANT_BE_DELETED");
        }

        long adminCount = organizationAdminRepository.countByOrganizationId(organizationId);
        if (adminCount <= 1) {
            throw new ForbiddenException("Cannot delete the last admin of the organization.");
        }

        organizationAdminRepository.delete(organizationAdminToDelete);
        log.info("Successfully deleted admin {} from organization {}.", organizationAdminId, organizationId);
    }

    public void validateAdminPermission(Long organizationId, User authenticatedUser, OrganizationPermission requiredPermission) {
        OrganizationAdmin organizationAdmin = organizationAdminRepository.findByAdminIdAndOrganizationId(authenticatedUser.getId(), organizationId)
                .orElseThrow(() -> {
                    log.error("User with ID {} is not an admin of organization with ID {}", authenticatedUser.getId(), organizationId);
                    return new ForbiddenException("User is not an admin of this organization.");
                });

        if (!organizationAdmin.getRole().hasPermission(requiredPermission)) {
            log.error("User with ID {} does not have permission to {}", authenticatedUser.getId(), requiredPermission.name());
            throw new ForbiddenException("User does not have permission to " + requiredPermission.name());
        }
    }
}




