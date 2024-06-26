package com.moonspoon.moonspoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication
//시큐리티 일시중지
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MoonspoonApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoonspoonApplication.class, args);
	}

}
