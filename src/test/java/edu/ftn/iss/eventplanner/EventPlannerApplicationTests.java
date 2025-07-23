package edu.ftn.iss.eventplanner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EventPlannerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void usesH2Database(@Autowired DataSource dataSource) throws Exception {
		String dbUrl = dataSource.getConnection().getMetaData().getURL();

		assertTrue(dbUrl.contains("jdbc:h2"), "Expected H2 database, but got: " + dbUrl);
	}

}
