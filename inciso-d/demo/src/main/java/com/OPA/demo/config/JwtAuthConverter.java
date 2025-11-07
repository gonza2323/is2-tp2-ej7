package com.OPA.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Produces {@link JwtAuthenticationToken} instances enriched with authorities derived from Auth0 claims,
 * while checking issuer and audience without custom JwtDecoder beans.
 */
@Component
public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final String expectedAudience;
    private final String expectedIssuer;

    public JwtAuthConverter(@Value("${app.security.audience}") String expectedAudience,
                            @Value("${app.security.issuer}") String expectedIssuer) {
        this.expectedAudience = expectedAudience;
        this.expectedIssuer = expectedIssuer;
        // leverage Auth0 permissions claim as authorities when present
        this.defaultAuthoritiesConverter.setAuthoritiesClaimName("permissions");
        this.defaultAuthoritiesConverter.setAuthorityPrefix("");
    }

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        validateClaims(jwt);

        Collection<GrantedAuthority> authorities = new HashSet<>(defaultAuthoritiesConverter.convert(jwt));
        authorities.addAll(extractRoles(jwt));
        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }

    private void validateClaims(Jwt jwt) {
        if (expectedAudience != null && !expectedAudience.isBlank()) {
            List<String> audiences = jwt.getAudience();
            if (audiences == null || !audiences.contains(expectedAudience)) {
                throw new JwtException("Invalid audience");
            }
        }
        if (expectedIssuer != null && !expectedIssuer.isBlank()) {
            if (jwt.getIssuer() == null || !expectedIssuer.equals(jwt.getIssuer().toString())) {
                throw new JwtException("Invalid issuer");
            }
        }
    }

    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        Set<String> roles = jwt.getClaims().entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getKey().toLowerCase().endsWith("roles"))
                .map(entry -> getClaimAsList(jwt, entry.getKey()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        if (roles.isEmpty()) {
            return Set.of();
        }

        return roles.stream()
                .filter(Objects::nonNull)
                .map(this::normalizeRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private String normalizeRole(String role) {
        String normalized = role.trim();
        if (normalized.isEmpty()) {
            return normalized;
        }
        if (!normalized.startsWith("ROLE_")) {
            normalized = "ROLE_" + normalized.replace(' ', '_').toUpperCase();
        }
        return normalized;
    }

    private List<String> getClaimAsList(Jwt jwt, String claimName) {
        Object claim = jwt.getClaim(claimName);
        if (claim instanceof List<?> list) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        if (claim instanceof String str && !str.isBlank()) {
            return List.of(str);
        }
        return List.of();
    }
}
