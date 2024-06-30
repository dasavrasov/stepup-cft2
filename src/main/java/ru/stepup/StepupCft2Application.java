package ru.stepup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class StepupCft2Application {

	public static void main(String[] args) {

		ConfigurableApplicationContext context=SpringApplication.run(StepupCft2Application.class, args);
		FileReader fileReader = context.getBean(FileReader.class);
		List<String> lines = fileReader.readFile();
		List<User> users = fileReader.readUsers(lines);
		List<Login> logins = fileReader.readLogins(lines, users);
		Checker<User> fioChecker = context.getBean(FioChecker.class);
		users=fioChecker.check(users);
		Checker<Login> applicationTypeChecker = context.getBean(ApplicationTypeChecker.class);
		logins=applicationTypeChecker.check(logins);
		Checker<Login> accessDateChecker = context.getBean(AccessDateChecker.class);
		logins=accessDateChecker.check(logins);
		DatabaseWriter databaseWriter = context.getBean(DatabaseWriter.class);
		databaseWriter.writeUsers(users);
		databaseWriter.writeLogins(logins);
		DatabaseReader databaseReader = context.getBean(DatabaseReader.class);
		users=databaseReader.readAllUsers();
		users.forEach(user -> System.out.println(user.getUsername()+" "+user.getFio()));
		logins=databaseReader.readAllLogins();
		logins.forEach(login -> System.out.println(login.getUserId()+" "+login.getApplication()+" "+login.getAccessDate()));
	}

}