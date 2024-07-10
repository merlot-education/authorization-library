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

import lombok.Getter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum OrganizationRole {
    ORG_LEG_REP("OrgLegRep"),
    FED_ADMIN("FedAdmin");

    private final String roleName;

    private static final Map<String, OrganizationRole> roleNameMap = Arrays.stream(OrganizationRole.values())
            .collect(Collectors.toMap(OrganizationRole::getRoleName, Function.identity()));

    OrganizationRole(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Returns a set of Strings of available role names.
     *
     * @return set of role names
     */
    public static Set<String> getAvailableRoles() {
        return EnumSet.allOf(OrganizationRole.class)
                .stream()
                .map(OrganizationRole::getRoleName)
                .collect(Collectors.toSet());
    }

    /**
     * Given a role name, return the corresponding enum object.
     *
     * @param roleName name of the role
     * @return role object
     */
    public static OrganizationRole getByRoleName(String roleName) {
        OrganizationRole role = roleNameMap.get(roleName);
        if (role == null) {
            throw new IllegalArgumentException("No role with this name found.");
        }
        return role;
    }
}
