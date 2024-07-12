/*
 *  Copyright 2023-2024 Dataport AöR
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.merloteducation.authorizationlibrary.authorization;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Component("ssiJwtAuthConverter")
@ConditionalOnProperty(name = "jwt-auth-converter", havingValue = "ssiJwtAuthConverter")
public class SsiJwtAuthConverter implements JwtAuthConverter {

    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    public SsiJwtAuthConverter(@Autowired OpaqueTokenIntrospector opaqueTokenIntrospector) {
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        OAuth2AuthenticatedPrincipal principal = opaqueTokenIntrospector.introspect(jwt.getTokenValue());
        Collection<GrantedAuthority> authorities = extractResourceRoles(principal);

        return new JwtAuthenticationToken(jwt, authorities, getFullName(principal));
    }

    private String getFullName(OAuth2AuthenticatedPrincipal principal) {
        return principal == null
                ? ""
                : principal.getAttribute("Vorname") + " " + principal.getAttribute("Nachname");
    }

    private Collection<GrantedAuthority> extractResourceRoles(OAuth2AuthenticatedPrincipal principal) {
        if (principal == null) {
            return Collections.emptySet();
        }

        String role = principal.getAttribute("Role");
        String orgaId = principal.getAttribute("issuerDID");

        if (StringUtil.isNullOrEmpty(orgaId)
                || role == null
                || !OrganizationRole.getAvailableRoles().contains(role)) {
            return Collections.emptySet();
        }

        return Set.of(new OrganizationRoleGrantedAuthority(OrganizationRole.getByRoleName(role), orgaId));
    }
}