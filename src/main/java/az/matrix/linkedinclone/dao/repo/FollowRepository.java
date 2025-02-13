package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Follow;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByUserAndOrganization(User authenticatedUser, Organization organization);

    Optional<Follow> findByUserAndOrganization(User authenticatedUser, Organization organization);

    Page<Follow> findAllByOrganization(Organization organization, Pageable pageable);
}
