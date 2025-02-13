package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Project;
import az.matrix.linkedinclone.dto.request.ProjectRequest;
import az.matrix.linkedinclone.dto.response.ProjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring"/*,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE*/)
public interface ProjectMapper {
    Project toEntity(ProjectRequest dto);
    ProjectResponse toDto(Project project);
    void mapToUpdate(@MappingTarget Project project,ProjectRequest projectDto);
}
