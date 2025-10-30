package ar.edu.uncuyo.ej2b.repository;

import ar.edu.uncuyo.ej2b.entity.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    boolean existsByNombreAndEliminadoFalse(String nombre);
    boolean existsByDniAndEliminadoFalse(Integer dni);
    boolean existsByDniAndIdNotAndEliminadoFalse(Integer dni, Long id);

    Optional<Persona> findByIdAndEliminadoFalse(Long id);

    Page<Persona> findAllByEliminadoFalse(Pageable pageable);
    List<Persona> findAllByEliminadoFalse(Sort sort);
}
