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
        assertThat(converted.getOrganizationRole().getRoleName()).isEqualTo("OrgLegRep");
        assertThat(converted.getOrganizationId()).isEqualTo("10");
        assertThat(converted.getAuthority()).isEqualTo("ROLE_OrgLegRep_10");
    }

    @Test
    void convertFail() {

        String authorityString = "SomeRole_10";
        Exception e = assertThrows(IllegalArgumentException.class,
            () -> activeRoleFromHeaderConverter.convert(authorityString));
        assertThat(e.getMessage()).startsWith("No role with this name found.");
    }
}
