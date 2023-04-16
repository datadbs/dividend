package com.zb.dividend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividendApplication {
	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(DividendApplication.class, args);

	}
}
