package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import az.matrix.linkedinclone.enums.OrganizationRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface OrganizationAdminService {
    boolean isAdmin(User user, Organization organization);

    OrganizationAdminResponse addAdmin(Long id, Long userId, OrganizationRole role);

    OrganizationAdminResponse changeAdminRole(Long id, Long userId, OrganizationRole organizationRole);

    void deleteAdmin(Long id, Long userId);

    Page<OrganizationAdminResponse> getAllAdmins(Long organizationId, Pageable pageable);
}
