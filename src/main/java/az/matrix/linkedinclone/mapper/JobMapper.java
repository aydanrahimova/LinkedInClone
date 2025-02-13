package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dto.request.JobRequest;
import az.matrix.linkedinclone.dto.response.JobResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JobMapper {
    Job toEntity(JobRequest jobRequest);
    @Mapping(target = "organizationName",source = "organization.name")
    JobResponse toDto(Job job);

    void mapForUpdate(@MappingTarget Job job, JobRequest jobRequest);
}
