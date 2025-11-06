package ar.edu.uncuyo.ej2b.controller;

import ar.edu.uncuyo.ej2b.auth.CustomUserDetails;
import ar.edu.uncuyo.ej2b.dto.usuario.UsuarioDetailDto;
import ar.edu.uncuyo.ej2b.service.JwtService;
import ar.edu.uncuyo.ej2b.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@EnableMethodSecurity(prePostEnabled = true)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtEncoder jwtEncoder;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            String token = jwtService.generateToken(authentication);

            // Get user details
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return ResponseEntity.ok(new LoginResponse(
                    token,
                    userDetails.getId(),
                    userDetails.getUsername()
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDetailDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UsuarioDetailDto usuario = usuarioService.findDto(userDetails.getId());
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    public record LoginRequest(String email, String password) {}

    public record LoginResponse(String token, Long userId, String username) {}

    public record UserInfo(Long userId, String username) {}
}
