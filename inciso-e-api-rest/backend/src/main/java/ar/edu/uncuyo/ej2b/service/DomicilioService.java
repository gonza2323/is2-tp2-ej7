package ar.edu.uncuyo.ej2b.service;

import ar.edu.uncuyo.ej2b.dto.DomicilioDto;
import ar.edu.uncuyo.ej2b.entity.Domicilio;
import ar.edu.uncuyo.ej2b.entity.Localidad;
import ar.edu.uncuyo.ej2b.error.BusinessException;
import ar.edu.uncuyo.ej2b.mapper.DomicilioMapper;
import ar.edu.uncuyo.ej2b.repository.DomicilioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DomicilioService {

    private final DomicilioRepository domicilioRepository;
    private final DomicilioMapper domicilioMapper;
    private final LocalidadService localidadService;

    @Transactional(readOnly = true)
    public Domicilio buscarDomicilio(Long id) {
        return domicilioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("El domicilio no existe"));
    }

    @Transactional(readOnly = true)
    public DomicilioDto buscarDomicilioDto(Long id) {
        Domicilio domicilio = buscarDomicilio(id);
        return domicilioMapper.toDto(domicilio);
    }

    @Transactional
    public Domicilio crearDomicilio(DomicilioDto domicilioDto) {
        Localidad localidad = localidadService.buscarLocalidad(domicilioDto.getLocalidadId());

        Domicilio domicilio = domicilioMapper.toEntity(domicilioDto);
        domicilio.setId(null);
        domicilio.setLocalidad(localidad);
        Domicilio domicilioGuardado = domicilioRepository.save(domicilio);
        domicilioGuardado.getLocalidad().getId();
        return domicilioRepository.save(domicilio);
    }

    @Transactional
    public void modificarDomicilio(DomicilioDto domicilioDto) {
        Domicilio domicilio = buscarDomicilio(domicilioDto.getId());

        Localidad localidad = localidadService.buscarLocalidad(domicilioDto.getLocalidadId());

        domicilioMapper.updateEntityFromDto(domicilioDto, domicilio);
        domicilio.setLocalidad(localidad);
        domicilioRepository.save(domicilio);
    }

    @Transactional
    public void eliminarDomicilio(Long id) {
        Domicilio domicilio = buscarDomicilio(id);
        domicilio.setEliminado(true);
        domicilioRepository.save(domicilio);
    }
}
