package eu.merloteducation.authorizationlibrary.authorization;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component("authorityChecker")
public class AuthorityChecker {
    public Set<String> getRepresentedOrgaIds(Authentication authentication) {
        Set<String> representedOrgaIds = new HashSet<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority instanceof OrganizationRoleGrantedAuthority orgaRoleAuthority &&
                    orgaRoleAuthority.isRepresentative()) {
                representedOrgaIds.add(orgaRoleAuthority.getOrganizationId());
            }
        }
        return representedOrgaIds;
    }

    public Set<String> getAdministratedOrgaIds(Authentication authentication) {
        Set<String> administratedOrgaIds = new HashSet<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority instanceof OrganizationRoleGrantedAuthority orgaRoleAuthority &&
                orgaRoleAuthority.isFedAdmin()) {
                administratedOrgaIds.add(orgaRoleAuthority.getOrganizationId());
            }
        }
        return administratedOrgaIds;
    }

    /**
     * Checks whether the user with the currently active authorization represents a given organization by the id.
     *
     * @param authentication current authentication
     * @param orgaId         id of the organization to request
     * @return user represents given organization
     */
    public boolean representsOrganization(Authentication authentication, String orgaId) {
        String numOrgaId = orgaId.replace("Participant:", "");
        Set<String> representedOrgaIds = getRepresentedOrgaIds(authentication);
        return representedOrgaIds.contains(numOrgaId);
    }

    /**
     * Checks whether the user with the currently active authorization administrates a given organization by the id.
     *
     * @param authentication current authentication
     * @param orgaId         id of the organization to request
     * @return user administrates given organization
     */
    public boolean administratesOrganization(Authentication authentication, String orgaId) {
        String numOrgaId = orgaId.replace("Participant:", "");
        Set<String> administratedOrgaIds = getAdministratedOrgaIds(authentication);
        return administratedOrgaIds.contains(numOrgaId);
    }
}
