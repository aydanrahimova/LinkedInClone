package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;
import az.matrix.linkedinclone.enums.OrganizationRole;
import az.matrix.linkedinclone.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/{id}")
    public OrganizationResponse getOrganization(@PathVariable Long id){
        return organizationService.getOrganization(id);
    }

    @PostMapping
    public OrganizationResponse createOrganization(@Validated @RequestBody OrganizationRequest organizationRequest) {
        return organizationService.createOrganization(organizationRequest);
    }

    @PutMapping("/{id}")
    public OrganizationResponse editOrganization(@PathVariable Long id,@Validated @RequestBody OrganizationRequest organizationRequest){
        return organizationService.editOrganization(id,organizationRequest);
    }

    @PatchMapping("/{id}")
    public void deactivateOrganization(@PathVariable Long id){
        organizationService.deactivateOrganization(id);
    }
//ayri controllere cixarmaliyam???-adminle olan managementleri

    @GetMapping("/{organizationId}/admins")//get all admins
    public Page<OrganizationAdminResponse> getAllAdmins(@PathVariable Long organizationId, @PageableDefault Pageable pageable){
        return organizationService.getAllAdmins(organizationId,pageable);
    }

    @PostMapping("/{id}/add-admin")
    public void addAdmin(@PathVariable Long id, @RequestParam Long userId, @RequestParam OrganizationRole role){
        organizationService.addAdmin(id,userId,role);
    }

    @PatchMapping("/{id}/change-admin-role/{userId}")
    public void changeAdminRole(@PathVariable Long id,@PathVariable Long userId,@RequestParam OrganizationRole organizationRole){
        organizationService.changeAdminRole(id,userId,organizationRole);
    }

    @DeleteMapping("/{id}/delete-admin/{userId}")
    public void deleteAdmin(@PathVariable Long id,@PathVariable Long userId){
        organizationService.deleteAdmin(id,userId);
    }


}
