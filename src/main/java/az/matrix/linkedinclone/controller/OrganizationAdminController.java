package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import az.matrix.linkedinclone.enums.OrganizationRole;
import az.matrix.linkedinclone.service.OrganizationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organization-admins")
@RequiredArgsConstructor
public class OrganizationAdminController {
    private final OrganizationAdminService organizationAdminService;

    @GetMapping
    public Page<OrganizationAdminResponse> getAllAdmins(@RequestParam Long organizationId, @PageableDefault Pageable pageable) {
        return organizationAdminService.getAllAdmins(organizationId, pageable);
    }

    @PostMapping
    public OrganizationAdminResponse addAdmin(@RequestParam Long organizationId, @RequestParam Long userId, @RequestParam OrganizationRole role) {
        return organizationAdminService.addAdmin(organizationId, userId, role);
    }

    @PatchMapping("/{userId}")
    public OrganizationAdminResponse changeAdminRole(@RequestParam Long organizationId, @PathVariable Long userId, @RequestParam OrganizationRole organizationRole) {
        return organizationAdminService.changeAdminRole(organizationId, userId, organizationRole);
    }

    @DeleteMapping
    public void deleteAdmin(@RequestParam Long id, @RequestParam Long userId) {
        organizationAdminService.deleteAdmin(id, userId);
    }

}
