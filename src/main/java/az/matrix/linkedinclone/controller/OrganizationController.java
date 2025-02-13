package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;
import az.matrix.linkedinclone.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/{id}")
    public OrganizationResponse getOrganization(@PathVariable Long id) {
        return organizationService.getOrganization(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OrganizationResponse createOrganization(@Valid @ModelAttribute OrganizationRequest organizationRequest, @RequestPart(required = false) MultipartFile logoPhoto) {
        return organizationService.createOrganization(organizationRequest, logoPhoto);
    }

    @PutMapping("/{id}")
    public OrganizationResponse editOrganization(@PathVariable Long id, @Valid @RequestBody OrganizationRequest organizationRequest) {
        return organizationService.editOrganization(id, organizationRequest);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivateOrganization(@PathVariable Long id) {
        organizationService.deactivateOrganization(id);
    }

}
