package eu.merloteducation.authorizationlibrary.authorization;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Getter
public class MerlotAuthenticationToken extends JwtAuthenticationToken {
    private final String fullName;

    public MerlotAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name,
        String fullName) {

        super(jwt, authorities, name);
        this.fullName = fullName;
    }

}
