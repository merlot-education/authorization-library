package eu.merloteducation.authorizationlibrary.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class UserInfoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final String userInfoUri;
    private final WebClient webClient;

    public UserInfoOpaqueTokenIntrospector(@Autowired WebClient.Builder webClientBuilder,
                                           @Value("${spring.security.oauth2.resourceserver.jwt.userinfo-uri:#{null}}") String userInfoUri) {
        this.webClient = webClientBuilder.build();
        this.userInfoUri = userInfoUri;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        Map<String, Object> claims = webClient
                .get()
                .uri(userInfoUri)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (claims == null || claims.isEmpty()) {
            return null;
        }

        return new OAuth2IntrospectionAuthenticatedPrincipal(claims, List.of());
    }
}