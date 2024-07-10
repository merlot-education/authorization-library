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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component("activeRoleHeaderHandlerInterceptor")
public class ActiveRoleHeaderHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthorityChecker authorityChecker;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull Object handler) {

        if (request.getHeader("Active-Role") != null) {
            OrganizationRoleGrantedAuthority activeRole = new OrganizationRoleGrantedAuthority(
                request.getHeader("Active-Role"));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String organizationId = activeRole.getOrganizationId();
            if (activeRole.isRepresentative() && !authorityChecker.representsOrganization(authentication, organizationId)
                || activeRole.isFedAdmin() && !authorityChecker.administratesOrganization(authentication, organizationId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        return true;
    }
}
