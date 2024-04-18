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
