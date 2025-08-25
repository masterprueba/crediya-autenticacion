package co.com.crediya.autenticacion.api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para RegistrarUsuarioRequest")
class RegistrarUsuarioRequestTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Para manejar LocalDate
    }

    @Test
    @DisplayName("Debe crear request con todos los campos")
    void debeCrearRequestConTodosLosCampos() {
        // Arrange
        String nombres = "Juan Carlos";
        String apellidos = "Pérez García";
        String telefono = "+573001234567";
        LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
        String direccion = "Calle Falsa 123";
        String correoElectronico = "juan.perez@example.com";
        String documentoIdentidad = "1234567890";
        Long rol = 2L;
        BigDecimal salarioBase = new BigDecimal("2500000");

        // Act
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                nombres, apellidos, telefono, fechaNacimiento, direccion,
                correoElectronico, documentoIdentidad, rol, salarioBase
        );

        // Assert
        assertNotNull(request);
        assertEquals(nombres, request.nombres());
        assertEquals(apellidos, request.apellidos());
        assertEquals(telefono, request.telefono());
        assertEquals(fechaNacimiento, request.fecha_nacimiento());
        assertEquals(direccion, request.direccion());
        assertEquals(correoElectronico, request.correo_electronico());
        assertEquals(documentoIdentidad, request.documento_identidad());
        assertEquals(rol, request.rol());
        assertEquals(salarioBase, request.salario_base());
    }

    @Test
    @DisplayName("Debe crear request con campos nulos")
    void debeCrearRequestConCamposNulos() {
        // Act
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                null, null, null, null, null, null, null, null, null
        );

        // Assert
        assertNotNull(request);
        assertNull(request.nombres());
        assertNull(request.apellidos());
        assertNull(request.telefono());
        assertNull(request.fecha_nacimiento());
        assertNull(request.direccion());
        assertNull(request.correo_electronico());
        assertNull(request.documento_identidad());
        assertNull(request.rol());
        assertNull(request.salario_base());
    }

    @Test
    @DisplayName("Debe serializar a JSON correctamente")
    void debeSerializarAJsonCorrectamente() throws Exception {
        // Arrange
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                "Ana", "García", "+573001234567", LocalDate.of(1985, 12, 25),
                "Carrera 15 #45-67", "ana@example.com", "9876543210", 1L, new BigDecimal("3000000")
        );

        // Act
        String json = objectMapper.writeValueAsString(request);

        // Assert
        assertNotNull(json);
        assertTrue(json.contains("\"nombres\":\"Ana\""));
        assertTrue(json.contains("\"apellidos\":\"García\""));
        assertTrue(json.contains("\"telefono\":\"+573001234567\""));
        assertTrue(json.contains("\"fecha_nacimiento\":[1985,12,25]"));
        assertTrue(json.contains("\"direccion\":\"Carrera 15 #45-67\""));
        assertTrue(json.contains("\"correo_electronico\":\"ana@example.com\""));
        assertTrue(json.contains("\"documento_identidad\":\"9876543210\""));
        assertTrue(json.contains("\"rol\":1"));
        assertTrue(json.contains("\"salario_base\":3000000"));
    }

    @Test
    @DisplayName("Debe deserializar desde JSON correctamente")
    void debeDeserializarDesdeJsonCorrectamente() throws Exception {
        // Arrange
        String json = """
            {
                "nombres": "Pedro",
                "apellidos": "Rodríguez",
                "telefono": "+573201234567",
                "fecha_nacimiento": "1992-08-10",
                "direccion": "Calle 123 #45-67",
                "correo_electronico": "pedro@example.com",
                "documento_identidad": "1122334455",
                "rol": 2,
                "salario_base": 4500000
            }
            """;

        // Act
        RegistrarUsuarioRequest request = objectMapper.readValue(json, RegistrarUsuarioRequest.class);

        // Assert
        assertNotNull(request);
        assertEquals("Pedro", request.nombres());
        assertEquals("Rodríguez", request.apellidos());
        assertEquals("+573201234567", request.telefono());
        assertEquals(LocalDate.of(1992, 8, 10), request.fecha_nacimiento());
        assertEquals("Calle 123 #45-67", request.direccion());
        assertEquals("pedro@example.com", request.correo_electronico());
        assertEquals("1122334455", request.documento_identidad());
        assertEquals(2L, request.rol());
        assertEquals(new BigDecimal("4500000"), request.salario_base());
    }

    @Test
    @DisplayName("Debe manejar JSON con campos faltantes")
    void debeManejarJsonConCamposFaltantes() throws Exception {
        // Arrange
        String json = """
            {
                "nombres": "Carlos",
                "apellidos": "López",
                "correo_electronico": "carlos@example.com",
                "salario_base": 2000000
            }
            """;

        // Act
        RegistrarUsuarioRequest request = objectMapper.readValue(json, RegistrarUsuarioRequest.class);

        // Assert
        assertNotNull(request);
        assertEquals("Carlos", request.nombres());
        assertEquals("López", request.apellidos());
        assertNull(request.telefono());
        assertNull(request.fecha_nacimiento());
        assertNull(request.direccion());
        assertEquals("carlos@example.com", request.correo_electronico());
        assertNull(request.documento_identidad());
        assertNull(request.rol());
        assertEquals(new BigDecimal("2000000"), request.salario_base());
    }

    @Test
    @DisplayName("Debe manejar JSON con valores nulos")
    void debeManejarJsonConValoresNulos() throws Exception {
        // Arrange
        String json = """
            {
                "nombres": "Laura",
                "apellidos": "Hernández",
                "telefono": null,
                "fecha_nacimiento": null,
                "direccion": null,
                "correo_electronico": "laura@example.com",
                "documento_identidad": null,
                "rol": null,
                "salario_base": 1800000
            }
            """;

        // Act
        RegistrarUsuarioRequest request = objectMapper.readValue(json, RegistrarUsuarioRequest.class);

        // Assert
        assertNotNull(request);
        assertEquals("Laura", request.nombres());
        assertEquals("Hernández", request.apellidos());
        assertNull(request.telefono());
        assertNull(request.fecha_nacimiento());
        assertNull(request.direccion());
        assertEquals("laura@example.com", request.correo_electronico());
        assertNull(request.documento_identidad());
        assertNull(request.rol());
        assertEquals(new BigDecimal("1800000"), request.salario_base());
    }

    @Test
    @DisplayName("Debe implementar equals correctamente")
    void debeImplementarEqualsCorrectamente() {
        // Arrange
        RegistrarUsuarioRequest request1 = new RegistrarUsuarioRequest(
                "Juan", "Pérez", "+573001234567", LocalDate.of(1990, 5, 15),
                "Calle 123", "juan@example.com", "1234567890", 2L, new BigDecimal("2500000")
        );

        RegistrarUsuarioRequest request2 = new RegistrarUsuarioRequest(
                "Juan", "Pérez", "+573001234567", LocalDate.of(1990, 5, 15),
                "Calle 123", "juan@example.com", "1234567890", 2L, new BigDecimal("2500000")
        );

        RegistrarUsuarioRequest request3 = new RegistrarUsuarioRequest(
                "Ana", "García", "+573001234567", LocalDate.of(1990, 5, 15),
                "Calle 123", "ana@example.com", "1234567890", 2L, new BigDecimal("2500000")
        );

        // Assert
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request2, request3);
    }

    @Test
    @DisplayName("Debe implementar hashCode correctamente")
    void debeImplementarHashCodeCorrectamente() {
        // Arrange
        RegistrarUsuarioRequest request1 = new RegistrarUsuarioRequest(
                "Juan", "Pérez", "+573001234567", LocalDate.of(1990, 5, 15),
                "Calle 123", "juan@example.com", "1234567890", 2L, new BigDecimal("2500000")
        );

        RegistrarUsuarioRequest request2 = new RegistrarUsuarioRequest(
                "Juan", "Pérez", "+573001234567", LocalDate.of(1990, 5, 15),
                "Calle 123", "juan@example.com", "1234567890", 2L, new BigDecimal("2500000")
        );

        // Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("Debe implementar toString correctamente")
    void debeImplementarToStringCorrectamente() {
        // Arrange
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                "Diego", "Morales", "+573001234567", LocalDate.of(1988, 3, 22),
                "Avenida 68 #40-50", "diego@example.com", "5566778899", 1L, new BigDecimal("3200000")
        );

        // Act
        String resultado = request.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("Diego"));
        assertTrue(resultado.contains("Morales"));
        assertTrue(resultado.contains("diego@example.com"));
        assertTrue(resultado.contains("RegistrarUsuarioRequest"));
    }
}
