package ar.edu.uncuyo.gimnasio_sport.auth;

import ar.edu.uncuyo.gimnasio_sport.dto.UsuarioDetailDto;
import ar.edu.uncuyo.gimnasio_sport.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioDetailDto usuario = usuarioService.buscarUsuarioDtoByUsername(username);
        return new CustomUserDetails(usuario);
    }
}