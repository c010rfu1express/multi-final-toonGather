package com.multi.toonGather.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.multi.toonGather.cs.client")
public class toonGatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(toonGatherApplication.class, args);
	}

}
