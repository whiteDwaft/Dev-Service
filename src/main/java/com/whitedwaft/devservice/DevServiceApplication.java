package com.whitedwaft.devservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.whitedwaft.devservice")
public class DevServiceApplication {

	public static void main(String[] args) {


		SpringApplication.run(DevServiceApplication.class, args);
	}

}
