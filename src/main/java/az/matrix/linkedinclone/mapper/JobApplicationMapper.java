package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.JobApplication;
import az.matrix.linkedinclone.dto.response.JobApplicationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {
    JobApplicationResponse toDto(JobApplication jobApplication);
}
