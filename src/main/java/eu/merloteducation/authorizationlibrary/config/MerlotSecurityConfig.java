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

package eu.merloteducation.authorizationlibrary.config;

import eu.merloteducation.authorizationlibrary.authorization.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@Component
public class MerlotSecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;

    public MerlotSecurityConfig(@Autowired JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    /**
     * Given an instance of the HttpSecurity, apply the common security configurations of project MERLOT.
     *
     * @param http HttpSecurity reference
     * @throws Exception issue with applying the security configuration
     */
    public void applySecurityConfig(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        http.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(
                Customizer.withDefaults());
        http.headers(
                headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    }
}
