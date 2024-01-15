package com.example.soalab2server1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableJpaAuditing
@SpringBootApplication
@EnableDiscoveryClient
public class SoaLab2Server1Application {

	public static void main(String[] args) {
		SpringApplication.run(SoaLab2Server1Application.class, args);
	}

}
