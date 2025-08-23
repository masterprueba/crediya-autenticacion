package co.com.crediya.autenticacion.api.config;

import co.com.crediya.autenticacion.api.Handler;
import co.com.crediya.autenticacion.api.RouterRest;
import co.com.crediya.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest(excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@Import({CorsConfig.class, SecurityHeadersConfig.class})
@Disabled // Deshabilitado permanentemente hasta que se agreguen las dependencias de seguridad
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Test
    void securityHeadersAndCorsShouldBeApplied() {
        webTestClient.options() // Usamos OPTIONS para probar cabeceras sin invocar el handler principal
                .uri("/api/v1/usuarios")
                .header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "POST")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff");
                // Otros headers pueden variar o no estar presentes en una respuesta 404
    }

}