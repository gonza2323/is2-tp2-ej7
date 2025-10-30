package ar.edu.uncuyo.gimnasio_sport.mapper;

import ar.edu.uncuyo.gimnasio_sport.dto.PersonaCreateFormDTO;
import ar.edu.uncuyo.gimnasio_sport.entity.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "direccion", ignore = true)
    @Mapping(target = "sucursal", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    Persona toEntity(PersonaCreateFormDTO dto);

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "direccion", ignore = true)
    @Mapping(target = "sucursal", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    Persona updateEntityFromDto(PersonaCreateFormDTO dto, @MappingTarget Persona direccion);
}
