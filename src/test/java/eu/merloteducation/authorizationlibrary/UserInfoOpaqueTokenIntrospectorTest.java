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

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import eu.merloteducation.authorizationlibrary.authorization.UserInfoOpaqueTokenIntrospector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
@WireMockTest(httpPort = 8102)
class UserInfoOpaqueTokenIntrospectorTest {

    @Autowired
    private UserInfoOpaqueTokenIntrospector userInfoOpaqueTokenIntrospector;

    @Test
    void getUserInfoClaimsSuccessful() {
        stubFor(get("/userinfo")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"some\": \"data\"}")));

        OAuth2AuthenticatedPrincipal principal = this.userInfoOpaqueTokenIntrospector.introspect("1234");
        assertEquals("data", principal.getAttribute("some"));
    }

    @Test
    void getUserInfoClaimsEmpty() {
        stubFor(get("/userinfo")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{}")));

        OAuth2AuthenticatedPrincipal principal = this.userInfoOpaqueTokenIntrospector.introspect("1234");
        assertNull(principal);
    }
}
