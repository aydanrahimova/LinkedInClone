package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;
import az.matrix.linkedinclone.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/{id}")
    public OrganizationResponse getOrganization(@PathVariable Long id) {
        return organizationService.getOrganization(id);
    }

    @PostMapping
    public OrganizationResponse createOrganization(@Validated @RequestBody OrganizationRequest organizationRequest) {
        return organizationService.createOrganization(organizationRequest);
    }

    @PutMapping("/{id}")
    public OrganizationResponse editOrganization(@PathVariable Long id, @Validated @RequestBody OrganizationRequest organizationRequest) {
        return organizationService.editOrganization(id, organizationRequest);
    }

    @PatchMapping("/{id}")
    public void deactivateOrganization(@PathVariable Long id) {
        organizationService.deactivateOrganization(id);
    }

}
