package com.example.democicd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.democicd")
public class DemoCicdApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoCicdApplication.class, args);
	}

}
