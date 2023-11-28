package eu.merloteducation.authorizationlibrary.authorization;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("jwtAuthConverter")
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtAuthConverterProperties jwtAuthConverterProperties;

    public JwtAuthConverter(JwtAuthConverterProperties jwtAuthConverterProperties) {
        this.jwtAuthConverterProperties = jwtAuthConverterProperties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (jwtAuthConverterProperties.getPrincipalAttribute() != null) {
            claimName = jwtAuthConverterProperties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("realm_access");
        Collection<String> resourceRoles;
        if (resourceAccess == null
                || (resourceRoles = (Collection<String>) resourceAccess.get("roles")) == null) {
            return Set.of();
        }
        return resourceRoles.stream().filter(s -> s.startsWith("OrgRep") || s.startsWith("OrgLegRep"))
                .map(OrganizationRoleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}