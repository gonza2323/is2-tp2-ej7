package ar.edu.uncuyo.gimnasio_sport.auth;

import ar.edu.uncuyo.gimnasio_sport.dto.UsuarioDetailDto;
import ar.edu.uncuyo.gimnasio_sport.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final AuthService authService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = new OidcUserService().loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oidcUser.getAttribute("sub");
        String email = oidcUser.getAttribute("email");

        // Buscamos el usuario según providerId. Si no está asociado al proveedor, es asociado según email.
        UsuarioDetailDto usuario = authService.findByOAuthProviderIdOrEmail(provider, providerId, email);
        CustomUserDetails userDetails = new CustomUserDetails(usuario, oidcUser);
        return userDetails;
    }
}
