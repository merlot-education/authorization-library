package eu.merloteducation.authorizationlibrary.authorization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component("keycloakJwtAuthConverter")
@ConditionalOnProperty(name = "jwt-auth-converter", havingValue = "keycloakJwtAuthConverter")
public class KeycloakJwtAuthConverter implements JwtAuthConverter {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractResourceRoles(jwt);

        return new JwtAuthenticationToken(jwt, authorities, getFullName(jwt));
    }

    private String getFullName(Jwt jwt) {
        return jwt.getClaim("name");
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {

        Map<String, Object> resourceAccess = jwt.getClaim("realm_access");
        Collection<String> resourceRoles;
        if (resourceAccess == null || (resourceRoles = (Collection<String>) resourceAccess.get("roles")) == null) {
            return Set.of();
        }

        return resourceRoles.stream().filter(r -> r.contains("_")).map(r -> {
                            try {
                                return new OrganizationRoleGrantedAuthority(r);
                            } catch (IllegalArgumentException ignored) {
                                return null;
                            }
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}