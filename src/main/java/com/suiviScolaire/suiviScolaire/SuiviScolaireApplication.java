package com.suiviScolaire.suiviScolaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SuiviScolaireApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuiviScolaireApplication.class, args);
	}

}
