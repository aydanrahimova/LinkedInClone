package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface OrganizationService {
    OrganizationResponse createOrganization(OrganizationRequest organizationRequest, MultipartFile logoPhoto);

    OrganizationResponse editOrganization(Long id, OrganizationRequest organizationRequest);

    OrganizationResponse getOrganization(Long id);

    void deactivateOrganization(Long id);


}
