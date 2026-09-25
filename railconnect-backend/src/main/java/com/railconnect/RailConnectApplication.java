package com.railconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.railconnect")
@EntityScan(basePackages = "com.railconnect")
@EnableJpaRepositories(basePackages = "com.railconnect")
@EnableScheduling
public class RailConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RailConnectApplication.class, args);
        System.out.println("=================================================================");
        System.out.println("  RAILCONNECT SYSTEM INITIALIZED AND READY ON http://localhost:8080");
        System.out.println("  Intelligent Railway Booking, Digital Travel Pass & Journey Assistance");
        System.out.println("=================================================================");
    }
}
