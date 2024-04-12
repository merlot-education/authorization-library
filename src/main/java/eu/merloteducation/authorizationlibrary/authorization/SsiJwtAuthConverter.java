package eu.merloteducation.authorizationlibrary.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Component("ssiJwtAuthConverter")
@ConditionalOnProperty(name = "jwt-auth-converter", havingValue = "ssiJwtAuthConverter")
public class SsiJwtAuthConverter implements JwtAuthConverter {

    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    private final JwtAuthConverterProperties jwtAuthConverterProperties;

    public SsiJwtAuthConverter(@Autowired OpaqueTokenIntrospector opaqueTokenIntrospector,
                               @Autowired JwtAuthConverterProperties jwtAuthConverterProperties) {
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
        this.jwtAuthConverterProperties = jwtAuthConverterProperties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        OAuth2AuthenticatedPrincipal principal = opaqueTokenIntrospector.introspect(jwt.getTokenValue());
        Collection<GrantedAuthority> authorities = extractResourceRoles(principal);

        String fullName = principal == null ? "" : principal.getAttribute("Vorname") + " " + principal.getAttribute("Nachname");

        return new MerlotAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt, principal), fullName);
    }

    private String getPrincipalClaimName(Jwt jwt, OAuth2AuthenticatedPrincipal principal) {

        String claimName = JwtClaimNames.SUB;
        if (jwtAuthConverterProperties.getPrincipalAttribute() != null) {
            claimName = jwtAuthConverterProperties.getPrincipalAttribute();
        }

        if (!jwt.hasClaim(claimName)) {
            return principal == null ? "" : principal.getAttribute("ID");
        }

        return jwt.getClaim(claimName);
    }

    private Collection<GrantedAuthority> extractResourceRoles(OAuth2AuthenticatedPrincipal principal) {
        if (principal == null) {
            return Collections.emptySet();
        }
        return Set.of(new OrganizationRoleGrantedAuthority(
                principal.getAttribute("Role") + "_" + principal.getAttribute("issuerDID")));
    }
}