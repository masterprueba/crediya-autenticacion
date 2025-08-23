package co.com.crediya.autenticacion.model.usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la entidad Usuario")
class UsuarioTest {

    @Test
    @DisplayName("Debe crear un usuario con builder correctamente")
    void debeCrearUsuarioConBuilderCorrectamente() {
        // Arrange
        LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
        BigDecimal salario = new BigDecimal("2500000");
        Instant ahora = Instant.now();

        // Act
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombres("Juan Carlos")
                .apellidos("Pérez García")
                .telefono("+573001234567")
                .email("juan.perez@example.com")
                .fechaNacimiento(fechaNacimiento)
                .direccion("Calle Falsa 123, Bogotá")
                .documentoIdentidad("1234567890")
                .idRol(2L)
                .salario(salario)
                .creado(ahora)
                .build();

        // Assert
        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("Juan Carlos", usuario.getNombres());
        assertEquals("Pérez García", usuario.getApellidos());
        assertEquals("+573001234567", usuario.getTelefono());
        assertEquals("juan.perez@example.com", usuario.getEmail());
        assertEquals(fechaNacimiento, usuario.getFechaNacimiento());
        assertEquals("Calle Falsa 123, Bogotá", usuario.getDireccion());
        assertEquals("1234567890", usuario.getDocumentoIdentidad());
        assertEquals(2L, usuario.getIdRol());
        assertEquals(salario, usuario.getSalario());
        assertEquals(ahora, usuario.getCreado());
    }

    @Test
    @DisplayName("Debe crear un usuario vacío con constructor sin argumentos")
    void debeCrearUsuarioVacioConConstructorSinArgumentos() {
        // Act
        Usuario usuario = new Usuario();

        // Assert
        assertNotNull(usuario);
        assertNull(usuario.getId());
        assertNull(usuario.getNombres());
        assertNull(usuario.getApellidos());
        assertNull(usuario.getTelefono());
        assertNull(usuario.getEmail());
        assertNull(usuario.getFechaNacimiento());
        assertNull(usuario.getDireccion());
        assertNull(usuario.getDocumentoIdentidad());
        assertNull(usuario.getIdRol());
        assertNull(usuario.getSalario());
        assertNull(usuario.getCreado());
    }

    @Test
    @DisplayName("Debe crear un usuario con constructor con todos los argumentos")
    void debeCrearUsuarioConConstructorConTodosLosArgumentos() {
        // Arrange
        LocalDate fechaNacimiento = LocalDate.of(1985, 12, 25);
        BigDecimal salario = new BigDecimal("3500000");
        Instant ahora = Instant.now();

        // Act
        Usuario usuario = new Usuario(
                2L,
                "María Elena",
                "González López",
                "+573109876543",
                "maria.gonzalez@example.com",
                fechaNacimiento,
                "Carrera 15 #45-67, Medellín",
                "9876543210",
                1L,
                salario,
                ahora
        );

        // Assert
        assertNotNull(usuario);
        assertEquals(2L, usuario.getId());
        assertEquals("María Elena", usuario.getNombres());
        assertEquals("González López", usuario.getApellidos());
        assertEquals("+573109876543", usuario.getTelefono());
        assertEquals("maria.gonzalez@example.com", usuario.getEmail());
        assertEquals(fechaNacimiento, usuario.getFechaNacimiento());
        assertEquals("Carrera 15 #45-67, Medellín", usuario.getDireccion());
        assertEquals("9876543210", usuario.getDocumentoIdentidad());
        assertEquals(1L, usuario.getIdRol());
        assertEquals(salario, usuario.getSalario());
        assertEquals(ahora, usuario.getCreado());
    }

    @Test
    @DisplayName("Debe permitir modificar un usuario usando toBuilder")
    void debePermitirModificarUsuarioUsandoToBuilder() {
        // Arrange
        Usuario usuarioOriginal = Usuario.builder()
                .id(1L)
                .nombres("Carlos")
                .apellidos("Rodríguez")
                .email("carlos@example.com")
                .salario(new BigDecimal("2000000"))
                .build();

        // Act
        Usuario usuarioModificado = usuarioOriginal.toBuilder()
                .nombres("Carlos Alberto")
                .salario(new BigDecimal("2500000"))
                .build();

        // Assert
        assertNotNull(usuarioModificado);
        assertEquals(1L, usuarioModificado.getId());
        assertEquals("Carlos Alberto", usuarioModificado.getNombres());
        assertEquals("Rodríguez", usuarioModificado.getApellidos());
        assertEquals("carlos@example.com", usuarioModificado.getEmail());
        assertEquals(new BigDecimal("2500000"), usuarioModificado.getSalario());
        
        // El usuario original no debe haberse modificado
        assertEquals("Carlos", usuarioOriginal.getNombres());
        assertEquals(new BigDecimal("2000000"), usuarioOriginal.getSalario());
    }

    @Test
    @DisplayName("Debe implementar equals y hashCode correctamente")
    void debeImplementarEqualsYHashCodeCorrectamente() {
        // Arrange
        Usuario usuario1 = Usuario.builder()
                .id(1L)
                .nombres("Ana")
                .apellidos("Martínez")
                .email("ana@example.com")
                .build();

        Usuario usuario2 = Usuario.builder()
                .id(1L)
                .nombres("Ana")
                .apellidos("Martínez")
                .email("ana@example.com")
                .build();

        Usuario usuario3 = Usuario.builder()
                .id(2L)
                .nombres("Ana")
                .apellidos("Martínez")
                .email("ana@example.com")
                .build();

        // Assert
        assertEquals(usuario1, usuario2);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
        assertNotEquals(usuario1, usuario3);
        assertNotEquals(usuario1.hashCode(), usuario3.hashCode());
    }

    @Test
    @DisplayName("Debe implementar toString correctamente")
    void debeImplementarToStringCorrectamente() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombres("Pedro")
                .apellidos("Silva")
                .email("pedro@example.com")
                .build();

        // Act
        String resultado = usuario.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("Pedro"));
        assertTrue(resultado.contains("Silva"));
        assertTrue(resultado.contains("pedro@example.com"));
        assertTrue(resultado.contains("Usuario"));
    }
}
