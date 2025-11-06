package ar.edu.uncuyo.ej2b.mapper;

import ar.edu.uncuyo.ej2b.dto.AutorDto;
import ar.edu.uncuyo.ej2b.entity.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    Autor toEntity(AutorDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    void updateEntityFromDto(AutorDto dto, @MappingTarget Autor autor);

    AutorDto toDto(Autor autor);

    List<AutorDto> toDtos(List<Autor> autores);
}
