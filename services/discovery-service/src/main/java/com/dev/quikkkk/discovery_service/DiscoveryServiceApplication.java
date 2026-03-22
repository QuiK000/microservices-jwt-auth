package com.dev.quikkkk.discovery_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(DiscoveryServiceApplication.class, args);
	}

}
