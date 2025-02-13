package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.*;
import az.matrix.linkedinclone.enums.AuthorType;
import az.matrix.linkedinclone.enums.ReactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReactionRepository extends JpaRepository<Reaction, Long> {


    Page<Reaction> findAllByTarget(ReactionTarget target, Pageable pageable);

    Page<Reaction> findAllByTargetAndReactionType(ReactionTarget target, ReactionType reactionType, Pageable pageable);


    boolean existsByTargetAndAuthorIdAndAuthorType(ReactionTarget target, Long authorId, AuthorType authorType);
}
