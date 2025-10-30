package ar.edu.uncuyo.ej2b.service;

import ar.edu.uncuyo.ej2b.dto.LocalidadDto;
import ar.edu.uncuyo.ej2b.entity.Localidad;
import ar.edu.uncuyo.ej2b.error.BusinessException;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class LocalidadServiceIntegrationTest {

    @Autowired
    private LocalidadService localidadService;

    @Test
    void crearYBuscarLocalidad() {
        LocalidadDto dto = LocalidadDto.builder().denominacion("Localidad").build();

        Localidad created = localidadService.crearLocalidad(dto);
        Localidad fetched = localidadService.buscarLocalidad(created.getId());

        assertThat(fetched.getDenominacion()).isEqualTo("Localidad");
    }

    @Test
    void crearLocalidadNombreYaExistente_debeLanzarBusinessException() {
        LocalidadDto dto = LocalidadDto.builder().denominacion("Localidad").build();

        localidadService.crearLocalidad(dto);
        BusinessException ex = assertThrows(BusinessException.class, () -> {localidadService.crearLocalidad(dto);});
        assertThat(ex.getMessage().toLowerCase()).contains("ya existe");
    }

    @Test
    void modificarYBuscarLocalidad() {
        LocalidadDto dto1 = LocalidadDto.builder().denominacion("Localidad").build();
        Localidad created = localidadService.crearLocalidad(dto1);

        LocalidadDto dto2 = LocalidadDto.builder().id(created.getId()).denominacion("Localidad 2").build();
        localidadService.modificarLocalidad(dto2);

        Localidad fetched = localidadService.buscarLocalidad(created.getId());

        assertThat(fetched.getDenominacion()).isEqualTo("Localidad 2");
    }

    @Test
    void modificarSinCambiosYBuscarLocalidad() {
        LocalidadDto dto1 = LocalidadDto.builder().denominacion("Localidad").build();
        Localidad created = localidadService.crearLocalidad(dto1);

        LocalidadDto dto2 = LocalidadDto.builder().id(created.getId()).denominacion("Localidad").build();
        localidadService.modificarLocalidad(dto2);

        Localidad fetched = localidadService.buscarLocalidad(created.getId());

        assertThat(fetched.getDenominacion()).isEqualTo("Localidad");
    }

    @Test
    void modificarNombreYaExiste_lanzaBusinessException() {
        LocalidadDto dto1 = LocalidadDto.builder().denominacion("Localidad 1").build();
        LocalidadDto dto2 = LocalidadDto.builder().denominacion("Localidad 2").build();

        Localidad created1 = localidadService.crearLocalidad(dto1);
        Localidad created2 = localidadService.crearLocalidad(dto2);

        LocalidadDto modified = LocalidadDto.builder().id(created1.getId()).denominacion("Localidad 2").build();

        BusinessException ex = assertThrows(BusinessException.class, () -> {localidadService.modificarLocalidad(modified);});

        assertThat(ex.getMessage().toLowerCase()).contains("ya existe");
    }

    @Test
    void modificarLocalidadNoExistente_lanzaBusinessException() {
        LocalidadDto dto = LocalidadDto.builder().id(999L).denominacion("Localidad 1").build();

        BusinessException ex = assertThrows(BusinessException.class, () -> {localidadService.modificarLocalidad(dto);});

        assertThat(ex.getMessage().toLowerCase()).contains("no existe");
    }

    @Test
    void eliminarYBuscarLocalidad_lanzaBusinessException() {
        LocalidadDto dto = LocalidadDto.builder().id(999L).denominacion("Localidad 1").build();
        Localidad loc = localidadService.crearLocalidad(dto);

        localidadService.eliminarLocalidad(loc.getId());

        BusinessException ex = assertThrows(BusinessException.class, () -> {localidadService.buscarLocalidad(loc.getId());});

        assertThat(ex.getMessage().toLowerCase()).contains("no existe");
    }

    @Test
    void eliminarLocalidadNoExistente_lanzaBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class, () -> {localidadService.eliminarLocalidad(999L);});
        assertThat(ex.getMessage().toLowerCase()).contains("no existe");
    }
}
