package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dto.request.JobRequest;
import az.matrix.linkedinclone.dto.response.JobResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {
    Job toEntity(JobRequest jobRequest);
    JobResponse toDto(Job job);
}
