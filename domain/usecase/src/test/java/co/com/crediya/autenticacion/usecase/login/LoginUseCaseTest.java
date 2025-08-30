package co.com.crediya.autenticacion.usecase.login;

import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import co.com.crediya.autenticacion.model.login.gateways.PasswordEncoderPort;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.Instant;

import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para LoginUseCase")
public class LoginUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        loginUseCase = new LoginUseCase(loginRepository, usuarioRepository, passwordEncoderPort);
    }

   @Test
    @DisplayName("Debe autenticar un usuario y generar un token JWT")
    void debeAutenticarUsuarioYGenerarToken() {
       Usuario usuario = Usuario.builder()
               .id(1L)
               .email("test@correo.com")
               .password("passwordEncriptado")
               .nombres("Test")
               .apellidos("Usuario")
               .nombreRol("ADMIN")
               .build();

       when(usuarioRepository.findByEmail("test@correo.com"))
               .thenReturn(Mono.just(usuario));

       when(passwordEncoderPort.matches("passwordPlano", "passwordEncriptado"))
               .thenReturn(true);

       when(loginRepository.generate(anyString(), anyString(), anyString(), anyMap(), any(Instant.class)))
               .thenReturn(Mono.just("token-jwt"));

       StepVerifier.create(loginUseCase.login("test@correo.com", "passwordPlano"))
               .expectNext("token-jwt")
               .verifyComplete();
    }

    @Test
    @DisplayName("Debe fallar al autenticar con credenciales inválidas")
    void debeFallarAlAutenticarConCredencialesInvalidas() {
        when(usuarioRepository.findByEmail("test@example.com"))
                .thenReturn(Mono.empty());
        StepVerifier.create(loginUseCase.login("test@example.com", "wrongpassword"))
                .expectErrorMessage("Credenciales invalidas.")
                .verify();

    }

    @Test
    @DisplayName("Debe fallar al autenticar con contraseña incorrecta")
    void debeFallarAlAutenticarConContrasenaIncorrecta() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .email("test@example.com")
                .password("hashedPassword")
                .nombres("Test")
                .apellidos("User")
                .nombreRol("USER")
                .build();
        when(usuarioRepository.findByEmail("test@example.com"))
                .thenReturn(Mono.just(usuario));
        when(passwordEncoderPort.matches("wrongpassword", "hashedPassword"))
                .thenReturn(false);

        StepVerifier.create(loginUseCase.login("test@example.com", "wrongpassword"))
                .expectErrorMessage("Credenciales invalidas.")
                .verify();
    }

    @Test
    @DisplayName("Debe fallar al generar el token JWT")
    void debeFallarAlGenerarElTokenJWT() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .email("test@example.com")
                .password("hashedPassword")
                .nombres("Test")
                .apellidos("User")
                .nombreRol("USER")
                .build();
        when(usuarioRepository.findByEmail("test@example.com"))
                .thenReturn(Mono.just(usuario));
        when(passwordEncoderPort.matches("correctpassword", "hashedPassword"))
                .thenReturn(true);
        when(loginRepository.generate(anyString(), anyString(), anyString(), anyMap(), any(Instant.class)))
                .thenReturn(Mono.error(new RuntimeException("Error al generar el token")));
        StepVerifier.create(loginUseCase.login("test@example.com", "correctpassword"))
                .expectErrorMessage("Error al generar el token")
                .verify();
    }

}
