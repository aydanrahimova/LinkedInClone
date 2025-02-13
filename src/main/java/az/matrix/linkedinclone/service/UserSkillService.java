package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.SkillUserRequest;
import az.matrix.linkedinclone.dto.response.SkillUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserSkillService {
    SkillUserResponse addSkillToUser(SkillUserRequest skillUserRequest);

    Page<SkillUserResponse> getSkillsByUserId(Long userId, Pageable pageable);

    SkillUserResponse editSkillOfUser(Long id,SkillUserRequest skillUserRequest);

    void deleteSkillOfUser(Long id);
}
