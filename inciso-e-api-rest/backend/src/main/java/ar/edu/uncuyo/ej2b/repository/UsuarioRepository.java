package ar.edu.uncuyo.ej2b.repository;

import ar.edu.uncuyo.ej2b.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    boolean existsByEmailAndEliminadoFalse(String name);
    boolean existsByEmailAndIdNotAndEliminadoFalse(String name, Long id);

    Optional<Usuario> findByEmailAndEliminadoFalse(String nombre);
}
