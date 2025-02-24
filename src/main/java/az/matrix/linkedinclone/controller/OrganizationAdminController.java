package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import az.matrix.linkedinclone.enums.OrganizationRole;
import az.matrix.linkedinclone.service.OrganizationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organization-admins")
@RequiredArgsConstructor
public class OrganizationAdminController {
    private final OrganizationAdminService organizationAdminService;

    @GetMapping
    public ResponseEntity<Page<OrganizationAdminResponse>> getAllAdmins(@RequestParam Long organizationId, @PageableDefault Pageable pageable) {
        Page<OrganizationAdminResponse> admins = organizationAdminService.getAllAdmins(organizationId, pageable);
        return ResponseEntity.ok(admins);
    }

    @PostMapping
    public ResponseEntity<OrganizationAdminResponse> addAdmin(@RequestParam Long organizationId, @RequestParam Long userId, @RequestParam OrganizationRole role) {
        OrganizationAdminResponse adminResponse = organizationAdminService.addAdmin(organizationId, userId, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrganizationAdminResponse> changeRole(@PathVariable Long id, @RequestParam Long organizationId, @RequestParam OrganizationRole organizationRole) {
        OrganizationAdminResponse adminResponse = organizationAdminService.changeRole(organizationId, id, organizationRole);
        return ResponseEntity.ok(adminResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id, @RequestParam Long organizationId) {
        organizationAdminService.deleteAdmin(id, organizationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
