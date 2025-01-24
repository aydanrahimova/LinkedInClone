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
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.exception.UnauthorizedException;
import az.matrix.linkedinclone.mapper.OrganizationAdminMapper;
import az.matrix.linkedinclone.mapper.OrganizationMapper;
import az.matrix.linkedinclone.service.OrganizationService;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepo organizationRepo;
    private final OrganizationMapper organizationMapper;
    private final UserRepo userRepo;
    private final OrganizationAdminRepo organizationAdminRepo;
    private final OrganizationAdminMapper organizationAdminMapper;
    private final AuthHelper authHelper;

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
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of creating new organization by user with email {} started.", user.getEmail());

        Organization organization = organizationMapper.toEntity(organizationRequest);
        organization.setStatus(EntityStatus.ACTIVE);
        organization.setCreatedBy(user);
        organization.setCreatedAt(LocalDateTime.now());

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
        organization.setStatus(EntityStatus.DEACTIVATED);
        organizationRepo.save(organization);
    }


}
