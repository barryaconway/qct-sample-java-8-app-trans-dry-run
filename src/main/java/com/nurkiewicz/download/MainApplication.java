package com.nurkiewicz.download;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class MainApplication {

	public static void main(String[] args) {
		Integer temp = Integer.valueOf("1234");
		SpringApplication.run(MainApplication.class, args);
	}
}
