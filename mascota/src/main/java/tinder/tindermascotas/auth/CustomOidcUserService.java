package tinder.tindermascotas.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import tinder.tindermascotas.UserDetailDto;
import tinder.tindermascotas.config.CustomUserDetails;

@Service
@RequiredArgsConstructor // Esto reemplaza al constructor manual
public class CustomOidcUserService extends OidcUserService { // <-- ARREGLO 1: "extends"

    private final AuthService authService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        // --- ARREGLO 2: Llama al método "padre" (super) ---
        OidcUser oidcUser = super.loadUser(userRequest);
        // ----------------------------------------

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oidcUser.getAttribute("sub");
        String email = oidcUser.getAttribute("email");


        // Buscamos el usuario...
        UserDetailDto usuario = authService.findByOAuthProviderIdOrEmail(provider, providerId, email);

        // --- ARREGLO 3: ¡Maneja el caso de que el usuario NO exista! ---
        if (usuario == null) {
            // Este es tu requisito: "no crear usuario", por lo tanto, fallamos la autenticación.
            throw new OAuth2AuthenticationException(
                    "El usuario con email " + email + " no está registrado en el sistema."
            );
        }
        // -----------------------------------------------------------

        // Si existe, crea tu CustomUserDetails
        // (Asegúrate de que CustomUserDetails implemente OidcUser)
        CustomUserDetails userDetails = new CustomUserDetails(usuario, oidcUser);

        System.out.println("OIDC Provider: " + provider);
        System.out.println("OIDC sub: " + providerId);
        System.out.println("OIDC email: " + email);


        return userDetails;
    }
}