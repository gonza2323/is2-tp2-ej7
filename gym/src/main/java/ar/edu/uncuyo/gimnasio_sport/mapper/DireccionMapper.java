package ar.edu.uncuyo.gimnasio_sport.mapper;

import ar.edu.uncuyo.gimnasio_sport.dto.DireccionDto;
import ar.edu.uncuyo.gimnasio_sport.entity.Direccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DireccionMapper {

    @Mapping(target = "localidad", ignore = true)
    Direccion toEntity(DireccionDto dto);

    @Mapping(target = "localidadId", source = "localidad.id")
    @Mapping(target = "departamentoId", source = "localidad.departamento.id")
    @Mapping(target = "provinciaId", source = "localidad.departamento.provincia.id")
    @Mapping(target = "paisId", source = "localidad.departamento.provincia.pais.id")
    DireccionDto toDto(Direccion direccion);

    List<DireccionDto> toDtos(List<Direccion> direccion);

    @Mapping(target = "localidad", ignore = true)
    void updateEntityFromDto(DireccionDto dto, @MappingTarget Direccion direccion);
}
