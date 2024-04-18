package eu.merloteducation.authorizationlibrary.authorization;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum OrganizationRole {
    ORG_LEG_REP("OrgLegRep"),
    FED_ADMIN("FedAdmin");

    private final String roleName;
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
        // convert the names (camelCase) to UPPER_SNAKE_CASE
        String upperSnakeCase = roleName
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toUpperCase();
        return OrganizationRole.valueOf(upperSnakeCase);
    }
}
