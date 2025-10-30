package ar.edu.uncuyo.ej2b.repository;

import ar.edu.uncuyo.ej2b.entity.Libro;
import ar.edu.uncuyo.ej2b.entity.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByIdAndEliminadoFalse(Long id);

    Page<Libro> findAllByEliminadoFalse(Pageable pageable);

    List<Libro> findAllByPersonaAndEliminadoFalse(Persona persona);
}
