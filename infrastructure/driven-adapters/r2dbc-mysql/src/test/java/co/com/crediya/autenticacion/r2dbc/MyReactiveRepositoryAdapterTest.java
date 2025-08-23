package co.com.crediya.autenticacion.r2dbc;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.r2dbc.entity.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

    private Usuario usuarioDomain;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        adapter = new MyReactiveRepositoryAdapter(repository, mapper, transactionalOperator);

        usuarioDomain = Usuario.builder()
                .id(1L)
                .nombres("Juan Carlos")
                .apellidos("Pérez García")
                .telefono("+573001234567")
                .email("juan.perez@example.com")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .direccion("Calle Falsa 123")
                .documentoIdentidad("1234567890")
                .idRol(2L)
                .salario(new BigDecimal("2500000"))
                .creado(Instant.now())
                .build();

        usuarioEntity = new UsuarioEntity(
                1L,
                "Juan Carlos",
                "Pérez García",
                "+573001234567",
                LocalDate.of(1990, 5, 15),
                "Calle Falsa 123",
                "juan.perez@example.com",
                "1234567890",
                2L,
                new BigDecimal("2500000"),
                Instant.now()
        );
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

