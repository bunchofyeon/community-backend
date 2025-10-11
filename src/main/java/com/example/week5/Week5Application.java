package com.example.week5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Week5Application {

	public static void main(String[] args) {
		SpringApplication.run(Week5Application.class, args);
	}

}
