package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<Post> findByIdAndUserEmail(Long postId, String currentUserEmail);
    Optional<Post> findById(Long id);
}
