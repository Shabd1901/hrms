package com.peopleops.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI peopleOpsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PeopleOps HRMS API")
                        .description("Human Resource Management System REST API")
                        .version("1.0"));
    }
}
