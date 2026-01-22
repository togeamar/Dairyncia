package com.dairyncia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DairynciaSpringApplication {

	public static void main(String[] args) {
		System.out.println("========================================");
        System.out.println("APPLICATION STARTING - NEW VERSION");
        System.out.println("========================================");
		SpringApplication.run(DairynciaSpringApplication.class, args);
	}

}
