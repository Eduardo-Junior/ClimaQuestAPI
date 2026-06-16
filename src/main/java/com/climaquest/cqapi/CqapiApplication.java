package com.climaquest.cqapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.sentry.Sentry;
import java.lang.Exception;

@SpringBootApplication
@EnableScheduling
@EntityScan("com.climaquest.cqapi.entity")               // força scan das entidades
@EnableJpaRepositories("com.climaquest.cqapi.repository")
public class CqapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CqapiApplication.class, args);
		try {
			throw new Exception("This is a test.");
		} catch (Exception e) {
				Sentry.captureException(e);
		}
	}
}
