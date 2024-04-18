package eu.merloteducation.authorizationlibrary.authorization;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtAuthConverter extends Converter<Jwt, AbstractAuthenticationToken> {
    // no further methods needed
}
