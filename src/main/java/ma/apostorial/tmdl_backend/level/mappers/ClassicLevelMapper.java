package ma.apostorial.tmdl_backend.level.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ma.apostorial.tmdl_backend.level.dtos.ClassicLevelCreationDTO;
import ma.apostorial.tmdl_backend.level.dtos.ClassicLevelUpdateDTO;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;

@Mapper(componentModel = "spring")
public interface ClassicLevelMapper {
    ClassicLevelMapper INSTANCE = Mappers.getMapper(ClassicLevelMapper.class);

    ClassicLevel fromClassicLevelCreationDTOToEntity(ClassicLevelCreationDTO dto);
    ClassicLevel fromClassicLevelUpdateDTOToEntity(ClassicLevelUpdateDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateFromDTO(ClassicLevelUpdateDTO dto, @MappingTarget ClassicLevel entity);
}
