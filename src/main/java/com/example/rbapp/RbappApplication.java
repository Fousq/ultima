package com.example.rbapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RbappApplication {

	public static void main(String[] args) {
		SpringApplication.run(RbappApplication.class, args);
	}

}
