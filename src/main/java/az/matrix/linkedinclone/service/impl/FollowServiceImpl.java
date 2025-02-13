package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Follow;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.FollowRepository;
import az.matrix.linkedinclone.dao.repo.OrganizationRepository;
import az.matrix.linkedinclone.dto.response.UserResponse;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.AlreadyExistException;
import az.matrix.linkedinclone.exception.ForbiddenException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.UserMapper;
import az.matrix.linkedinclone.service.FollowService;
import az.matrix.linkedinclone.service.OrganizationAdminService;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final AuthHelper authHelper;
    private final OrganizationRepository organizationRepository;
    private final FollowRepository followRepository;
    private final OrganizationAdminService organizationAdminService;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponse> getFollowers(Long organizationId, Pageable pageable) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("User with ID {} attempting to get followers of organization with ID {}", authenticatedUser.getId(), organizationId);
        Organization organization = organizationRepository.findByIdAndStatus(organizationId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        if (!organizationAdminService.isAdmin(authenticatedUser, organizationId)) {
            throw new ForbiddenException("NOT_ALLOWED");
        }
        Page<Follow> follows = followRepository.findAllByOrganization(organization, pageable);
        List<UserResponse> userResponses = follows.stream()
                .map(Follow::getUser)
                .filter(user -> user.getStatus().equals(EntityStatus.ACTIVE))
                .map(userMapper::toDto)
                .toList();
        return new PageImpl<>(userResponses, pageable, userResponses.size());
    }

    @Override
    @Transactional
    public void follow(Long organizationId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("User with ID {} attempting to follow organization with ID {}", authenticatedUser.getId(), organizationId);
        Organization organization = organizationRepository.findByIdAndStatus(organizationId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));

        if (followRepository.existsByUserAndOrganization(authenticatedUser, organization)) {
            throw new AlreadyExistException(Follow.class);
        }

        Follow follow = Follow.builder().user(authenticatedUser).organization(organization).build();
        followRepository.save(follow);
        log.info("User with ID {} started following organization with ID {}", authenticatedUser.getId(), organizationId);
    }

    @Override
    public void unfollow(Long organizationId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("User with ID {} attempting to unfollow organization with ID {}", authenticatedUser.getId(), organizationId);
        Organization organization = organizationRepository.findByIdAndStatus(organizationId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        Follow follow = followRepository.findByUserAndOrganization(authenticatedUser, organization).orElseThrow(() -> new ResourceNotFoundException(Follow.class));
        followRepository.delete(follow);
        log.info("User with ID {} unfollowed organization with ID {}", authenticatedUser.getId(), organizationId);
    }
}
