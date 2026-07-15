package com.yuri.api_biblioteca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		return new OpenAPI()
				.info(new Info()
						.title("API Biblioteca")
						.version("1.0.0")
						.description("API REST para gerenciamento de livros.")
						.contact(new Contact()
								.name("Yuri Freitas")
								.url("https://github.com/YuriiFreitass")
						)
				);
	}
}