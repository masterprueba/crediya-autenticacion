package co.com.crediya.autenticacion.model.usuario.exceptions;

import co.com.crediya.autenticacion.model.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para DomainException")
class DomainExceptionTest {


    @Test
    @DisplayName("Debe permitir ser lanzada y capturada")
    void debePermitirSerLanzadaYCapturada() {
        // Arrange
        String codigoEsperado = "PRUEBA_EXCEPCION";
        String mensajeEsperado = "Esta es una excepción de prueba";

        // Act & Assert
        DomainException excepcion = assertThrows(DomainException.class, () -> {
            throw new DomainException(codigoEsperado, mensajeEsperado);
        });

        assertEquals(codigoEsperado, excepcion.code());
        assertEquals(mensajeEsperado, excepcion.getMessage());
    }

}
