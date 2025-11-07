package ar.edu.uncuyo.ej2b.mapper;

import ar.edu.uncuyo.ej2b.dto.PersonaDto;
import ar.edu.uncuyo.ej2b.entity.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring" , uses = { DomicilioMapper.class })
public interface PersonaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "domicilio", ignore = true)
    Persona toEntity(PersonaDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado",  ignore = true)
    @Mapping(target = "domicilio", ignore = true)
    void updateEntityFromDto(PersonaDto dto, @MappingTarget Persona persona);

    PersonaDto toDto(Persona persona);

    List<PersonaDto> toDtos(List<Persona> personas);
}
