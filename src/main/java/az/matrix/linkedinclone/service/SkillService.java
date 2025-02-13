package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.SkillRequest;
import az.matrix.linkedinclone.dto.response.SkillResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SkillService {
    SkillResponse addPredefinedSkill(SkillRequest skillRequest);

    Page<SkillResponse> getPredefinedSkills(Pageable pageable);

    SkillResponse getSkill(Long id);

    SkillResponse editPredefinedSkill(Long id, SkillRequest skillRequest);

    void deletePredefinedSkill(Long id);

}
