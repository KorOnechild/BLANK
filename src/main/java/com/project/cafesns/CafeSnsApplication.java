package com.project.cafesns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CafeSnsApplication {

	public static void main(String[] args) {
		System.setProperty("user.timezone", "Asia/Seoul");
		SpringApplication.run(CafeSnsApplication.class, args);
	}

}
