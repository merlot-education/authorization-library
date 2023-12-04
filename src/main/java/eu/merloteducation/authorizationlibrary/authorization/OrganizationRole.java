package eu.merloteducation.authorizationlibrary.authorization;

import lombok.Getter;

@Getter
public enum OrganizationRole {
    ORG_LEG_REP("OrgLegRep"),
    FED_ADMIN("FedAdmin");

    private final String roleName;
    OrganizationRole(String roleName) {
        this.roleName = roleName;
    }
}
