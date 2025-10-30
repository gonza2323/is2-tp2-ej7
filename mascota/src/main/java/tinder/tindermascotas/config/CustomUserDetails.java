package tinder.tindermascotas.config;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import tinder.tindermascotas.UserDetailDto;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class CustomUserDetails implements UserDetails , OidcUser {
    private final String id;
    private final String email;
    private final String password;
    private final String nombre;
    private final String apellido;
    private final OidcUser oidcUser;
    private final UserDetailDto usuario;


    public CustomUserDetails(String id, String email, String password, String nombre, String apellido, OidcUser oidcUser, UserDetailDto usuario) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.oidcUser = oidcUser;
        this.usuario = usuario;
    }
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // no roles needed
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // Métodos para cumplir interfaces OidcUser y OAuth2User (para autenticación con proveedor de identidad)

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser != null ? oidcUser.getAttributes() : Collections.emptyMap();
    }

    @Override
    public String getName() {
        // Usamos el "sub" (identificador único de Google)
        String sub = (String) oidcUser.getAttributes().get("sub");
        if (sub != null && !sub.isBlank()) {
            return sub;
        }

        // Fallbacks si por alguna razón "sub" no existe
        String email = (String) oidcUser.getAttributes().get("email");
        if (email != null && !email.isBlank()) {
            return email;
        }

        return "unknown-" + UUID.randomUUID();

    }

}
