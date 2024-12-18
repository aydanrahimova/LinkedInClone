package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<Organization,Long> {
}
