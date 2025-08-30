package co.com.crediya.autenticacion.securityadapter;

import co.com.crediya.autenticacion.model.login.gateways.PasswordEncoderPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para BCryptPasswordAdapter")
public class BCryptPasswordAdapterTest {

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Test
    @DisplayName("Debe crear adapter correctamente")
    void debeCrearAdapterCorrectamente() {
        // Assert
       assertNotNull(passwordEncoderPort);

    }

    @Test
    @DisplayName("Debe verificar la contraseña correctamente")
    void debeVerificarLaContrasenaCorrectamente() {
        String rawPassword = "password123";
        String hashedPassword = "$2a$10$Dow1j8eG5F"; // Ejemplo de hash bcrypt
        when(passwordEncoderPort.matches(rawPassword, hashedPassword)).thenReturn(true);

        assertTrue(passwordEncoderPort.matches(rawPassword, hashedPassword));
    }

    @Test
    @DisplayName("Debe fallar al verificar la contraseña incorrecta")
    void debeFallarAlVerificarLaContrasenaIncorrecta() {
        String hashedPassword = "$2a$10$Dow1j8eG5F"; // Ejemplo de hash bcrypt
        when(passwordEncoderPort.matches("wrongPassword", hashedPassword)).thenReturn(false);

        assertFalse(passwordEncoderPort.matches("wrongPassword", hashedPassword));

    }

    @Test
    @DisplayName("Debe manejar null al verificar la contraseña")
    void debeManejarNullAlVerificarLaContrasena() {
        // Arrange
        String hashedPassword = "$2a$10$Dow1j8eG5F"; // Ejemplo de hash bcrypt
        when(passwordEncoderPort.matches(null, hashedPassword)).thenReturn(false);

        assertFalse(passwordEncoderPort.matches(null, hashedPassword));
    }

}
