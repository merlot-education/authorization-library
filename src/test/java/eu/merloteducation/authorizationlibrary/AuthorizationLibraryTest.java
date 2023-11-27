package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.AuthorityChecker;
import eu.merloteducation.authorizationlibrary.authorization.JwtAuthConverter;
import eu.merloteducation.authorizationlibrary.authorization.JwtAuthConverterProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = { JwtAuthConverter.class , AuthorityChecker.class , JwtAuthConverterProperties.class})
class AuthorizationLibraryTest {

	@Autowired
	JwtAuthConverter jwtAuthConverter;

	@Autowired
	AuthorityChecker authorityChecker;

	@Test
	void contextLoads() {
	}

}
