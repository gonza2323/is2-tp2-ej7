package ar.edu.uncuyo.ej2b.service;

import ar.edu.uncuyo.ej2b.dto.DomicilioDto;
import ar.edu.uncuyo.ej2b.dto.LocalidadDto;
import ar.edu.uncuyo.ej2b.dto.PersonaDto;
import ar.edu.uncuyo.ej2b.entity.Domicilio;
import ar.edu.uncuyo.ej2b.entity.Localidad;
import ar.edu.uncuyo.ej2b.entity.Persona;
import ar.edu.uncuyo.ej2b.error.BusinessException;
import ar.edu.uncuyo.ej2b.mapper.PersonaMapper;
import ar.edu.uncuyo.ej2b.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaService {
    private final PersonaMapper personaMapper;
    private final PersonaRepository personaRepository;
    private final LibroService libroService;
    private final DomicilioService domicilioService;
    private final LocalidadService localidadService;

    @Transactional(readOnly = true)
    public Persona buscarPersona(Long id) {
        return personaRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("La persona no existe"));
    }

    @Transactional(readOnly = true)
    public PersonaDto buscarPersonaDto(Long id) {
        Persona persona = buscarPersona(id);
        return personaMapper.toDto(persona);
    }

    @Transactional(readOnly = true)
    public Page<PersonaDto> listarPersonasDtos(Pageable pageable) {
        Page<Persona> personas = personaRepository.findAllByEliminadoFalse(pageable);
        return personas.map(personaMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<PersonaDto> listarPersonasDtosTodas(Sort sort) {
        List<Persona> personas = personaRepository.findAllByEliminadoFalse(sort);
        return personaMapper.toDtos(personas);
    }

    @Transactional
    public Persona crearPersona(PersonaDto personaDto) {
        if (personaRepository.existsByDniAndEliminadoFalse(personaDto.getDni()))
            throw new BusinessException("Ya existe una persona con ese DNI");

        Persona persona = personaMapper.toEntity(personaDto);
        persona.setId(null);

        Domicilio domicilio = domicilioService.crearDomicilio(personaDto.getDomicilio());
        if (personaDto.getDomicilio() != null && personaDto.getDomicilio().getLocalidadId() != null) {
            Long localidadId = personaDto.getDomicilio().getLocalidadId();
            Localidad localidad = localidadService.buscarLocalidad(localidadId);
            domicilio.setLocalidad(localidad);
        }
        persona.setDomicilio(domicilio);

        return personaRepository.save(persona);
    }

    @Transactional
    public Persona modificarPersona(PersonaDto personaDto) {
        if (personaRepository.existsByDniAndIdNotAndEliminadoFalse(personaDto.getDni(), personaDto.getId()))
            throw new BusinessException("Ya existe una persona con ese DNI");

        Persona persona = buscarPersona(personaDto.getId());
        personaMapper.updateEntityFromDto(personaDto, persona);

        DomicilioDto domicilioDto = personaDto.getDomicilio();
        domicilioDto.setId(persona.getDomicilio().getId());
        domicilioService.modificarDomicilio(domicilioDto);

        return personaRepository.save(persona);
    }

    @Transactional
    public void eliminarPersona(Long id) {
        Persona persona = buscarPersona(id);
        persona.setEliminado(true);
        libroService.eliminarLibrosDePersona(persona);
        personaRepository.save(persona);
    }
}
