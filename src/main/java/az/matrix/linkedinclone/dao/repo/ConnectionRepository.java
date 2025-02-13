package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Connection;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.enums.ConnectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    @Query("SELECT c FROM Connection c WHERE (c.sender = :user1 AND c.receiver = :user2) OR (c.sender = :user2 AND c.receiver = :user1)")
    Connection findConnectionBetweenUsers(User user1, User user2);

    @Query("SELECT c FROM Connection c WHERE c.id = :id AND c.status = :status AND (c.sender = :user OR c.receiver = :user)")
    Connection findByIdAndStatusAndUser(Long id, ConnectionStatus status, User user);

    Optional<Connection> findByIdAndReceiverAndStatus(Long id, User user, ConnectionStatus oldStatus);

    @Query("SELECT c FROM Connection c WHERE (c.sender.id = :userId OR c.receiver.id = :userId) AND c.status = :status ")
    Page<Connection> findAllConnectionsByUserAndStatus(Long userId, ConnectionStatus status, Pageable pageable);

    @Query("""
                SELECT u FROM User u
                WHERE u.id IN (
                    SELECT c1.receiver.id FROM Connection c1
                    JOIN Connection c2 ON c1.receiver.id = c2.receiver.id
                    WHERE c1.sender.id = :user1Id AND c2.sender.id = :user2Id
                      AND c1.status = :connectionStatus AND c2.status = :connectionStatus
                   \s
                    UNION\s
                   \s
                    SELECT c1.sender.id FROM Connection c1
                    JOIN Connection c2 ON c1.sender.id = c2.sender.id
                    WHERE c1.receiver.id = :user1Id AND c2.receiver.id = :user2Id
                      AND c1.status = :connectionStatus AND c2.status = :connectionStatus
                )
            """)
    Page<User> findMutualConnectionBetweenUsers(Long user1Id, Long user2Id, ConnectionStatus connectionStatus, Pageable pageable);
}
