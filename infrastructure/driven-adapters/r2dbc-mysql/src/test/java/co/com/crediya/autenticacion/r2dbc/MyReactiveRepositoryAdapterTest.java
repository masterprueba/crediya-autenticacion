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
import org.springframework.dao.DataIntegrityViolationException;
import co.com.crediya.autenticacion.r2dbc.entity.UsuarioEntity;
import co.com.crediya.autenticacion.model.usuario.Usuario;


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
    @DisplayName("Debe guardar transaccionalmente y mapear correctamente")
    void debeGuardarTransaccionalmente() {
        Usuario usuario = Usuario.builder().email("a@b.com").build();
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(10L);
        when(mapper.map(eq(usuario), any())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario.toBuilder().id(10L).build());
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        StepVerifier.create(adapter.saveTransactional(usuario))
                .expectNextMatches(u -> u.getId() != null && u.getId() == 10L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe mapear error de integridad a DomainException")
    void debeMapearErrorDeIntegridad() {
        Usuario usuario = Usuario.builder().email("a@b.com").build();
        UsuarioEntity entity = new UsuarioEntity();
        when(mapper.map(eq(usuario), any())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.error(new DataIntegrityViolationException("dup")));
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        StepVerifier.create(adapter.saveTransactional(usuario))
                .expectError(co.com.crediya.autenticacion.model.usuario.exceptions.DomainException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe encontrar por email y mapear a dominio")
    void debeEncontrarPorEmailYMapear() {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(7L);
        entity.setEmail("x@y.com");
        when(repository.findByEmail("x@y.com")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(Usuario.builder().id(7L).email("x@y.com").build());

        StepVerifier.create(adapter.findByEmail("x@y.com"))
                .expectNextMatches(u -> u.getId() == 7L && u.getEmail().equals("x@y.com"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe verificar si existe por correo cuando el correo existe")
    void debeVerificarSiExistePorCorreoCuandoElCorreoExiste() {
        // Arrange
        String email = "juan.perez@example.com";
        when(repository.countByEmail(email)).thenReturn(Mono.just(1L));

        // Act & Assert
        StepVerifier.create(adapter.existsByCorreo(email))
                .expectNext(true)
                .verifyComplete();

        verify(repository).countByEmail(email);
    }

    @Test
    @DisplayName("Debe verificar si existe por correo cuando el correo no existe")
    void debeVerificarSiExistePorCorreoCuandoElCorreoNoExiste() {
        // Arrange
        String email = "nuevo.usuario@example.com";
        when(repository.countByEmail(email)).thenReturn(Mono.just(0L));

        // Act & Assert
        StepVerifier.create(adapter.existsByCorreo(email))
                .expectNext(false)
                .verifyComplete();

        verify(repository).countByEmail(email);
    }
}

