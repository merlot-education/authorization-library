package eu.merloteducation.authorizationlibrary.config;

import eu.merloteducation.authorizationlibrary.authorization.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@Component
public class MerlotSecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;

    public MerlotSecurityConfig(@Autowired JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    /**
     * Given an instance of the HttpSecurity, apply the common security configurations of project MERLOT.
     *
     * @param http HttpSecurity reference
     * @throws Exception issue with applying the security configuration
     */
    public void applySecurityConfig(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        http.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(
                Customizer.withDefaults());
        http.headers(
                headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    }
}
