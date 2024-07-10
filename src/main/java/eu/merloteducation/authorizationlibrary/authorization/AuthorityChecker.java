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
