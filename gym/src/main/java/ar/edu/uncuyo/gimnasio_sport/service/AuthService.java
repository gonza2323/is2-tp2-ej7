package ar.edu.uncuyo.gimnasio_sport.service;

import ar.edu.uncuyo.gimnasio_sport.dto.UsuarioDetailDto;
import ar.edu.uncuyo.gimnasio_sport.entity.Usuario;
import ar.edu.uncuyo.gimnasio_sport.error.BusinessException;
import ar.edu.uncuyo.gimnasio_sport.mapper.UsuarioMapper;
import ar.edu.uncuyo.gimnasio_sport.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public UsuarioDetailDto findByOAuthProviderIdOrEmail(String provider, String providerId, String email) {

        // Buscamos usuario por OAuth2 user id
        Usuario usuario = usuarioRepository.findByProviderUserIdAndEliminadoFalse(providerId)
                .orElse(null);

        // Si no hay, buscamos por email y lo asociamos a este OAuth2 id. Si no hay, arroja excepción
        if (usuario == null) {
            usuario = usuarioRepository.findByNombreUsuarioAndEliminadoFalse(email)
                            .orElseThrow();

            // si ya tiene otro proveedor de identidad, arrojamos excepción
            if (usuario.getProvider() != null)
                throw new BusinessException("Email ya asociado a otro proveedor de identidad");

            usuario.setProvider(provider);
            usuario.setProviderUserId(providerId);
        }

        return usuarioMapper.toDto(usuario);
    }
}
