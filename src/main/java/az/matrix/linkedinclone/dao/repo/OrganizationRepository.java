package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.enums.OrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByIdAndOrganizationType(Long id, OrganizationType organizationType);

    Optional<Organization> findByIdAndStatus(Long id, EntityStatus entityStatus);

    List<Organization> findAllByStatus(EntityStatus entityStatus);
}
