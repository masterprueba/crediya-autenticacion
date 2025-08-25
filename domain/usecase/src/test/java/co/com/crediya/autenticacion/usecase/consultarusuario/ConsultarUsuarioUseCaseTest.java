package co.com.crediya.autenticacion.usecase.consultarusuario;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.usuario.exceptions.DomainException;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ConsultarUsuarioUseCase")
class ConsultarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private ConsultarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConsultarUsuarioUseCase(usuarioRepository);
    }

    @Test
    @DisplayName("Debe devolver usuario con rol cliente")
    void debeDevolverUsuarioConRolCliente() {
        Usuario usuario = Usuario.builder().idRol(4L).email("a@b.com").build();
        when(usuarioRepository.findByEmail("a@b.com")).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.ejecutar("a@b.com"))
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe fallar si usuario no existe")
    void debeFallarSiUsuarioNoExiste() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.ejecutar("x@y.com"))
                .expectError(DomainException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe fallar si usuario no es cliente")
    void debeFallarSiUsuarioNoEsCliente() {
        Usuario usuario = Usuario.builder().idRol(2L).email("a@b.com").build();
        when(usuarioRepository.findByEmail("a@b.com")).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.ejecutar("a@b.com"))
                .expectError(DomainException.class)
                .verify();
    }
}


