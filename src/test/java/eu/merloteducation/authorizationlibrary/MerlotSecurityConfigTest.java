package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.JwtAuthConverter;
import eu.merloteducation.authorizationlibrary.authorization.JwtAuthConverterProperties;
import eu.merloteducation.authorizationlibrary.authorization.UserInfoOpaqueTokenIntrospector;
import eu.merloteducation.authorizationlibrary.config.MerlotSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {MerlotSecurityConfig.class, JwtAuthConverter.class, JwtAuthConverterProperties.class, UserInfoOpaqueTokenIntrospector.class})
class MerlotSecurityConfigTest {

    @Autowired
    private MerlotSecurityConfig merlotSecurityConfig;

    @MockBean
    private WebClient.Builder webClientBuilder;

    @Test
    void checkSecuritySetupMethodsAreCalled() throws Exception {
        HttpSecurity security = mock(HttpSecurity.class);
        merlotSecurityConfig.applySecurityConfig(security);
        verify(security, times(1)).oauth2ResourceServer(any());
        verify(security, times(1)).sessionManagement(any());
        verify(security, times(1)).cors(Customizer.withDefaults());
        verify(security, times(1)).headers(any());
    }
}
