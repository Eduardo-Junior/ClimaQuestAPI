package com.climaquest.cqapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("com.climaquest.cqapi.entity")               // força scan das entidades
@EnableJpaRepositories("com.climaquest.cqapi.repository")
public class CqapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CqapiApplication.class, args);
	}

}
