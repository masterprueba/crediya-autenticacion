package co.com.crediya.autenticacion.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void debeCrearBeanDeOpenAPI() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI api = config.customOpenAPI();
        assertNotNull(api);
        assertNotNull(api.getInfo());
        assertEquals("API de Autenticación Crediya", api.getInfo().getTitle());
    }
}


