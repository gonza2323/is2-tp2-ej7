package tinder.tindermascotas.config;

// Imports de Spring Boot 3 / Security 6 (¡importante!)
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tinder.tindermascotas.auth.CustomOidcUserService;
import tinder.tindermascotas.service.UserLoginService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // --- ARREGLADO: Inyección por constructor para AMBOS servicios ---
    private final UserLoginService userLoginService;
    private final CustomOidcUserService oidcUserService;

    public SecurityConfig(UserLoginService userLoginService, CustomOidcUserService oidcUserService) {
        this.userLoginService = userLoginService;
        this.oidcUserService = oidcUserService;
    }
    // -----------------------------------------------------------------

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Permite las rutas públicas
                        .requestMatchers("/", "/registro", "/usuario/registrar", "/exito", "/error", "/favicon.ico").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()

                        // Permite los assets estáticos
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/vendor/**").permitAll()

                        // Asegura que las peticiones al dispatcher de ERROR sean permitidas
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                        // Todas las demás peticiones requieren autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("mail")
                        .passwordParameter("clave")
                        .defaultSuccessUrl("/inicio", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService)
                        )
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Tienes "/inicio" en formLogin y "/" aquí, asegúrate que sea intencional
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Deshabilitado para pruebas, ¡recuerda habilitarlo en producción!

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // --- ARREGLADO: NoOpPasswordEncoder ya no existe o no es soportado ---
        // Se debe usar un encriptador real. BCrypt es el estándar.
        return new BCryptPasswordEncoder();
    }
}
