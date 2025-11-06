package tinder.tindermascotas.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinder.tindermascotas.UserDetailDto;
import tinder.tindermascotas.entities.User;
import tinder.tindermascotas.mappers.UserMapper;
import tinder.tindermascotas.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository usuarioRepository;
    private final UserMapper userMapper;


    @Transactional
    public UserDetailDto findByOAuthProviderIdOrEmail(String provider, String providerId, String email) {

        // Buscamos usuario por OAuth2 user id
        User usuario = usuarioRepository.findByProviderUserId(providerId)
                .orElse(null);
//
//        // Si no hay, buscamos por email y lo asociamos a este OAuth2 id. Si no hay, arroja excepción
//        if (usuario == null) {
//            usuario = usuarioRepository.findByNombre(email)
//                    .orElseThrow();
//
//            // si ya tiene otro proveedor de identidad, arrojamos excepción
//            if (usuario.getProvider() != null)
//                throw new BusinessException("Email ya asociado a otro proveedor de identidad");
//
//            usuario.setProvider(provider);
//            usuario.setProviderUserId(providerId);
//        }

        return userMapper.toDto(usuario);
    }
}