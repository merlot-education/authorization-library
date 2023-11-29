package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = { JwtAuthConverter.class, JwtAuthConverterProperties.class })
class JwtAuthConverterTest {

    @Autowired
    JwtAuthConverter jwtAuthConverter;

    @Test
    void convertJwt() {

        Jwt jwt = new Jwt("someValue", Instant.now(), Instant.now().plusSeconds(999), Map.of("header1", "header1"),
            Map.of("sub", "myUserId", "realm_access", Map.of("roles", Set.of("OrgLegRep_10", "SomeOtherRole"))));
        Authentication auth = jwtAuthConverter.convert(jwt);
        List<OrganizationRoleGrantedAuthority> orgaAuths = (List<OrganizationRoleGrantedAuthority>) auth.getAuthorities();
        assertEquals("OrgLegRep", orgaAuths.get(0).getOrganizationRole());
        assertEquals("10", orgaAuths.get(0).getOrganizationId());

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
