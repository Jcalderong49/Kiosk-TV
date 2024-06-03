package es.KioskTV;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * The main application class for KioskTV.
 */
@SpringBootApplication
public class KioskTvApplication {
	/**
	 * The entry point of the application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(KioskTvApplication.class, args);
	}

	/**
	 * Customizes the OpenAPI documentation for the KioskTV API.
	 *
	 * @return the customized OpenAPI instance
	 */
	@Bean
	public OpenAPI customAPI() {
		return new OpenAPI()
				.info(new Info().title("KioskTV API"))
				.addSecurityItem(new SecurityRequirement().addList("JavaInUseSecurityScheme"))
				.components(new Components().addSecuritySchemes("JavaInUseSecurityScheme", new SecurityScheme()
						.name("JavaInUseSecurityScheme")
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")));
	}
}