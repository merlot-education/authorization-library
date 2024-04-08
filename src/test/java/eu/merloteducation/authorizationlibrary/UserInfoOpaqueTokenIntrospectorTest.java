package eu.merloteducation.authorizationlibrary;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import eu.merloteducation.authorizationlibrary.authorization.UserInfoOpaqueTokenIntrospector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
@WireMockTest(httpPort = 8102)
class UserInfoOpaqueTokenIntrospectorTest {

    @Autowired
    private UserInfoOpaqueTokenIntrospector userInfoOpaqueTokenIntrospector;

    @Test
    void getUserInfoClaimsSuccessful() {
        stubFor(get("/userinfo")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"some\": \"data\"}")));

        OAuth2AuthenticatedPrincipal principal = this.userInfoOpaqueTokenIntrospector.introspect("1234");
        assertEquals("data", principal.getAttribute("some"));
    }

    @Test
    void getUserInfoClaimsEmpty() {
        stubFor(get("/userinfo")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{}")));

        OAuth2AuthenticatedPrincipal principal = this.userInfoOpaqueTokenIntrospector.introspect("1234");
        assertNull(principal);
    }
}
