package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;

import az.matrix.linkedinclone.enums.OrganizationPermission;
import az.matrix.linkedinclone.enums.OrganizationRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrganizationService {
    OrganizationResponse createOrganization(OrganizationRequest organizationRequest);

    OrganizationResponse editOrganization(Long id, OrganizationRequest organizationRequest);

    OrganizationResponse getOrganization(Long id);

    void deactivateOrganization(Long id);

    void addAdmin(Long id, Long userId, OrganizationRole role);

    void changeAdminRole(Long id, Long userId, OrganizationRole organizationRole);

    void deleteAdmin(Long id, Long userId);

    Page<OrganizationAdminResponse> getAllAdmins(Long organizationId, Pageable pageable);
}
