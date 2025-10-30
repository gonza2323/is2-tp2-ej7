package ar.edu.uncuyo.ej2b.repository;

import ar.edu.uncuyo.ej2b.entity.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomicilioRepository extends JpaRepository<Domicilio, Long> {
    Optional<Domicilio> findByIdAndEliminadoFalse(Long id);
}
