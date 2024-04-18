package eu.merloteducation.authorizationlibrary;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@PropertySource("classpath:authorization-library.properties")
@ComponentScan
public class AutoConfigure {
    // intentionally left empty
}
