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

import eu.merloteducation.authorizationlibrary.authorization.SsiJwtAuthConverter;
import eu.merloteducation.authorizationlibrary.config.MerlotSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {MerlotSecurityConfig.class})
class MerlotSecurityConfigTest {

    @Autowired
    private MerlotSecurityConfig merlotSecurityConfig;

    @MockBean
    private SsiJwtAuthConverter ssiJwtAuthConverter;

    @Test
    void checkSecuritySetupMethodsAreCalled() throws Exception {
        HttpSecurity security = mock(HttpSecurity.class);
        merlotSecurityConfig.applySecurityConfig(security);
        verify(security, times(1)).oauth2ResourceServer(any());// ensure oauth config was called
        verify(security, times(1)).sessionManagement(any()); // session management activated
        verify(security, times(1)).cors(Customizer.withDefaults()); // default CORS
        verify(security, times(1)).headers(any()); // default CORS
    }
}
