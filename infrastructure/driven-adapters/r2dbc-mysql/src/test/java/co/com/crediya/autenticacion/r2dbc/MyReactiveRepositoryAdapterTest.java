package co.com.crediya.autenticacion.r2dbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para MyReactiveRepositoryAdapter")
@SuppressWarnings("unchecked")
class MyReactiveRepositoryAdapterTest {

    @Mock
    private MyReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private TransactionalOperator transactionalOperator;

    private MyReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MyReactiveRepositoryAdapter(repository, mapper, transactionalOperator);
    }

    @Test
    @DisplayName("Debe crear adapter correctamente")
    void debeCrearAdapterCorrectamente() {
        // Assert
        assertNotNull(adapter);
    }

    @Test
    @DisplayName("Debe verificar si existe por correo cuando el correo existe")
    void debeVerificarSiExistePorCorreoCuandoElCorreoExiste() {
        // Arrange
        String email = "juan.perez@example.com";
        when(repository.countByCorreo(email)).thenReturn(Mono.just(1L));

        // Act & Assert
        StepVerifier.create(adapter.existsByCorreo(email))
                .expectNext(true)
                .verifyComplete();

        verify(repository).countByCorreo(email);
    }

    @Test
    @DisplayName("Debe verificar si existe por correo cuando el correo no existe")
    void debeVerificarSiExistePorCorreoCuandoElCorreoNoExiste() {
        // Arrange
        String email = "nuevo.usuario@example.com";
        when(repository.countByCorreo(email)).thenReturn(Mono.just(0L));

        // Act & Assert
        StepVerifier.create(adapter.existsByCorreo(email))
                .expectNext(false)
                .verifyComplete();

        verify(repository).countByCorreo(email);
    }
}

