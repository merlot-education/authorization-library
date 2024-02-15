package eu.merloteducation.authorizationlibrary.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("jwtAuthConverter")
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Autowired
    private OpaqueTokenIntrospector opaqueTokenIntrospector;

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtAuthConverterProperties jwtAuthConverterProperties;

    public JwtAuthConverter(JwtAuthConverterProperties jwtAuthConverterProperties) {

        this.jwtAuthConverterProperties = jwtAuthConverterProperties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        OAuth2AuthenticatedPrincipal principal = opaqueTokenIntrospector.introspect(jwt.getTokenValue());
        Collection<GrantedAuthority> authorities = Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
            extractResourceRoles(principal).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {

        String claimName = JwtClaimNames.SUB;
        if (jwtAuthConverterProperties.getPrincipalAttribute() != null) {
            claimName = jwtAuthConverterProperties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(OAuth2AuthenticatedPrincipal principal) {
        if (principal == null) {
            return Collections.emptySet();
        }
        return Set.of(new OrganizationRoleGrantedAuthority(
                principal.getAttribute("Role") + "_" + principal.getAttribute("issuerDID")));
    }
}