package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface FollowService {
    Page<UserResponse> getFollowers(Long organizationId, Pageable pageable);

    void follow(Long organizationId);

    void unfollow(Long id);
}
