package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.OrganizationAdmin;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.enums.OrganizationRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationAdminRepo extends JpaRepository<OrganizationAdmin, Long> {
    Optional<OrganizationAdmin> findByAdminAndOrganizationId(User admin, Long organizationId);

    Optional<OrganizationAdmin> findByAdminAndOrganization(User admin, Organization organization);

    boolean existsByOrganizationIdAndRole(Long organizationId, OrganizationRole organizationRole);

    Page<OrganizationAdmin> findAllByOrganizationId(Long organizationId, Pageable pageable);

}
