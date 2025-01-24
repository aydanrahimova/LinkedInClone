package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.ExperienceRequest;
import az.matrix.linkedinclone.dto.response.ExperienceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;


@Service
public interface ExperienceService {
    Page<ExperienceResponse> getExperiencesByUserId(Long userId, @PageableDefault Pageable pageable);

    ExperienceResponse addExperience(ExperienceRequest experienceRequest);

    ExperienceResponse editExperience(Long id, ExperienceRequest experienceRequest);

    void deleteExperience(Long id);
}
