package com.example.hands_on_java_springboot_jdbc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Hands On Java Springboot JDBC",
		version = "1.0",
		description = "Hands On Java Springboot JDBC"
	),
	servers = {
		@Server(
			url = "http://localhost:8080",
			description = "Local Server"
		)
	}
)
public class HandsOnJavaSpringbootJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(HandsOnJavaSpringbootJdbcApplication.class, args);
	}

}
