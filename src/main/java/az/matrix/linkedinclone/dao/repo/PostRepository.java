package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Post;
import az.matrix.linkedinclone.enums.AuthorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);

    Page<Post> findAllByAuthorIdAndAuthorType(Long authorId, AuthorType authorType, Pageable pageable);
}
