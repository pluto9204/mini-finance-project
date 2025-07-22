package com.service.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
		log.debug("Hello");
	}

}
