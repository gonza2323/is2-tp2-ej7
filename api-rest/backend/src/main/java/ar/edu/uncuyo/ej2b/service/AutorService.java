package ar.edu.uncuyo.ej2b.service;

import ar.edu.uncuyo.ej2b.dto.AutorDto;
import ar.edu.uncuyo.ej2b.dto.LocalidadDto;
import ar.edu.uncuyo.ej2b.entity.Autor;
import ar.edu.uncuyo.ej2b.entity.Localidad;
import ar.edu.uncuyo.ej2b.error.BusinessException;
import ar.edu.uncuyo.ej2b.mapper.AutorMapper;
import ar.edu.uncuyo.ej2b.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {
    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    @Transactional(readOnly = true)
    public Autor buscarAutor(Long id) {
        return autorRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("El autor no existe"));
    }

    @Transactional(readOnly = true)
    public AutorDto buscarAutorDto(Long id) {
        Autor autor = buscarAutor(id);
        return autorMapper.toDto(autor);
    }

    @Transactional(readOnly = true)
    public Page<AutorDto> listarAutoresDtos(Pageable pageable) {
        Page<Autor> autores = autorRepository.findAllByEliminadoFalse(pageable);
        return autores.map(autorMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<AutorDto> listarAutoresDtosTodos(Sort sort) {
        List<Autor> autores = autorRepository.findAllByEliminadoFalse(sort);
        return autorMapper.toDtos(autores);
    }

    @Transactional
    public Autor crearAutor(AutorDto autorDto) {
        Autor autor = autorMapper.toEntity(autorDto);
        autor.setId(null);
        return autorRepository.save(autor);
    }

    @Transactional
    public Autor modificarAutor(AutorDto autorDto) {
        Autor autor = buscarAutor(autorDto.getId());

        autorMapper.updateEntityFromDto(autorDto, autor);
        return autorRepository.save(autor);
    }

    @Transactional
    public void eliminarAutor(Long id) {
        Autor autor = buscarAutor(id);
        autor.setEliminado(true);
        autorRepository.save(autor);
    }
}
