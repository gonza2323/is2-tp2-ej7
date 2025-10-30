package ar.edu.uncuyo.ej2b.init;

import ar.edu.uncuyo.ej2b.dto.*;
import ar.edu.uncuyo.ej2b.dto.libro.LibroCreateDto;
import ar.edu.uncuyo.ej2b.entity.Localidad;
import ar.edu.uncuyo.ej2b.repository.LocalidadRepository;
import ar.edu.uncuyo.ej2b.repository.PersonaRepository;
import ar.edu.uncuyo.ej2b.service.AutorService;
import ar.edu.uncuyo.ej2b.service.LibroService;
import ar.edu.uncuyo.ej2b.service.LocalidadService;
import ar.edu.uncuyo.ej2b.service.PersonaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {

    private final PersonaRepository personaRepository;
    private final LocalidadService localidadService;
    private final AutorService autorService;
    private final PersonaService personaService;
    private final Random random;

    final int CANT_LOCALIDADES = 50;
    final int CANT_AUTORES = 50;
    final int CANT_PERSONAS = 50;
    final int CANT_LIBROS = 50;
    private final LibroService libroService;
    private final LocalidadRepository localidadRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        crearDatosIniciales();
    }

    @Transactional
    protected void crearDatosIniciales() throws Exception {
        if (personaRepository.existsByNombreAndEliminadoFalse("NOMBRE 01")) {
            System.out.println("Datos iniciales ya creados. Salteando creación de datos iniciales. Para forzar su creación, borrar la base de datos");
            return;
        }

        // Nos damos permisos para poder crear los datos iniciales
//        var authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRATIVO"));
//        var auth = new UsernamePasswordAuthenticationToken("system", null, authorities);
//        SecurityContextHolder.getContext().setAuthentication(auth);

        System.out.println("Creando datos iniciales...");

        // Creación de datos iniciales
        crearLocalidades();
        crearAutores();
        crearPersonas();
        crearLibros();

        // Resetear los permisos
//        SecurityContextHolder.clearContext();

        System.out.println("Datos iniciales creados.");
    }

    @Transactional
    protected void crearLocalidades() {
        for (int i = 1; i < CANT_LOCALIDADES + 1; i++) {
            localidadService.crearLocalidad(LocalidadDto.builder()
                    .denominacion("LOCALIDAD " + String.format("%02d", i))
                    .build());
        }
    }

    @Transactional
    protected void crearAutores() {
        for (int i = 1; i < CANT_AUTORES + 1; i++) {
            autorService.crearAutor(AutorDto.builder()
                    .nombre("NOMBRE " + String.format("%02d", i))
                    .apellido("APELLIDO " + String.format("%02d", i))
                    .biografia("BIOGRAFÍA " + String.format("%02d", i))
                    .build());
        }
    }

    @Transactional
    protected void crearPersonas() {
        List<Localidad> localidades = localidadRepository.findAll();

        for (int i = 1; i < CANT_PERSONAS + 1; i++) {
            long randomLocalidadId = localidades.get(random.nextInt(localidades.size())).getId();
            personaService.crearPersona(PersonaDto.builder()
                    .nombre("NOMBRE " + String.format("%02d", i))
                    .apellido("APELLIDO " + String.format("%02d", i))
                    .dni(i)
                    .domicilio(DomicilioDto.builder()
                            .calle("CALLE " + String.format("%02d", random.nextInt(1, 99)))
                            .numeracion(random.nextInt(1, 10000))
                            .localidadId(randomLocalidadId).build())
                    .build());
        }
    }

    @Transactional
    protected void crearLibros() {
        for (int i = 1; i < CANT_LIBROS + 1; i++) {
            crearLibroRandom(i);
        }
    }

    @Transactional
    protected void crearLibroRandom(int i) {
        libroService.crearLibro(LibroCreateDto.builder()
                .titulo("TÍTULO " + String.format("%02d", i))
                .fecha(random.nextInt(1900, 2025))
                .paginas(random.nextInt(20, 3000))
                .genero("GÉNERO " + String.format("%02d", random.nextInt(0, 30)))
                .personaId(random.nextLong(1, CANT_PERSONAS + 1))
                .autoresIds(crearListaAutoresIdsRandom())
                .build());
    }

    private List<Long> crearListaAutoresIdsRandom() {
        int length = random.nextInt(1, 4);

        Set<Long> ids = new HashSet<>();
        while (ids.size() < length) {
            long id = random.nextLong(1, CANT_AUTORES + 1);
            ids.add(id);
        }

        return new ArrayList<>(ids);
    }

    private LocalDate randomDate(LocalDate start, LocalDate end) {
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        long randomDays = random.nextLong(0, daysBetween + 1);
        return start.plusDays(randomDays);
    }
}
