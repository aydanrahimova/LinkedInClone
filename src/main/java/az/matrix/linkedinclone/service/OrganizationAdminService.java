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

    boolean isAdmin(User user, Long organizationId);

    OrganizationAdminResponse addAdmin(Long organizationId, Long userId, OrganizationRole role);

    OrganizationAdminResponse changeRole(Long organizationId, Long id, OrganizationRole organizationRole);

    void deleteAdmin(Long id, Long organizationId);

    Page<OrganizationAdminResponse> getAllAdmins(Long organizationId, Pageable pageable);
}
