package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    List<User> findAllByStatus(ProfileStatus status);

    Optional<User>  findByIdAndStatus(Long id, ProfileStatus profileStatus);
}
