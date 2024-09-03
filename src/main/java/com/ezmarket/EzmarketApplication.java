package com.ezmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EzmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(EzmarketApplication.class, args);
	}

}
