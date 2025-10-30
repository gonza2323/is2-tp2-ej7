package ar.edu.uncuyo.ej2b.mapper;

import ar.edu.uncuyo.ej2b.dto.LocalidadDto;
import ar.edu.uncuyo.ej2b.entity.Localidad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocalidadMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    Localidad toEntity(LocalidadDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    void updateEntityFromDto(LocalidadDto dto, @MappingTarget Localidad localidad);

    LocalidadDto toDto(Localidad localidad);

    List<LocalidadDto> toDtos(List<Localidad> localidades);
}
