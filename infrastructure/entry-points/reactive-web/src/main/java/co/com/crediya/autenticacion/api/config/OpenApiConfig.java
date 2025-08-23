package co.com.crediya.autenticacion.api.config;

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
                        .title("API de Autenticación Crediya")
                        .version("1.0.0")
                        .description("API para el registro y autenticación de usuarios en el sistema Crediya.")
                        .contact(new Contact()
                                .name("Equipo Crediya")
                                .email("jors.castro@gmail.com")
                        )
                );
    }
}
