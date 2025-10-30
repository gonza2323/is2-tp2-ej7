package ar.edu.uncuyo.ej2b.mapper;

import ar.edu.uncuyo.ej2b.dto.libro.LibroCreateDto;
import ar.edu.uncuyo.ej2b.dto.libro.LibroSummaryDto;
import ar.edu.uncuyo.ej2b.entity.Libro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LibroMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "autores", ignore = true)
    Libro toEntity(LibroCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "autores", ignore = true)
    void updateEntityFromDto(LibroCreateDto dto, @MappingTarget Libro libro);

    @Mapping(target = "autoresIds", ignore = true)
    LibroCreateDto toDto(Libro libro);

    @Mapping(target = "personaId", source = "persona.id")
    LibroSummaryDto toSummaryDto(Libro libro);

    List<LibroCreateDto> toDtos(List<Libro> libros);
}
