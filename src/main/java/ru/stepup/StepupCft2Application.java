package ru.stepup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StepupCft2Application {

	public static void main(String[] args) {

		ConfigurableApplicationContext context=SpringApplication.run(StepupCft2Application.class, args);
		FileReader fileReader = context.getBean(FileReader.class);
		fileReader.readFile();
	}

}
