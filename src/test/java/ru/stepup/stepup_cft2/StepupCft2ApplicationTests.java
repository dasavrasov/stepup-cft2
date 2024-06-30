package ru.stepup.stepup_cft2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;
import ru.stepup.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
class StepupCft2ApplicationTests {

	static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15-alpine");

	@Autowired
	private ApplicationContext context;

	@Autowired
	FileReader fileReader;

	@Autowired
	Checker<User> fioChecker;

	@Autowired
	Checker<Login> applicationTypeChecker;

	@Autowired
	Checker<Login> accessDateChecker;

	@Autowired
	DatabaseWriter databaseWriter;

	@Autowired
	DatabaseReader databaseReader;

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@Test
	void fileReaderTest() {
		fileReader.readFile();
		fileReader.readUsers();
		List<User> users = fileReader.getUsers();
		fileReader.readLogins();
		List<Login> logins = fileReader.getLogins();
		assertEquals(5,users.size());
		assertEquals(5,logins.size());
		assertTrue(users.stream().anyMatch(user -> user.getFio().equals("Саврасов Денис Алексеевич")));
		assertTrue(users.stream().anyMatch(user -> user.getFio().equals("ермаков Александр Иванович")));
		assertTrue(users.stream().anyMatch(user -> user.getFio().equals("иванов Иван Иванович")));
		assertTrue(logins.stream().anyMatch(user -> user.getApplication().equals("mobile")));
		assertTrue(logins.stream().anyMatch(user -> user.getApplication().equals("web")));
		assertTrue(logins.stream().anyMatch(user -> user.getApplication().equals("Sberbank")));
	}

	@Test
	void checkerTest() {
		fileReader.readFile();
		fileReader.readUsers();
		List<User> users = fileReader.getUsers();
		users=fioChecker.check(fileReader.getUsers());
		users.stream().forEach(user -> assertTrue(Character.isUpperCase(user.getFio().charAt(0))));
		fileReader.readLogins();
		List<Login> logins = fileReader.getLogins();
		logins=applicationTypeChecker.check(fileReader.getLogins());
		assertTrue(logins.stream().allMatch(login -> login.getApplication().equals("web") || login.getApplication().equals("mobile") || login.getApplication().startsWith("other:")));
		assertTrue(logins.stream().anyMatch(login -> login.getAccessDate() == null));
		logins=accessDateChecker.check(logins);
		assertTrue(logins.stream().noneMatch(login -> login.getAccessDate() == null));
	}

	@Test
	void databaseWriterTest() {
		fileReader.readFile();
		fileReader.readUsers();
		fileReader.readLogins();
		List<User> users=fioChecker.check(fileReader.getUsers());
		List<Login> logins=applicationTypeChecker.check(fileReader.getLogins());
		logins=accessDateChecker.check(logins);
		databaseWriter.writeUsers(users);
		databaseWriter.writeLogins(logins);
		List<User> usersFromDb = databaseReader.readAllUsers();
		List<Login> loginsFromDb = databaseReader.readAllLogins();
		users.forEach(user -> System.out.println(user.getUsername()+" "+user.getFio()));
		logins.forEach(login -> System.out.println(login.getUserId()+" "+login.getApplication()+" "+login.getAccessDate()));
		assertEquals(users.size(), usersFromDb.size());
		assertEquals(logins.size(), loginsFromDb.size());
	}


}
