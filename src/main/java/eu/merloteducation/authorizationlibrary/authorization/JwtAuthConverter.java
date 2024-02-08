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
        // TODO replace this logic once OCM is updated
        String role = principal.getAttribute("Role");
        if (role == null) {
            return Collections.emptySet();
        }
        return switch (role) {
            case "dataport" ->
                    Set.of(new OrganizationRoleGrantedAuthority("OrgLegRep_did:web:marketplace.dev.merlot-education.eu#14e2471b-a276-3349-8a6e-caa941f9369b"));
            case "capgemini" ->
                    Set.of(new OrganizationRoleGrantedAuthority("OrgLegRep_did:web:marketplace.dev.merlot-education.eu#1c092e75-4a75-3746-9c76-a737389e3e49"));
            case "gaia" ->
                    Set.of(new OrganizationRoleGrantedAuthority("OrgLegRep_did:web:marketplace.dev.merlot-education.eu#c041ea73-3ecf-3a06-a5cd-919f5cef8e54"));
            default -> Collections.emptySet();
        };
    }
}