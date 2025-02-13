package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    List<User> findAllByStatus(EntityStatus status);

    Optional<User> findByIdAndStatus(Long id, EntityStatus status);

    Optional<User> findByEmailAndStatus(String email, EntityStatus entityStatus);
}
