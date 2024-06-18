package ru.stepup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
public class StepupCft2Application {

	public static void main(String[] args) {

		ConfigurableApplicationContext context=SpringApplication.run(StepupCft2Application.class, args);
		FileReader fileReader = context.getBean(FileReader.class);
		List<String> lines=fileReader.readFile();
		DatabaseWriter databaseWriter = context.getBean(DatabaseWriter.class);
		databaseWriter.saveData(lines);
		DatabaseReader databaseReader = context.getBean(DatabaseReader.class);
		databaseReader.readData();
	}

}

