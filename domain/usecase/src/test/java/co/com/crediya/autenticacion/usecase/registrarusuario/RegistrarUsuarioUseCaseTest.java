package co.com.crediya.autenticacion.usecase.registrarusuario;

import co.com.crediya.autenticacion.model.login.gateways.PasswordEncoderPort;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.exceptions.DomainException;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para RegistrarUsuarioUseCase")
class RegistrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(usuarioRepository);
        
        registrarUsuarioUseCase = new RegistrarUsuarioUseCase(usuarioRepository, passwordEncoderPort);

        usuarioValido = Usuario.builder()
                .nombres("Juan Carlos")
                .apellidos("Pérez García")
                .telefono("+573001234567")
                .email("juan.perez@example.com")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .direccion("Calle Falsa 123")
                .documentoIdentidad("1234567890")
                .idRol(2L)
                .salario(new BigDecimal("2500000"))
                .build();
    }

    @Test
    @DisplayName("Debe registrar usuario exitosamente cuando es válido y el correo no existe")
    void debeRegistrarUsuarioExitosamenteCuandoEsValidoYElCorreoNoExiste() {
        // Arrange
        Usuario usuarioGuardado = usuarioValido.toBuilder().id(1L).password("asadad").build();
        
        when(usuarioRepository.existsByCorreo("juan.perez@example.com")).thenReturn(Mono.just(false));
        when(usuarioRepository.saveTransactional(any(Usuario.class))).thenReturn(Mono.just(usuarioGuardado));

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioValido))
                .expectNext(usuarioGuardado)
                .verifyComplete();

        verify(usuarioRepository).existsByCorreo("juan.perez@example.com");
        verify(usuarioRepository).saveTransactional(usuarioValido);
    }

    @Test
    @DisplayName("Debe fallar cuando el correo ya existe")
    void debeFallarCuandoElCorreoYaExiste() {
        // Arrange
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioValido))
                .expectError(DomainException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe fallar cuando los nombres están vacíos")
    void debeFallarCuandoLosNombresEstanVacios() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .nombres("")
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectErrorMatches(error -> 
                    error instanceof DomainException && 
                    error.getMessage().equals("Los nombres son requeridos"))
                .verify();

        verify(usuarioRepository, never()).existsByCorreo(anyString());
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe fallar cuando los nombres son nulos")
    void debeFallarCuandoLosNombresSonNulos() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .nombres(null)
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectErrorMatches(error -> 
                    error instanceof DomainException && 
                    error.getMessage().equals("Los nombres son requeridos"))
                .verify();

        verify(usuarioRepository, never()).existsByCorreo(anyString());
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe fallar cuando los apellidos están vacíos")
    void debeFallarCuandoLosApellidosEstanVacios() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .apellidos("   ")
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectErrorMatches(error -> 
                    error instanceof DomainException && 
                    error.getMessage().equals("Los apellidos son requeridos"))
                .verify();

        verify(usuarioRepository, never()).existsByCorreo(anyString());
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe fallar cuando los apellidos son nulos")
    void debeFallarCuandoLosApellidosSonNulos() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .apellidos(null)
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectErrorMatches(error -> 
                    error instanceof DomainException && 
                    error.getMessage().equals("Los apellidos son requeridos"))
                .verify();

        verify(usuarioRepository, never()).existsByCorreo(anyString());
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el email es nulo")
    void debeFallarCuandoElEmailEsNulo() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .email(null)
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectErrorMatches(error -> 
                    error instanceof DomainException && 
                    error.getMessage().equals("El campo 'correo_electronico' es requerido."))
                .verify();

        verify(usuarioRepository, never()).existsByCorreo(anyString());
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el email está vacío")
    void debeFallarCuandoElEmailEstaVacio() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .email("   ")
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectErrorMatches(error -> 
                    error instanceof DomainException && 
                    error.getMessage().equals("El campo 'correo_electronico' es requerido."))
                .verify();

        verify(usuarioRepository, never()).existsByCorreo(anyString());
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el formato del email es inválido")
    void debeFallarCuandoElFormatoDelEmailEsInvalido() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .email("email-invalido")
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectError(DomainException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe fallar cuando el salario es nulo")
    void debeFallarCuandoElSalarioEsNulo() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .salario(null)
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectErrorMatches(error -> 
                    error instanceof DomainException && 
                    error.getMessage().equals("El salario base debe estar entre 0 y 15,000,000"))
                .verify();

        verify(usuarioRepository, never()).existsByCorreo(anyString());
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el salario es negativo")
    void debeFallarCuandoElSalarioEsNegativo() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .salario(new BigDecimal("-1000"))
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectErrorMatches(error -> 
                    error instanceof DomainException && 
                    error.getMessage().equals("El salario base debe estar entre 0 y 15,000,000"))
                .verify();

        verify(usuarioRepository, never()).existsByCorreo(anyString());
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el salario excede el límite máximo")
    void debeFallarCuandoElSalarioExcedeElLimiteMaximo() {
        // Arrange
        Usuario usuarioInvalido = usuarioValido.toBuilder()
                .salario(new BigDecimal("15000001"))
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioInvalido))
                .expectErrorMatches(error -> 
                    error instanceof DomainException && 
                    error.getMessage().equals("El salario base debe estar entre 0 y 15,000,000"))
                .verify();

        verify(usuarioRepository, never()).existsByCorreo(anyString());
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe permitir salario en el límite mínimo (0)")
    void debePermitirSalarioEnElLimiteMinimo() {
        // Arrange
        Usuario usuarioConSalarioMinimo = usuarioValido.toBuilder()
                .salario(BigDecimal.ZERO)
                .password("NuevoP@ssw0rd1")
                .build();

        Usuario usuarioGuardado = usuarioConSalarioMinimo.toBuilder().id(1L).build();
        
        when(usuarioRepository.existsByCorreo("juan.perez@example.com")).thenReturn(Mono.just(false));
        when(usuarioRepository.saveTransactional(any(Usuario.class))).thenReturn(Mono.just(usuarioGuardado));

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioConSalarioMinimo))
                .expectNext(usuarioGuardado)
                .verifyComplete();

        verify(usuarioRepository).existsByCorreo("juan.perez@example.com");
        verify(usuarioRepository).saveTransactional(usuarioConSalarioMinimo);
    }

    @Test
    @DisplayName("Debe permitir salario en el límite máximo (15,000,000)")
    void debePermitirSalarioEnElLimiteMaximo() {
        // Arrange
        Usuario usuarioConSalarioMaximo = usuarioValido.toBuilder()
                .salario(new BigDecimal("15000000"))
                .password("NewP@ssw0rd1")
                .build();

        Usuario usuarioGuardado = usuarioConSalarioMaximo.toBuilder().id(1L).build();
        
        when(usuarioRepository.existsByCorreo("juan.perez@example.com")).thenReturn(Mono.just(false));
        when(usuarioRepository.saveTransactional(any(Usuario.class))).thenReturn(Mono.just(usuarioGuardado));

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioConSalarioMaximo))
                .expectNext(usuarioGuardado)
                .verifyComplete();

        verify(usuarioRepository).existsByCorreo("juan.perez@example.com");
        verify(usuarioRepository).saveTransactional(usuarioConSalarioMaximo);
    }

    @Test
    @DisplayName("Debe validar correctamente varios formatos de email válidos")
    void debeValidarCorrectamenteVariosFormatosDeEmailValidos() {
        // Arrange
        String[] emailsValidos = {
            "test@example.com",
            "user.name@domain.co",
            "user+tag@example.org",
            "first.last@subdomain.example.com",
            "123@example.com",
            "user_name@example-domain.com"
        };

        for (String email : emailsValidos) {
            Usuario usuarioConEmail = usuarioValido.toBuilder()
                    .email(email)
                    .password("ValidP@ss1")
                    .build();

            Usuario usuarioGuardado = usuarioConEmail.toBuilder().id(1L).build();
            
            when(usuarioRepository.existsByCorreo(email)).thenReturn(Mono.just(false));
            when(usuarioRepository.saveTransactional(usuarioConEmail)).thenReturn(Mono.just(usuarioGuardado));

            // Act & Assert
            StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioConEmail))
                    .expectNext(usuarioGuardado)
                    .verifyComplete();
        }
    }

    @Test
    @DisplayName("Debe rechazar formato de email sin arroba")
    void debeRechazarFormatoDeEmailSinArroba() {
        // Arrange
        Usuario usuarioConEmailInvalido = usuarioValido.toBuilder()
                .email("email-sin-arroba.com")
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioConEmailInvalido))
                .expectError(DomainException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe rechazar formato de email que empieza con arroba")
    void debeRechazarFormatoDeEmailQueEmpiezaConArroba() {
        // Arrange
        Usuario usuarioConEmailInvalido = usuarioValido.toBuilder()
                .email("@example.com")
                .build();

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioConEmailInvalido))
                .expectError(DomainException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe propagar errores del repositorio")
    void debePropagarErroresDelRepositorio() {
        // Arrange
        when(usuarioRepository.existsByCorreo("juan.perez@example.com"))
                .thenReturn(Mono.error(new RuntimeException("Error de conexión a base de datos")));

        // Act & Assert
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioValido))
                .expectErrorMatches(error -> 
                    error instanceof RuntimeException && 
                    error.getMessage().equals("Error de conexión a base de datos"))
                .verify();

        verify(usuarioRepository).existsByCorreo("juan.perez@example.com");
        verify(usuarioRepository, never()).saveTransactional(any(Usuario.class));
    }
}
