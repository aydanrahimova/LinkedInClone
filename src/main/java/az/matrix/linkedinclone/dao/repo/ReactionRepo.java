package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.*;
import az.matrix.linkedinclone.enums.ReactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepo extends JpaRepository<Reaction, Long> {

    boolean existsByUserAndTarget(User user, ReactionTarget target);

    Optional<Reaction> findByIdAndUser(Long id, User user);

    Page<Reaction> findAllByTarget(ReactionTarget target, Pageable pageable);

    Page<Reaction> findAllByTargetAndReactionType(ReactionTarget target, ReactionType reactionType, Pageable pageable);
}
