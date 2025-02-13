package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.OrganizationAdmin;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.OrganizationRepository;
import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;
import az.matrix.linkedinclone.enums.OrganizationPermission;
import az.matrix.linkedinclone.enums.OrganizationRole;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.OrganizationMapper;
import az.matrix.linkedinclone.service.OrganizationService;
import az.matrix.linkedinclone.utility.AuthHelper;
import az.matrix.linkedinclone.utility.MediaUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final OrganizationAdminServiceImpl organizationAdminServiceImpl;
    private final AuthHelper authHelper;

    @Value("${files.directory}")
    private String UPLOAD_DIR;


    @Override
    public OrganizationResponse getOrganization(Long id) {
        log.info("Getting organization with ID {} started.", id);
        Organization organization = organizationRepository.findByIdAndStatus(id, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        log.info("Organization with ID {} successfully returned", id);
        return organizationMapper.toDto(organization);
    }

    @Transactional
    @Override
    public OrganizationResponse createOrganization(OrganizationRequest organizationRequest, MultipartFile logoPhoto) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Creating new organization started by user with ID {}.", user.getId());

        Organization organization = organizationMapper.toEntity(organizationRequest);
        organization.setCreatedBy(user);
        if (logoPhoto != null && !logoPhoto.isEmpty()) {
            String logoPath = MediaUploadUtil.uploadPhoto(logoPhoto, UPLOAD_DIR);
            organization.setLogoPath(logoPath);
        }

        OrganizationAdmin organizationAdmin = OrganizationAdmin.builder()
                .organization(organization)
                .admin(user)
                .role(OrganizationRole.SUPER_ADMIN)
                .build();

        organization.getAdmins().add(organizationAdmin);
        organizationRepository.save(organization);
        OrganizationResponse response = organizationMapper.toDto(organization);
        log.info("New organization successfully created.");
        return response;
    }

    @Transactional
    @Override
    public OrganizationResponse editOrganization(Long id, OrganizationRequest organizationRequest) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Editing organization with ID {} started by user with ID {}", id, authenticatedUser.getId());
        Organization organization = organizationRepository.findByIdAndStatus(id, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        organizationAdminServiceImpl.validateAdminPermission(id, authenticatedUser, OrganizationPermission.EDIT_ORGANIZATION_PROFILE);
        organizationMapper.mapForUpdate(organization, organizationRequest);
        organizationRepository.save(organization);
        OrganizationResponse response = organizationMapper.toDto(organization);
        log.info("Organization with ID {} successfully updated", id);
        return response;
    }

    @Override
    @Transactional
    public void deactivateOrganization(Long id) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Deactivation organization with ID {} by user with ID {} started.", id, authenticatedUser.getId());
        Organization organization = organizationRepository.findByIdAndStatus(id, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        organizationAdminServiceImpl.validateAdminPermission(id, authenticatedUser, OrganizationPermission.EDIT_ORGANIZATION_PROFILE);
        organization.setStatus(EntityStatus.DEACTIVATED);
        log.info("Organization successfully deactivated");
        organizationRepository.save(organization);
    }


}
