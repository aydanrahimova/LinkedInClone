package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Education;
import az.matrix.linkedinclone.dto.request.EducationRequest;
import az.matrix.linkedinclone.dto.response.EducationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring"/*,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE*/)
public interface EducationMapper {
    Education toEntity(EducationRequest dto);
    @Mapping(target = "schoolId",source = "school.id")
    @Mapping(target = "schoolName",source = "school.name")
    EducationResponse toDto(Education entity);
    void mapForUpdate(@MappingTarget Education education,EducationRequest educationDto);
}
