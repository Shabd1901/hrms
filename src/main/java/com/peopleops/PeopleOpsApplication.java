package com.peopleops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Main entry point for the PeopleOps HRMS Spring Boot application.
// Initializes the Spring application context, auto-configurations, and embedded web server.
@SpringBootApplication
public class PeopleOpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeopleOpsApplication.class, args);
    }

}
