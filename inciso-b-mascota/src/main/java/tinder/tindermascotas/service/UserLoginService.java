package tinder.tindermascotas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tinder.tindermascotas.config.CustomUserDetails;
import tinder.tindermascotas.entities.User;
import tinder.tindermascotas.repositories.UserRepository;


@Service
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByMail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return CustomUserDetails.builder()
                .id(user.getId())
                .email(user.getMail())
                .password(user.getClave())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .build();
    }
}
