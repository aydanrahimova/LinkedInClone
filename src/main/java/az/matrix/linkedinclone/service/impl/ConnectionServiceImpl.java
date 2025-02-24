package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Connection;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.ConnectionRepository;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.dto.response.ConnectionResponse;
import az.matrix.linkedinclone.dto.response.UserResponse;
import az.matrix.linkedinclone.enums.ConnectionStatus;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.AlreadyExistException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.ConnectionMapper;
import az.matrix.linkedinclone.mapper.UserMapper;
import az.matrix.linkedinclone.service.ConnectionService;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {
    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final ConnectionMapper connectionMapper;
    private final AuthHelper authHelper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public ConnectionResponse sendConnectionRequest(Long receiverId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of sending connection request to user with ID {} started by user {}", receiverId, authenticatedUser.getId());
        User receiver = userRepository.findByIdAndStatus(receiverId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(User.class));

        Connection existingConnection = connectionRepository.findConnectionBetweenUsers(authenticatedUser, receiver);
        if (existingConnection != null) {
            if (existingConnection.getStatus() == ConnectionStatus.REJECTED || existingConnection.getStatus() == ConnectionStatus.DELETED) {
                existingConnection.setStatus(ConnectionStatus.PENDING);
                connectionRepository.save(existingConnection);
            } else throw new AlreadyExistException(Connection.class);
        }

        Connection connection = Connection.builder()
                .sender(authenticatedUser)
                .receiver(receiver)
                .status(ConnectionStatus.PENDING)
                .build();
        connectionRepository.save(connection);

        log.info("Connection request is successfully sent to user {}", receiver.getEmail());

        ConnectionResponse response = connectionMapper.toDto(connection);
        response.setConnectedUser(userMapper.toDto(receiver));
        log.info("Notification is sent to user {} to his/her email", receiver.getEmail());

        return response;
    }

    @Override
    public Page<UserResponse> getConnectionsOfUser(Long userId, Pageable pageable) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of getting connections of user with ID {} started by user with ID {}", userId, authenticatedUser.getId());

        if (authenticatedUser.getId().equals(userId)) {
            Page<Connection> connections = connectionRepository.findAllConnectionsByUserAndStatus(userId, ConnectionStatus.ACCEPTED, pageable);

            List<UserResponse> activeUsers = connections.stream()
                    .map(connection -> {
                        User user = connection.getSender().getId().equals(userId) ? connection.getReceiver() : connection.getSender();
                        return user.getStatus().equals(EntityStatus.ACTIVE)
                                ? userMapper.toDto(user)
                                : null;
                    })
                    .filter(Objects::nonNull)
                    .toList();
            log.info("All connection of authenticated user with ID {} returned", authenticatedUser.getId());
            return new PageImpl<>(activeUsers, pageable, activeUsers.size());

        } else {
            Page<User> connectedUsers = connectionRepository.findMutualConnectionBetweenUsers(authenticatedUser.getId(), userId, ConnectionStatus.ACCEPTED, pageable);

            List<UserResponse> activeConnectedUsers = connectedUsers.stream()
                    .filter(user -> user.getStatus().equals(EntityStatus.ACTIVE))
                    .map(userMapper::toDto)
                    .toList();
            log.info("Mutual connections of user with ID {} returned to user with ID {}.", userId, authenticatedUser.getId());
            return new PageImpl<>(activeConnectedUsers, pageable, activeConnectedUsers.size());
        }
    }

    @Override
    public Page<UserResponse> getPendingConnections(Pageable pageable, Boolean sentByMe) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of getting pending connections started by user with ID {}", authenticatedUser.getId());

        Page<Connection> connections = sentByMe
                ? connectionRepository.findAllConnectionsBySenderAndStatus(authenticatedUser, ConnectionStatus.PENDING, pageable)
                : connectionRepository.findAllConnectionsByReceiverAndStatus(authenticatedUser, ConnectionStatus.PENDING, pageable);

        List<UserResponse> activeConnectedUsers = connections.map(connection -> getUserFromConnection(connection, sentByMe)).filter(Objects::nonNull).stream().toList();

        log.info("Pending connections successfully returned.");
        return new PageImpl<>(activeConnectedUsers, pageable, activeConnectedUsers.size());
    }

    @Override
    @Transactional
    public void deleteConnection(Long id) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of deleting connection with ID {} started for user with ID {}", id, authenticatedUser.getId());

        Connection connection = connectionRepository.findByIdAndStatusAndUser(id, ConnectionStatus.ACCEPTED, authenticatedUser);
        if (connection == null) {
            throw new ResourceNotFoundException(Connection.class);
        }
        connection.setStatus(ConnectionStatus.DELETED);
        connectionRepository.save(connection);
        log.info("Connection with ID {} has been deleted by user with ID {}", id, authenticatedUser.getId());
    }

    @Override
    @Transactional
    public ConnectionResponse changeConnectionStatus(Long id, ConnectionStatus newStatus) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of changing status of connection with ID {} started for user with ID {}", id, authenticatedUser.getId());
        Connection connection = connectionRepository.findByIdAndReceiverAndStatus(id, authenticatedUser, ConnectionStatus.PENDING).orElseThrow(() -> new ResourceNotFoundException(Connection.class));
        connection.setStatus(newStatus);
        connection.setResponseTime(LocalDateTime.now());
        connectionRepository.save(connection);
        ConnectionResponse response = connectionMapper.toDto(connection);
        response.setConnectedUser(userMapper.toDto(connection.getSender()));
        return response;
    }

    private UserResponse getUserFromConnection(Connection connection, Boolean sentByMe) {
        User user = sentByMe ? connection.getReceiver() : connection.getSender();
        return user.getStatus().equals(EntityStatus.ACTIVE)
                ? userMapper.toDto(user)
                : null;
    }
}
