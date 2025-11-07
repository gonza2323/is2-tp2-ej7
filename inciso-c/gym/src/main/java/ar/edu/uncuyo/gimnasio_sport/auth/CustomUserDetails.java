package ar.edu.uncuyo.gimnasio_sport.auth;

import ar.edu.uncuyo.gimnasio_sport.dto.UsuarioDetailDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomUserDetails implements UserDetails, OidcUser, OAuth2User {

    private final UsuarioDetailDto usuario;
    private final OidcUser oidcUser;

    public CustomUserDetails(UsuarioDetailDto usuario) {
        this.usuario = usuario;
        this.oidcUser = null;
    }

    public CustomUserDetails(UsuarioDetailDto usuario, OidcUser oidcUser) {
        this.usuario = usuario;
        this.oidcUser = oidcUser;
    }

    // función propia para poder saber la id del usuario autenticado (más útil que el nombre de usuario)

    public Long getId() {
        return usuario.getId();
    }


    // Métodos para cumplir la interfaz UserDetails (para autenticación con usuario y contraseña)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getClave();
    }

    @Override
    public String getUsername() {
        return usuario.getNombreUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    // Métodos para cumplir interfaces OidcUser y OAuth2User (para autenticación con proveedor de identidad)

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser != null ? oidcUser.getClaims() : Collections.emptyMap();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser != null ? oidcUser.getUserInfo() : null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser != null ? oidcUser.getIdToken() : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser != null ? oidcUser.getAttributes() : Collections.emptyMap();
    }

    @Override
    public String getName() {
        return usuario.getNombreUsuario();
    }
}