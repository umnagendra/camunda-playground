package xyz.nagendra.camundaplayground;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableProcessApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class CamundaPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaPlaygroundApplication.class, args);
	}

}
