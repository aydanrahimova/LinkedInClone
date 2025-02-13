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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
            return connections.map(connection -> {
                User user = connection.getSender().getId().equals(userId) ? connection.getReceiver() : connection.getSender();
                return userMapper.toDto(user);
            });
        } else {
            Page<User> connectedUsers = connectionRepository.findMutualConnectionBetweenUsers(authenticatedUser.getId(), userId, ConnectionStatus.ACCEPTED, pageable);
            return connectedUsers.map(userMapper::toDto);
        }
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
        return connectionMapper.toDto(connection);
    }

}
