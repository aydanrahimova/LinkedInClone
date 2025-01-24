package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;

import az.matrix.linkedinclone.enums.OrganizationRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface OrganizationService {
    OrganizationResponse createOrganization(OrganizationRequest organizationRequest);

    OrganizationResponse editOrganization(Long id, OrganizationRequest organizationRequest);

    OrganizationResponse getOrganization(Long id);

    void deactivateOrganization(Long id);


}
