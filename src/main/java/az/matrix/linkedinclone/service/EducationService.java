package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.EducationRequest;
import az.matrix.linkedinclone.dto.response.EducationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EducationService {
    Page<EducationResponse> getEducationsByUserId(Long userId, Pageable pageable);

    EducationResponse addEducation(EducationRequest educationRequest);

    EducationResponse editEducation(Long eductionId, EducationRequest educationRequest);

    void deleteEducation(java.lang.Long educationId);
}
