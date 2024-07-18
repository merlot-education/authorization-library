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
class AuthorityCheckerTest {

    @Autowired
    AuthorityChecker authorityChecker;

    @Test
    void getAdministratedOrgaIdsCorrectly() {

        Set<String> actual = authorityChecker.getAdministratedOrgaIds(getTestAuthentication());

        assertThat(actual).containsExactly("30");
    }

    @Test
    void administratesOrganizationTrue(){
        assertThat(authorityChecker.administratesOrganization(getTestAuthentication(), "Participant:30")).isTrue();
        assertThat(authorityChecker.administratesOrganization(getTestAuthentication(), "30")).isTrue();
    }

    @Test
    void administratesOrganizationFalse(){
        assertThat(authorityChecker.administratesOrganization(getTestAuthentication(), "Participant:10")).isFalse();
        assertThat(authorityChecker.administratesOrganization(getTestAuthentication(), "20")).isFalse();
    }

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
                list.add(new OrganizationRoleGrantedAuthority("OrgLegRep_20"));
                list.add(new OrganizationRoleGrantedAuthority("FedAdmin_30"));
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
