package co.com.crediya.autenticacion.r2dbc.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para UsuarioEntity")
class UsuarioEntityTest {

    @Test
    @DisplayName("Debe crear una entidad con constructor sin argumentos")
    void debeCrearEntidadConConstructorSinArgumentos() {
        // Act
        UsuarioEntity entity = new UsuarioEntity();

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getNombres());
        assertNull(entity.getApellidos());
        assertNull(entity.getTelefono());
        assertNull(entity.getFechaNacimiento());
        assertNull(entity.getDireccion());
        assertNull(entity.getEmail());
        assertNull(entity.getDocumentoIdentidad());
        assertNull(entity.getIdRol());
        assertNull(entity.getSalario());
        assertNull(entity.getCreado());
    }

    @Test
    @DisplayName("Debe crear una entidad con constructor con todos los argumentos")
    void debeCrearEntidadConConstructorConTodosLosArgumentos() {
        // Arrange
        Long id = 1L;
        String nombres = "Juan Carlos";
        String apellidos = "Pérez García";
        String telefono = "+573001234567";
        LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
        String direccion = "Calle Falsa 123";
        String email = "juan.perez@example.com";
        String documentoIdentidad = "1234567890";
        Long idRol = 2L;
        BigDecimal salario = new BigDecimal("2500000");
        Instant creado = Instant.now();

        // Act
        UsuarioEntity entity = new UsuarioEntity(
                id, nombres, apellidos, telefono, fechaNacimiento,
                direccion, email, documentoIdentidad, idRol, salario, creado
        );

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(nombres, entity.getNombres());
        assertEquals(apellidos, entity.getApellidos());
        assertEquals(telefono, entity.getTelefono());
        assertEquals(fechaNacimiento, entity.getFechaNacimiento());
        assertEquals(direccion, entity.getDireccion());
        assertEquals(email, entity.getEmail());
        assertEquals(documentoIdentidad, entity.getDocumentoIdentidad());
        assertEquals(idRol, entity.getIdRol());
        assertEquals(salario, entity.getSalario());
        assertEquals(creado, entity.getCreado());
    }

    @Test
    @DisplayName("Debe permitir modificar propiedades usando setters")
    void debePermitirModificarPropiedadesUsandoSetters() {
        // Arrange
        UsuarioEntity entity = new UsuarioEntity();
        Long nuevoId = 5L;
        String nuevoNombre = "María Elena";
        String nuevoApellido = "González López";
        String nuevoTelefono = "+573109876543";
        LocalDate nuevaFechaNacimiento = LocalDate.of(1985, 12, 25);
        String nuevaDireccion = "Carrera 15 #45-67";
        String nuevoEmail = "maria.gonzalez@example.com";
        String nuevoDocumento = "9876543210";
        Long nuevoIdRol = 1L;
        BigDecimal nuevoSalario = new BigDecimal("3500000");
        Instant nuevoCreado = Instant.now();

        // Act
        entity.setId(nuevoId);
        entity.setNombres(nuevoNombre);
        entity.setApellidos(nuevoApellido);
        entity.setTelefono(nuevoTelefono);
        entity.setFechaNacimiento(nuevaFechaNacimiento);
        entity.setDireccion(nuevaDireccion);
        entity.setEmail(nuevoEmail);
        entity.setDocumentoIdentidad(nuevoDocumento);
        entity.setIdRol(nuevoIdRol);
        entity.setSalario(nuevoSalario);
        entity.setCreado(nuevoCreado);

        // Assert
        assertEquals(nuevoId, entity.getId());
        assertEquals(nuevoNombre, entity.getNombres());
        assertEquals(nuevoApellido, entity.getApellidos());
        assertEquals(nuevoTelefono, entity.getTelefono());
        assertEquals(nuevaFechaNacimiento, entity.getFechaNacimiento());
        assertEquals(nuevaDireccion, entity.getDireccion());
        assertEquals(nuevoEmail, entity.getEmail());
        assertEquals(nuevoDocumento, entity.getDocumentoIdentidad());
        assertEquals(nuevoIdRol, entity.getIdRol());
        assertEquals(nuevoSalario, entity.getSalario());
        assertEquals(nuevoCreado, entity.getCreado());
    }

    @Test
    @DisplayName("Debe implementar equals y hashCode correctamente")
    void debeImplementarEqualsYHashCodeCorrectamente() {
        // Arrange
        LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
        BigDecimal salario = new BigDecimal("2500000");
        Instant creado = Instant.now();

        UsuarioEntity entity1 = new UsuarioEntity(
                1L, "Juan", "Pérez", "+573001234567", fechaNacimiento,
                "Calle 123", "juan@example.com", "1234567890", 2L, salario, creado
        );

        UsuarioEntity entity2 = new UsuarioEntity(
                1L, "Juan", "Pérez", "+573001234567", fechaNacimiento,
                "Calle 123", "juan@example.com", "1234567890", 2L, salario, creado
        );

        UsuarioEntity entity3 = new UsuarioEntity(
                2L, "Juan", "Pérez", "+573001234567", fechaNacimiento,
                "Calle 123", "juan@example.com", "1234567890", 2L, salario, creado
        );

        // Assert
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1, entity3);
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    @DisplayName("Debe implementar toString correctamente")
    void debeImplementarToStringCorrectamente() {
        // Arrange
        UsuarioEntity entity = new UsuarioEntity(
                1L, "Ana", "Martínez", "+573001234567", LocalDate.of(1990, 5, 15),
                "Calle 456", "ana@example.com", "9876543210", 1L, new BigDecimal("3000000"), Instant.now()
        );

        // Act
        String resultado = entity.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("Ana"));
        assertTrue(resultado.contains("Martínez"));
        assertTrue(resultado.contains("ana@example.com"));
        assertTrue(resultado.contains("UsuarioEntity"));
    }

    @Test
    @DisplayName("Debe manejar valores nulos correctamente")
    void debeManejarValoresNulosCorrectamente() {
        // Act
        UsuarioEntity entity = new UsuarioEntity(
                null, null, null, null, null,
                null, null, null, null, null, null
        );

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getNombres());
        assertNull(entity.getApellidos());
        assertNull(entity.getTelefono());
        assertNull(entity.getFechaNacimiento());
        assertNull(entity.getDireccion());
        assertNull(entity.getEmail());
        assertNull(entity.getDocumentoIdentidad());
        assertNull(entity.getIdRol());
        assertNull(entity.getSalario());
        assertNull(entity.getCreado());
    }

    @Test
    @DisplayName("Debe manejar salarios con decimales")
    void debeManejarSalariosConDecimales() {
        // Arrange
        BigDecimal salarioConDecimales = new BigDecimal("2500000.50");
        UsuarioEntity entity = new UsuarioEntity();

        // Act
        entity.setSalario(salarioConDecimales);

        // Assert
        assertEquals(salarioConDecimales, entity.getSalario());
        assertEquals(0, salarioConDecimales.compareTo(entity.getSalario()));
    }

    @Test
    @DisplayName("Debe manejar fechas de nacimiento límite")
    void debeManejarFechasDeNacimientoLimite() {
        // Arrange
        LocalDate fechaMinima = LocalDate.MIN;
        LocalDate fechaMaxima = LocalDate.MAX;
        UsuarioEntity entity1 = new UsuarioEntity();
        UsuarioEntity entity2 = new UsuarioEntity();

        // Act
        entity1.setFechaNacimiento(fechaMinima);
        entity2.setFechaNacimiento(fechaMaxima);

        // Assert
        assertEquals(fechaMinima, entity1.getFechaNacimiento());
        assertEquals(fechaMaxima, entity2.getFechaNacimiento());
    }

    @Test
    @DisplayName("Debe manejar emails con diferentes formatos válidos")
    void debeManejarEmailsConDiferentesFormatosValidos() {
        // Arrange
        String[] emailsValidos = {
            "test@example.com",
            "user.name@domain.co",
            "user+tag@example.org",
            "123@example.com"
        };

        // Act & Assert
        for (String email : emailsValidos) {
            UsuarioEntity entity = new UsuarioEntity();
            entity.setEmail(email);
            assertEquals(email, entity.getEmail());
        }
    }

    @Test
    @DisplayName("Debe manejar documentos de identidad largos")
    void debeManejarDocumentosDeIdentidadLargos() {
        // Arrange
        String documentoLargo = "1234567890123456789012345678901234567890";
        UsuarioEntity entity = new UsuarioEntity();

        // Act
        entity.setDocumentoIdentidad(documentoLargo);

        // Assert
        assertEquals(documentoLargo, entity.getDocumentoIdentidad());
    }
}
