package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.response.UserResponse;
import az.matrix.linkedinclone.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @GetMapping
    public Page<UserResponse> getFollowers(@RequestParam Long organizationId, @PageableDefault Pageable pageable) {
        return followService.getFollowers(organizationId, pageable);
    }

    @PostMapping
    public void follow(@RequestParam Long organizationId) {
        followService.follow(organizationId);
    }

    @DeleteMapping
    public void unfollow(@RequestParam Long organizationId) {
        followService.unfollow(organizationId);
    }
}
