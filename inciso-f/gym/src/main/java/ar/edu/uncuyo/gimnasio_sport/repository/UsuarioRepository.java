package ar.edu.uncuyo.gimnasio_sport.repository;

import ar.edu.uncuyo.gimnasio_sport.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombreUsuarioAndEliminadoFalse(String nombre);
    Optional<Usuario> findByProviderUserIdAndEliminadoFalse(String providerUserId);

    boolean existsByNombreUsuarioAndEliminadoFalse(String nombreUsuario);
}
