package eu.merloteducation.authorizationlibrary.authorization;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class OrganizationRoleGrantedAuthority implements GrantedAuthority {

    private final String organizationRole;
    private final String organizationId;

    public OrganizationRoleGrantedAuthority(String authorityString) {
        String[] roleSplit = authorityString.split("_");
        if (roleSplit[0].equals(OrganizationRole.ORG_LEG_REP.getRoleName()) ||
                roleSplit[0].equals(OrganizationRole.FED_ADMIN.getRoleName())) {
            this.organizationRole = roleSplit[0];
            this.organizationId = authorityString.replace(this.organizationRole + "_", "");
        } else {
            throw new IllegalArgumentException("Unknown organization role authority " + authorityString);
        }
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + organizationRole + "_" + organizationId;
    }

    public boolean isRepresentative() {
        return this.organizationRole.equals(OrganizationRole.ORG_LEG_REP.getRoleName());
    }

    public boolean isFedAdmin() {
        return this.organizationRole.equals(OrganizationRole.FED_ADMIN.getRoleName());
    }
}
