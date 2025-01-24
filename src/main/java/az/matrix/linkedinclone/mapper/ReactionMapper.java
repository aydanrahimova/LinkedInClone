package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Reaction;
import az.matrix.linkedinclone.dto.response.ReactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {

    ReactionResponse toDto(Reaction reaction);
}
