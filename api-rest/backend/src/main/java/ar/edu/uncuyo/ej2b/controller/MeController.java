package ar.edu.uncuyo.ej2b.controller;

import ar.edu.uncuyo.ej2b.auth.CustomUserDetails;
import ar.edu.uncuyo.ej2b.dto.usuario.UsuarioDetailDto;
import ar.edu.uncuyo.ej2b.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class MeController {

    private final UsuarioService usuarioService;

    @GetMapping("/account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDetailDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        UsuarioDetailDto usuario = usuarioService.findDto(Long.parseLong(jwt.getSubject()));
        return ResponseEntity.ok(usuario);
    }
}
