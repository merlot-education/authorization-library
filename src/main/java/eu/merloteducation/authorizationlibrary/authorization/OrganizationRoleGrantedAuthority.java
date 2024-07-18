/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
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

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class OrganizationRoleGrantedAuthority implements GrantedAuthority {

    private final OrganizationRole organizationRole;
    private final String organizationId;

    /**
     * Create an OrganizationRoleGrantedAuthority from a given authority string.
     * The string is expected to contain the role name, followed by an underscore (_),
     * followed by the organization id.
     *
     * @param authorityString authority string
     */
    public OrganizationRoleGrantedAuthority(String authorityString) {
        if (!authorityString.contains("_")) {
            throw new IllegalArgumentException("Provided authority string does not follow expected format. "
                    + authorityString);
        }
        this.organizationRole = OrganizationRole.getByRoleName(authorityString.split("_")[0]);
        this.organizationId = authorityString.replace(this.organizationRole.getRoleName() + "_", "");
    }

    /**
     * Create an OrganizationRoleGrantedAuthority from the given role and organization id.
     *
     * @param role role
     * @param orgaId organization id
     */
    public OrganizationRoleGrantedAuthority(OrganizationRole role, String orgaId) {
        this.organizationRole = role;
        this.organizationId = orgaId;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + organizationRole.getRoleName() + "_" + organizationId;
    }

    public boolean isRepresentative() {
        return this.organizationRole.equals(OrganizationRole.ORG_LEG_REP);
    }

    public boolean isFedAdmin() {
        return this.organizationRole.equals(OrganizationRole.FED_ADMIN);
    }
}
