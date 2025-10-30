package ar.edu.uncuyo.ej2b.controller;

import ar.edu.uncuyo.ej2b.dto.LocalidadDto;
import ar.edu.uncuyo.ej2b.dto.PersonaDto;
import ar.edu.uncuyo.ej2b.entity.Persona;
import ar.edu.uncuyo.ej2b.mapper.PersonaMapper;
import ar.edu.uncuyo.ej2b.service.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;
    private final PersonaMapper personaMapper;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buscarPersona(@PathVariable Long id) {
        PersonaDto persona = personaService.buscarPersonaDto(id);
        return ResponseEntity.ok(persona);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> listarPersonas(Pageable pageable) {
        Page<PersonaDto> personas = personaService.listarPersonasDtos(pageable);
        return ResponseEntity.ok(personas);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearPersona(@Valid @RequestBody PersonaDto personaDto) {
        Persona persona = personaService.crearPersona(personaDto);
        PersonaDto dto = personaMapper.toDto(persona);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> listarTodasLasPersonas() {
        List<PersonaDto> personas = personaService.listarPersonasDtosTodas(Sort.by("apellido"));
        return ResponseEntity.ok(personas);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modificarPersona(@PathVariable Long id, @Valid @RequestBody PersonaDto personaDto) {
        personaDto.setId(id);
        Persona persona = personaService.modificarPersona(personaDto);
        PersonaDto dto = personaMapper.toDto(persona);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarPersona(@PathVariable Long id) {
        personaService.eliminarPersona(id);
        return ResponseEntity.noContent().build();
    }
}
