package ar.edu.uncuyo.ej2b.service;

import ar.edu.uncuyo.ej2b.dto.libro.LibroCreateDto;
import ar.edu.uncuyo.ej2b.dto.libro.LibroSummaryDto;
import ar.edu.uncuyo.ej2b.entity.Autor;
import ar.edu.uncuyo.ej2b.entity.Libro;
import ar.edu.uncuyo.ej2b.entity.Persona;
import ar.edu.uncuyo.ej2b.error.BusinessException;
import ar.edu.uncuyo.ej2b.mapper.LibroMapper;
import ar.edu.uncuyo.ej2b.repository.AutorRepository;
import ar.edu.uncuyo.ej2b.repository.LibroRepository;
import ar.edu.uncuyo.ej2b.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibroService {
    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;
    private final AutorService autorService;
    private final FileStorageService fileStorageService;
    private final AutorRepository autorRepository;
    private final PersonaRepository personaRepository;

    @Transactional(readOnly = true)
    public Libro buscarLibro(Long id) {
        return libroRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("El libro no existe"));
    }

    @Transactional(readOnly = true)
    public LibroCreateDto buscarLibroDto(Long id) {
        Libro libro = buscarLibro(id);
        return libroMapper.toDto(libro);
    }

    @Transactional(readOnly = true)
    public Page<LibroSummaryDto> listarLibrosDtos(Pageable pageable) {
        Page<Libro> libros = libroRepository.findAllByEliminadoFalse(pageable);
        return libros.map(libroMapper::toSummaryDto);
    }

    @Transactional
    public Libro crearLibro(LibroCreateDto libroDto) {
        Libro libro = libroMapper.toEntity(libroDto);
        libro.setAutores(new ArrayList<>());
        libro.setId(null);

        Persona persona = personaRepository.findByIdAndEliminadoFalse(libroDto.getPersonaId())
                        .orElseThrow();
        libro.setPersona(persona);

        for (Long autorId : libroDto.getAutoresIds()) {
            Autor autor = autorService.buscarAutor(autorId);
            libro.getAutores().add(autor);
        }

        return libroRepository.save(libro);
    }

    @Transactional
    public Libro modificarLibro(LibroCreateDto libroDto) {
        Libro libro = buscarLibro(libroDto.getId());

        libroMapper.updateEntityFromDto(libroDto, libro);

        libro.getAutores().clear();
        for (Long autorId : libroDto.getAutoresIds()) {
            Autor autor = autorService.buscarAutor(autorId);
            libro.getAutores().add(autor);
        }

        return libroRepository.save(libro);
    }

    @Transactional
    public void eliminarLibro(Long id) {
        Libro libro = buscarLibro(id);
        libro.setEliminado(true);
        libroRepository.save(libro);
    }

    @Transactional
    public void eliminarLibrosDePersona(Persona persona) {
        List<Libro> libros = libroRepository.findAllByPersonaAndEliminadoFalse(persona);
        for (Libro libro : libros)
            libro.setEliminado(true);
        libroRepository.saveAll(libros);
    }

    @Transactional
    public Libro crearLibroConPdf(LibroCreateDto dto, MultipartFile file) {
        Libro libro = libroMapper.toEntity(dto);
        libro.setId(null);

        if (dto.getAutoresIds() != null && !dto.getAutoresIds().isEmpty()) {
            List<Autor> autores = autorRepository.findAllById(dto.getAutoresIds());
            libro.setAutores(autores);
        }

        if (file != null && !file.isEmpty()) {
            String path = fileStorageService.saveFile(file, dto.getTitulo());
            libro.setPdfPath(path);
        }

        return libroRepository.save(libro);
    }
}
