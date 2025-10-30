package ar.edu.uncuyo.ej2b.service;

import ar.edu.uncuyo.ej2b.dto.LocalidadDto;
import ar.edu.uncuyo.ej2b.entity.Localidad;
import ar.edu.uncuyo.ej2b.error.BusinessException;
import ar.edu.uncuyo.ej2b.mapper.LocalidadMapper;
import ar.edu.uncuyo.ej2b.repository.LocalidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalidadService {
    private final LocalidadRepository localidadRepository;
    private final LocalidadMapper localidadMapper;

    @Transactional(readOnly = true)
    public Localidad buscarLocalidad(Long id) {
        return localidadRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("La localidad no existe"));
    }

    @Transactional(readOnly = true)
    public LocalidadDto buscarLocalidadDto(Long id) {
        Localidad localidad = buscarLocalidad(id);
        return localidadMapper.toDto(localidad);
    }

    @Transactional(readOnly = true)
    public Page<LocalidadDto> listarLocalidadesDtos(Pageable pageable) {
        Page<Localidad> localidades = localidadRepository.findAllByEliminadoFalse(pageable);
        return localidades.map(localidadMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<LocalidadDto> listarLocalidadesDtosTodas(Sort sort) {
        List<Localidad> localidades = localidadRepository.findAllByEliminadoFalse(sort);
        return localidadMapper.toDtos(localidades);
    }

    @Transactional
    public Localidad crearLocalidad(LocalidadDto localidadDto) {
        if (localidadRepository.existsByDenominacionAndEliminadoFalse(localidadDto.getDenominacion()))
            throw new BusinessException("Ya existe una localidad con ese nombre");

        Localidad localidad = localidadMapper.toEntity(localidadDto);
        localidad.setId(null);
        return localidadRepository.save(localidad);
    }

    @Transactional
    public Localidad modificarLocalidad(LocalidadDto localidadDto) {
        Localidad localidad = buscarLocalidad(localidadDto.getId());

        if (localidadRepository.existsByDenominacionAndIdNotAndEliminadoFalse(localidadDto.getDenominacion(), localidadDto.getId()))
            throw new BusinessException("Ya existe una localidad con ese nombre");

        localidadMapper.updateEntityFromDto(localidadDto, localidad);
        return localidadRepository.save(localidad);
    }

    @Transactional
    public void eliminarLocalidad(Long id) {
        Localidad localidad = buscarLocalidad(id);
        localidad.setEliminado(true);
        localidadRepository.save(localidad);
    }
}
