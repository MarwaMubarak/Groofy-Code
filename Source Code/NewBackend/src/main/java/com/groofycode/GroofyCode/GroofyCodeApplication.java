package com.groofycode.GroofyCode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GroofyCodeApplication {
	public static void main(String[] args) {
		SpringApplication.run(GroofyCodeApplication.class, args);
	}
}
