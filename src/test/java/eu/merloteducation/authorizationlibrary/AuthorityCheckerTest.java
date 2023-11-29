package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.AuthorityChecker;
import eu.merloteducation.authorizationlibrary.authorization.OrganizationRoleGrantedAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = { AuthorityChecker.class })
public class AuthorityCheckerTest {

    @Autowired
    AuthorityChecker authorityChecker;

    @Test
    void getRepresentedOrgaIdsCorrectly() {

        Set<String> actual = authorityChecker.getRepresentedOrgaIds(getTestAuthentication());

        assertThat(actual).containsExactlyInAnyOrder("10", "20");
    }

    @Test
    void representsOrganizationTrue(){
        assertThat(authorityChecker.representsOrganization(getTestAuthentication(), "Participant:10")).isTrue();
        assertThat(authorityChecker.representsOrganization(getTestAuthentication(), "20")).isTrue();
    }

    @Test
    void representsOrganizationFalse(){
        assertThat(authorityChecker.representsOrganization(getTestAuthentication(), "Participant:30")).isFalse();
        assertThat(authorityChecker.representsOrganization(getTestAuthentication(), "40")).isFalse();
    }

    Authentication getTestAuthentication() {

        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {

                List<OrganizationRoleGrantedAuthority> list = new ArrayList<>();
                list.add(new OrganizationRoleGrantedAuthority("OrgLegRep_10"));
                list.add(new OrganizationRoleGrantedAuthority("OrgRep_20"));
                return list;
            }

            @Override
            public Object getCredentials() {

                return null;
            }

            @Override
            public Object getDetails() {

                return null;
            }

            @Override
            public Object getPrincipal() {

                return null;
            }

            @Override
            public boolean isAuthenticated() {

                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {

                return null;
            }
        };
    }
}
