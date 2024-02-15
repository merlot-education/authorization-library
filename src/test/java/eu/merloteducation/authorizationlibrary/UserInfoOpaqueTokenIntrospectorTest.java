package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.UserInfoOpaqueTokenIntrospector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserInfoOpaqueTokenIntrospectorTest {

    private UserInfoOpaqueTokenIntrospector userInfoOpaqueTokenIntrospector;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    WebClient.RequestBodySpec requestBodySpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        lenient().when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        lenient().when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        lenient().when(responseSpec.bodyToMono(eq(new ParameterizedTypeReference<Map<String, Object>>(){})))
                .thenReturn(Mono.just(Map.of("some", "value")));

        lenient().when(webClientBuilder.build()).thenReturn(webClient);
        this.userInfoOpaqueTokenIntrospector = new UserInfoOpaqueTokenIntrospector(webClientBuilder, "someuri");
    }

    @Test
    void getUserInfoClaimsSuccessful() {
        OAuth2AuthenticatedPrincipal principal = this.userInfoOpaqueTokenIntrospector.introspect("1234");
        assertEquals("value", principal.getAttribute("some"));
    }
}
