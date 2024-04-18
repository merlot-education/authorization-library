package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        properties = "jwt-auth-converter=keycloakJwtAuthConverter",
        classes = { JwtAuthConverter.class, KeycloakJwtAuthConverter.class }
)
class KeycloakJwtAuthConverterTest {

    @Autowired
    KeycloakJwtAuthConverter keycloakJwtAuthConverter;

    @MockBean
    OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Test
    void convertJwt() {

        Jwt jwt = new Jwt("someValue", Instant.now(), Instant.now().plusSeconds(999), Map.of("header1", "header1"),
                Map.of("sub", "myUserId", "realm_access", Map.of("roles", Set.of("OrgLegRep_10", "SomeOtherRole"))));
        Authentication auth = keycloakJwtAuthConverter.convert(jwt);
        List<OrganizationRoleGrantedAuthority> orgaAuths = (List<OrganizationRoleGrantedAuthority>) auth.getAuthorities();
        assertEquals("OrgLegRep", orgaAuths.get(0).getOrganizationRole());
        assertEquals("10", orgaAuths.get(0).getOrganizationId());

    }

    @Test
    void convertJwtEmpty() {

        Jwt jwt = new Jwt("someValue", Instant.now(), Instant.now().plusSeconds(999), Map.of("header1", "header1"),
                Map.of("sub", "myUserId", "realm_access", Collections.emptyMap()));
        Authentication auth = keycloakJwtAuthConverter.convert(jwt);
        assertTrue(auth.getAuthorities().isEmpty());

        jwt = new Jwt("someValue", Instant.now(), Instant.now().plusSeconds(999), Map.of("header1", "header1"),
                Map.of("sub", "myUserId", "realm_access", Map.of("roles", Collections.emptyList())));
        auth = keycloakJwtAuthConverter.convert(jwt);
        assertTrue(auth.getAuthorities().isEmpty());

    }
}
