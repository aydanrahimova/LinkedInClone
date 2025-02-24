package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;
import az.matrix.linkedinclone.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getOrganization(@PathVariable Long id) {
        OrganizationResponse organizationResponse = organizationService.getOrganization(id);
        return ResponseEntity.ok(organizationResponse);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrganizationResponse> createOrganization(@Valid @ModelAttribute OrganizationRequest organizationRequest, @RequestPart(required = false) MultipartFile logoPhoto) {
        OrganizationResponse organizationResponse = organizationService.createOrganization(organizationRequest, logoPhoto);
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> editOrganization(@PathVariable Long id, @Valid @RequestBody OrganizationRequest organizationRequest) {
        OrganizationResponse organizationResponse = organizationService.editOrganization(id, organizationRequest);
        return ResponseEntity.ok(organizationResponse);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateOrganization(@PathVariable Long id) {
        organizationService.deactivateOrganization(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
