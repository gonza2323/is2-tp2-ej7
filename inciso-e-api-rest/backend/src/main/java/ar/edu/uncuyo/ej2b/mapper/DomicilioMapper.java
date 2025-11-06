package ar.edu.uncuyo.ej2b.mapper;


import ar.edu.uncuyo.ej2b.dto.DomicilioDto;
import ar.edu.uncuyo.ej2b.entity.Domicilio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DomicilioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "localidad", ignore = true)
    Domicilio toEntity(DomicilioDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "localidad", ignore = true)
    void updateEntityFromDto(DomicilioDto dto, @MappingTarget Domicilio domicilio);

    @Mapping(target = "localidadId", source = "localidad.id")
    DomicilioDto toDto(Domicilio domicilio);

    List<DomicilioDto> toDtos(List<Domicilio> domicilios);
}
