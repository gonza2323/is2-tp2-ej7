package com.OPA.demo.init;

import com.OPA.demo.enums.ERole;
import com.OPA.demo.entity.Usuario;
import com.OPA.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.buscarPorEmail("admin@example.com") == null) {
            Usuario adminUser = Usuario.builder()
                    .dni(11111111L)
                    .nombre("admin")
                    .email("admin@example.com")
                    .clave(new BCryptPasswordEncoder().encode("40919162"))
                    .telefono("1234567890")
                    .rol(ERole.ADMIN)
                    .alta(true)
                    .build();

            usuarioRepository.save(adminUser);
        }
    }
}