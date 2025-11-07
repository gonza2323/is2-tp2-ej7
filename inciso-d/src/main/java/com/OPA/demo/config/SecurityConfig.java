package com.OPA.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                           CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(
                                "/",
                                "/login",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/registrar",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/registrar").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/libros/crear",
                                "/libros/nuevo"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/libros/guardar").hasRole("ADMIN")
                        .requestMatchers(
                                "/autores/crear",
                                "/autores/nuevo"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/autores/guardar").hasRole("ADMIN")
                        .requestMatchers(
                                "/editoriales/crear",
                                "/editoriales/nuevo"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/editoriales/guardar").hasRole("ADMIN")
                        .requestMatchers(
                                "/libros",
                                "/libros/",
                                "/libros/listar",
                                "/autores",
                                "/autores/",
                                "/autores/listar",
                                "/editoriales",
                                "/editoriales/",
                                "/editoriales/listar",
                                "/inicio",
                                "/perfil"
                        ).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/perfil").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
