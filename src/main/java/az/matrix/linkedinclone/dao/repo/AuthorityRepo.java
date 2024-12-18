package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<Authority,Long> {
    Authority findByName(String name);
}
