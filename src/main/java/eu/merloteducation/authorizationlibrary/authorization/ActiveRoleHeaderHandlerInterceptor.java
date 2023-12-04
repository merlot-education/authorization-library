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
