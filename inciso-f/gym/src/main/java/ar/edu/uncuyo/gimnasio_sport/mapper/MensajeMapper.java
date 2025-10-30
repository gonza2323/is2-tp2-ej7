package ar.edu.uncuyo.gimnasio_sport.mapper;

import ar.edu.uncuyo.gimnasio_sport.dto.MensajeDTO;
import ar.edu.uncuyo.gimnasio_sport.entity.Mensaje;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MensajeMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    Mensaje toEntity(MensajeDTO dto);

    @Mapping(target = "usuarioId", source = "usuario.id")
    MensajeDTO toDto(Mensaje mensaje);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "nombre")
    @Mapping(target = "email")
    @Mapping(target = "asunto")
    @Mapping(target = "cuerpo")
    @Mapping(target = "tipo")
    @Mapping(target = "fechaProgramada")
    void updateEntityFromDto(MensajeDTO dto, @MappingTarget Mensaje mensaje);
}
