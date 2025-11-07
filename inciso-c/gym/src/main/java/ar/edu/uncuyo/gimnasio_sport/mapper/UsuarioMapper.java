package ar.edu.uncuyo.gimnasio_sport.mapper;

import ar.edu.uncuyo.gimnasio_sport.dto.UsuarioCreateFormDTO;
import ar.edu.uncuyo.gimnasio_sport.dto.UsuarioDetailDto;
import ar.edu.uncuyo.gimnasio_sport.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "providerUserId", ignore = true)
    Usuario toEntity(UsuarioCreateFormDTO dto);

    UsuarioDetailDto toDto(Usuario entity);
}
