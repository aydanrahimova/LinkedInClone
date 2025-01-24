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
    boolean existsBySenderAndReceiver(User sender, User receiver);

    Page<Connection> findAllBySenderAndStatus(User user, ConnectionStatus connectionStatus, Pageable pageable);

    Optional<Connection> findBySenderAndIdAndStatus(User user, Long id, ConnectionStatus connectionStatus);

    Page<Connection> findAllBySenderOrReceiverAndStatus(User sender, User receiver, ConnectionStatus connectionStatus, Pageable pageable);

    @Query("SELECT c FROM Connection c WHERE c.sender = :user OR c.receiver = :user")
    Page<Connection> findAllConnectionsByUser(User user, Pageable pageable);

    @Query("SELECT c FROM Connection c WHERE (c.sender = :user1 AND c.receiver = :user2) OR (c.sender = :user2 AND c.receiver = :user)")
    Connection findConnectionBetweenUsers(User user1, User user2);


    @Query("SELECT c FROM Connection c WHERE c.id = :id AND c.status = :status AND (c.sender = :user OR c.receiver = :user)")
    Connection findByIdAndStatusAndUser(Long id, ConnectionStatus status, User user);

    Optional<Connection> findByIdAndReceiverAndStatus(Long id, User user, ConnectionStatus oldStatus);
}
