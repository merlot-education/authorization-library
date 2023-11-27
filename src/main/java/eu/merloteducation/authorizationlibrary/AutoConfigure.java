package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.JwtAuthConverter;
import eu.merloteducation.authorizationlibrary.authorization.JwtAuthConverterProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan
public class AutoConfigure {
    @Bean
    @ConditionalOnMissingBean
    public JwtAuthConverter jwtAuthConverter() {
        return new JwtAuthConverter(new JwtAuthConverterProperties());
    }
}
