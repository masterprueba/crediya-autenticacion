package co.com.crediya.autenticacion.api;

import co.com.crediya.autenticacion.api.dto.RegistrarUsuarioRequest;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Test
    void testRegistrarUsuario() {
        var request = new RegistrarUsuarioRequest(
                "Test", "User",
                "3125856984","test.user@example.com","123416546546", 1L,new BigDecimal("50000")
        );

        var usuarioGuardado = new Usuario();
        usuarioGuardado.setId(1L);

        Mockito.when(registrarUsuarioUseCase.ejecutar(Mockito.any(Usuario.class)))
               .thenReturn(Mono.just(usuarioGuardado));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/api/v1/usuarios/1");
    }
}
