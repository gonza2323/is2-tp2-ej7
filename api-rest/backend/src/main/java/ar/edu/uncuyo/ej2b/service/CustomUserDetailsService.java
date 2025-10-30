package ar.edu.uncuyo.ej2b.service;

import ar.edu.uncuyo.ej2b.auth.CustomUserDetails;
import ar.edu.uncuyo.ej2b.entity.Usuario;
import ar.edu.uncuyo.ej2b.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailAndEliminadoFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return CustomUserDetails.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .clave(usuario.getClave())
                .rol(usuario.getRol()).build();
    }
}