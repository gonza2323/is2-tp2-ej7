package ar.edu.uncuyo.ej2b.service;

import ar.edu.uncuyo.ej2b.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 3600L; // 1 hour

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Long userId = null;
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            userId = customUserDetails.getId();
        } else {
            throw new IllegalArgumentException();
        }

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(userId.toString());

        if (scope != null && !scope.isEmpty()) {
            claimsBuilder.claim("scope", scope);
        }

        JwtClaimsSet claims = claimsBuilder.build();

        return this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
