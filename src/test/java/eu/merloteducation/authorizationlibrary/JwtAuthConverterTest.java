package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { JwtAuthConverter.class, JwtAuthConverterProperties.class })
class JwtAuthConverterTest {

    @Autowired
    JwtAuthConverter jwtAuthConverter;

    @MockBean
    OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Test
    void convertJwt() {
        when(opaqueTokenIntrospector.introspect(any()))
                .thenReturn(new OAuth2IntrospectionAuthenticatedPrincipal(
                        Map.of("Role", "OrgLegRep",
                                "issuerDID","did:web:marketplace.dev.merlot-education.eu#14e2471b-a276-3349-8a6e-caa941f9369b"),
                        null));
        Jwt jwt = new Jwt("someValue", Instant.now(), Instant.now().plusSeconds(999), Map.of("header1", "header1"),
            Map.of("sub", "myUserId"));
        Authentication auth = jwtAuthConverter.convert(jwt);
        List<OrganizationRoleGrantedAuthority> orgaAuths = (List<OrganizationRoleGrantedAuthority>) auth.getAuthorities();
        assertEquals("OrgLegRep", orgaAuths.get(0).getOrganizationRole());
        assertEquals("did:web:marketplace.dev.merlot-education.eu#14e2471b-a276-3349-8a6e-caa941f9369b", orgaAuths.get(0).getOrganizationId());

    }

    @Test
    void convertJwtEmpty() {

        Jwt jwt = new Jwt("someValue", Instant.now(), Instant.now().plusSeconds(999), Map.of("header1", "header1"),
            Map.of("sub", "myUserId", "realm_access", Collections.emptyMap()));
        Authentication auth = jwtAuthConverter.convert(jwt);
        assertTrue(auth.getAuthorities().isEmpty());

        jwt = new Jwt("someValue", Instant.now(), Instant.now().plusSeconds(999), Map.of("header1", "header1"),
            Map.of("sub", "myUserId", "realm_access", Map.of("roles", Collections.emptyList())));
        auth = jwtAuthConverter.convert(jwt);
        assertTrue(auth.getAuthorities().isEmpty());

    }
}
