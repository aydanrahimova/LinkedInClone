package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.OrganizationAdmin;
import az.matrix.linkedinclone.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationAdminRepository extends JpaRepository<OrganizationAdmin, Long> {
    Page<OrganizationAdmin> findAllByOrganizationId(Long organizationId, Pageable pageable);

    boolean existsByAdminAndOrganizationId(User admin, Long organizationId);

    Optional<OrganizationAdmin> findByAdminIdAndOrganizationId(Long adminId, Long organizationId);
    Optional<OrganizationAdmin> findByIdAndOrganizationId(Long organizationAdminId, Long organizationId);

    long countByOrganizationId(Long organizationId);
}
