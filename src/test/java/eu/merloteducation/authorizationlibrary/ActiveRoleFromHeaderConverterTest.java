package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.ActiveRoleFromHeaderConverter;
import eu.merloteducation.authorizationlibrary.authorization.OrganizationRoleGrantedAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = { ActiveRoleFromHeaderConverter.class })
class ActiveRoleFromHeaderConverterTest {

    @Autowired
    ActiveRoleFromHeaderConverter activeRoleFromHeaderConverter;

    @Test
    void convertCorrectly() {

        OrganizationRoleGrantedAuthority converted = activeRoleFromHeaderConverter.convert("OrgLegRep_10");
        assertThat(converted.getOrganizationRole()).isEqualTo("OrgLegRep");
        assertThat(converted.getOrganizationId()).isEqualTo("10");
        assertThat(converted.getAuthority()).isEqualTo("ROLE_OrgLegRep_10");
    }

    @Test
    void convertFail() {

        String authorityString = "SomeRole_10";
        Exception e = assertThrows(IllegalArgumentException.class,
            () -> activeRoleFromHeaderConverter.convert(authorityString));
        assertThat(e.getMessage()).isEqualTo("Unknown organization role authority " + authorityString);
    }
}
