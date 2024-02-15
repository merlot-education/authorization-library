package eu.merloteducation.authorizationlibrary.config;

import eu.merloteducation.authorizationlibrary.authorization.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@Component
public class MerlotSecurityConfig {
    @Autowired
    private JwtAuthConverter jwtAuthConverter;

    public void applySecurityConfig(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthConverter);
        //http.oauth2Login();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();
        http.headers().frameOptions().disable();
    }
}
