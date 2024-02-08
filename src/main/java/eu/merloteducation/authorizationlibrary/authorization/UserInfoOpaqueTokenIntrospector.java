package eu.merloteducation.authorizationlibrary.authorization;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class UserInfoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final String userInfoUri = "https://auth-service.dev.merlot-education.eu/userinfo";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        Map<String, Object> claims = restTemplate.exchange(userInfoUri, HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();
        return new OAuth2IntrospectionAuthenticatedPrincipal(claims, List.of());
    }
}