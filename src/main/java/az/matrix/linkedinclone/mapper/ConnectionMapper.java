package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Connection;
import az.matrix.linkedinclone.dto.response.ConnectionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConnectionMapper {
    ConnectionResponse toDto(Connection connection);
}
