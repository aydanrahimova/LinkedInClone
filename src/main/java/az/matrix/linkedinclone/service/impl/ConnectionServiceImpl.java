package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Connection;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.ConnectionRepository;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.response.ConnectionResponse;
import az.matrix.linkedinclone.enums.ConnectionStatus;
import az.matrix.linkedinclone.exception.AlreadyExistException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.ConnectionMapper;
import az.matrix.linkedinclone.service.ConnectionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {
    private final UserRepo userRepo;
    private final ConnectionRepository connectionRepository;
    private final ConnectionMapper connectionMapper;

    @Override
    @Transactional
    public ConnectionResponse sendConnectionRequest(Long receiverId) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of sending connection request to user with ID {} started by user {}", receiverId, authenticatedUserEmail);
        User sender = userRepo.findByEmail(authenticatedUserEmail).orElseThrow(() -> {
            log.warn("Failed to send connection request: Sender {} not found", authenticatedUserEmail);
            return new ResourceNotFoundException("SENDER_NOT_FOUND");
        });
        User receiver = userRepo.findById(receiverId).orElseThrow(() -> {
            log.warn("Failed to send connection request: Receiver with ID {} not found", receiverId);
            return new ResourceNotFoundException("SENDER_NOT_FOUND");
        });

        Connection existingConnection = connectionRepository.findConnectionBetweenUsers(sender, receiver);
        if (existingConnection != null) {
            if (existingConnection.getStatus() == ConnectionStatus.REJECTED || existingConnection.getStatus() == ConnectionStatus.DELETED) {
                existingConnection.setStatus(ConnectionStatus.PENDING);
                //email gonderme-obwiy metoda cixar
                connectionRepository.save(existingConnection);
            } else {
                log.warn("Failed to send connection: Sender {} and receiver {} are already connected or connection are already sent", authenticatedUserEmail, receiver.getEmail());
                throw new AlreadyExistException("CONNECTION_EXISTS");
            }
        }

        Connection connection = Connection.builder()
                .sender(sender)
                .receiver(receiver)
                .status(ConnectionStatus.PENDING)
                .sendTime(LocalDate.now())
                .build();
        connectionRepository.save(connection);

        log.info("Connection request is successfully sent to user {}", receiver.getEmail());
        //email gonderme
//        String subject = "New Connection Request";
//        String body = String.format("You have received a new connection request from %s %s.",
//                sender.getFirstName(), sender.getLastName());
        log.info("Notification is sent to user {} to his/her email", receiver.getEmail());


        return connectionMapper.toDto(connection);
    }

    @Override
    public Page<ConnectionResponse> getConnectionsOfUser(Long userId, Pageable pageable) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of getting all connections starts for user {}", authenticatedUserEmail);
        User user = userRepo.findByEmail(authenticatedUserEmail).orElseThrow();
        Page<ConnectionResponse> connectionResponsePage;
        Page<Connection> connections = connectionRepository.findAllConnectionsByUser(user, pageable);
        connectionResponsePage = connections.map(connectionMapper::toDto);
        log.info("Connections of user {} are returned", authenticatedUserEmail);
        return connectionResponsePage;
    }

    @Override
    @Transactional
    public ConnectionResponse acceptConnectionRequest(Long id) {
        return updateStatus(id, ConnectionStatus.PENDING, ConnectionStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public ConnectionResponse rejectConnectionRequest(Long id) {
        return updateStatus(id, ConnectionStatus.PENDING, ConnectionStatus.REJECTED);
    }
    //bir api ile yaz

    @Override
    @Transactional
    public void deleteConnection(Long id) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of deleting connection with ID {} started for user {}", id, authenticatedUserEmail);

        User user = userRepo.findByEmail(authenticatedUserEmail).orElseThrow();

        Connection connection = connectionRepository
                .findByIdAndStatusAndUser(id, ConnectionStatus.ACCEPTED, user);
        if (connection == null) {
            log.warn("Failed to delete connection with ID {}: Connection not found", id);
            throw new ResourceNotFoundException("CONNECTION_NOT_FOUND");
        }

        connection.setStatus(ConnectionStatus.DELETED);

        connectionRepository.save(connection);
        log.info("Connection with ID {} has been deleted by user {}", id, authenticatedUserEmail);
    }

    private ConnectionResponse updateStatus(Long id, ConnectionStatus oldStatus, ConnectionStatus newStatus) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of changing status of connection with ID {} started for user {}", id, authenticatedUserEmail);
        User user = userRepo.findByEmail(authenticatedUserEmail).orElseThrow();
        Connection connection = connectionRepository
                .findByIdAndReceiverAndStatus(id, user, oldStatus)
                .orElseThrow(() -> {
                    log.warn("Failed to update status from {} to {}: Connection not found", oldStatus, newStatus);
                    return new ResourceNotFoundException("CONNECTION_NOT_FOUND");
                });
        connection.setStatus(newStatus);
        connectionRepository.save(connection);
        return connectionMapper.toDto(connection);
    }
}
