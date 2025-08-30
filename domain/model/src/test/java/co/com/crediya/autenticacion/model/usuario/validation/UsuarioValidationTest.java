package co.com.crediya.autenticacion.model.usuario.validation;


import co.com.crediya.autenticacion.model.exceptions.DomainException;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para la validación de la entidad Usuario")
public class UsuarioValidationTest {


    @Test
    @DisplayName("Debe lanzar DomainException si los nombres son nulos o vacíos")
    void debeLanzarDomainExceptionSiNombresNulosOVacios() {

        Usuario usuarioConNombresNulos = Usuario.builder()
                .nombres(null)
                .build();
        Usuario usuarioConNombresVacios = Usuario.builder()
                .nombres("   ")
                .build();

        assertThrows(DomainException.class, () -> {
            UsuarioValidations.nombresRequeridos().validate(usuarioConNombresNulos).block();
        });
        assertThrows(DomainException.class, () -> {
            UsuarioValidations.nombresRequeridos().validate(usuarioConNombresVacios).block();
        });
    }

    @Test
    @DisplayName("Debe lanzar DomainException si los apellidos son nulos o vacíos")
    void debeLanzarDomainExceptionSiApellidosNulosOVacios() {

        Usuario usuarioConApellidosNulos = Usuario.builder()
                .apellidos(null)
                .build();
        Usuario usuarioConApellidosVacios = Usuario.builder()
                .apellidos("   ")
                .build();

        assertThrows(DomainException.class, () -> {
            UsuarioValidations.apellidosRequeridos().validate(usuarioConApellidosNulos).block();
        });
        assertThrows(DomainException.class, () -> {
            UsuarioValidations.apellidosRequeridos().validate(usuarioConApellidosVacios).block();
        });
    }

    @Test
    @DisplayName("Debe lanzar DomainException si el email es nulo o vacío")
    void debeLanzarDomainExceptionSiEmailNuloOVacio() {
        Usuario usuarioConEmailNulo = Usuario.builder()
                .email(null)
                .build();
        Usuario usuarioConEmailVacio = Usuario.builder()
                .email("   ")
                .build();

        assertThrows(DomainException.class, () -> {
            UsuarioValidations.emailRequerido().validate(usuarioConEmailNulo).block();
        });
        assertThrows(DomainException.class, () -> {
            UsuarioValidations.emailRequerido().validate(usuarioConEmailVacio).block();
        });
    }

    @Test
    @DisplayName("Debe lanzar DomainException si el formato del email es inválido")
    void debeLanzarDomainExceptionSiFormatoEmailInvalido() {
        Usuario usuarioConEmailInvalido = Usuario.builder()
                .email("email-invalido")
                .build();
        assertThrows(DomainException.class, () -> {
            UsuarioValidations.formatoEmailValido().validate(usuarioConEmailInvalido).block();
        });
    }

    @Test
    @DisplayName("Debe lanzar DomainException si el salario está fuera del rango permitido")
    void debeLanzarDomainExceptionSiSalarioFueraDeRango() {
        Usuario usuarioConSalarioNegativo = Usuario.builder()
                .salario(new BigDecimal(-1000))
                .build();
        Usuario usuarioConSalarioExcesivo = Usuario.builder()
                .salario(new BigDecimal(20000000))
                .build();
        assertThrows(DomainException.class, () -> {
            UsuarioValidations.salarioEnRango().validate(usuarioConSalarioNegativo).block();
        });
        assertThrows(DomainException.class, () -> {
            UsuarioValidations.salarioEnRango().validate(usuarioConSalarioExcesivo).block();
        });
    }

    @Test
    @DisplayName("Debe validar correctamente un usuario válido")
    void debeValidarCorrectamenteUsuarioValido() {
        Usuario usuarioValido = Usuario.builder()
                .nombres("Juan")
                .apellidos("Pérez")
                .email("juan.perez@email.com")
                .salario(BigDecimal.TEN)
                .build();
        UsuarioValidations.completa().validate(usuarioValido).block();
    }
}
