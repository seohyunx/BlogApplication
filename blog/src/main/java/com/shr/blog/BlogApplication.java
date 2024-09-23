package com.shr.blog;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println("GOOGLE_CLIENT_ID: " + System.getenv("GOOGLE_CLIENT_ID"));
		System.out.println("GOOGLE_CLIENT_SECRET: " + System.getenv("GOOGLE_CLIENT_SECRET"));
	}
}