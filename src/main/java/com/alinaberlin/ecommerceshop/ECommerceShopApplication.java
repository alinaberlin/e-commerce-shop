package com.alinaberlin.ecommerceshop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(servers =
        {
                @Server(url = "/", description = "Server URL")
        },
        info = @Info(title = "EShop API", version = "1.0", description = "Eshop Rest API"))
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class ECommerceShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceShopApplication.class, args);
    }

}
