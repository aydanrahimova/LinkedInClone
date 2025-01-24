package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dto.request.JobRequest;
import az.matrix.linkedinclone.dto.request.JobUpdateRequest;
import az.matrix.linkedinclone.dto.response.JobResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JobMapper {
    Job toEntity(JobRequest jobRequest);
    JobResponse toDto(Job job);

    void mapForUpdate(@MappingTarget Job job, JobUpdateRequest updateRequest);
}
