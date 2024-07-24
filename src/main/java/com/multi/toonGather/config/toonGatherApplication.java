package com.multi.toonGather.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.multi.toonGather.social.model.dao")
public class toonGatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(toonGatherApplication.class, args);
	}

}
