package xyz.nagendra.camundaplayground;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class CamundaPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaPlaygroundApplication.class, args);
	}

}
