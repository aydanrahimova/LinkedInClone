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

    @PatchMapping("/{id}")
    public OrganizationAdminResponse changeRole(@PathVariable Long id, @RequestParam Long organizationId, @RequestParam OrganizationRole organizationRole) {
        return organizationAdminService.changeRole(organizationId, id, organizationRole);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id, @RequestParam Long organizationId) {
        organizationAdminService.deleteAdmin(id, organizationId);
    }

}
