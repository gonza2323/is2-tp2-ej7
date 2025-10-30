package ar.edu.uncuyo.ej2b.controller;

import ar.edu.uncuyo.ej2b.dto.LocalidadDto;
import ar.edu.uncuyo.ej2b.entity.Localidad;
import ar.edu.uncuyo.ej2b.mapper.LocalidadMapper;
import ar.edu.uncuyo.ej2b.service.LocalidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/localidades")
@RequiredArgsConstructor
public class LocalidadController {

    private final LocalidadService localidadService;
    private final LocalidadMapper localidadMapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarLocalidad(@PathVariable Long id) {
        LocalidadDto localidad = localidadService.buscarLocalidadDto(id);
        return ResponseEntity.ok(localidad);
    }

    @GetMapping
    public ResponseEntity<?> listarLocalidades(Pageable pageable) {
        Page<LocalidadDto> localidades = localidadService.listarLocalidadesDtos(pageable);
        return ResponseEntity.ok(localidades);
    }

    @GetMapping("/all")
    public ResponseEntity<?> listarTodasLasLocalidades() {
        List<LocalidadDto> localidades = localidadService.listarLocalidadesDtosTodas(Sort.by("denominacion"));
        return ResponseEntity.ok(localidades);
    }

    @PostMapping
    public ResponseEntity<?> crearLocalidad(@Valid @RequestBody LocalidadDto localidadDto) {
        Localidad localidad = localidadService.crearLocalidad(localidadDto);
        LocalidadDto dto = localidadMapper.toDto(localidad);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarLocalidad(@PathVariable Long id, @Valid @RequestBody LocalidadDto localidadDto) {
        localidadDto.setId(id);
        Localidad localidad = localidadService.modificarLocalidad(localidadDto);
        LocalidadDto dto = localidadMapper.toDto(localidad);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLocalidad(@PathVariable Long id) {
        localidadService.eliminarLocalidad(id);
        return ResponseEntity.noContent().build();
    }
}
