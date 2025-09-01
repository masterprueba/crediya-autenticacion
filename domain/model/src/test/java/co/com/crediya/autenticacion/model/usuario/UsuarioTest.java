package co.com.crediya.autenticacion.model.usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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
                .nombreRol("ADMIN")
                .password("as")
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
        assertEquals("ADMIN", usuario.getNombreRol());
        assertEquals("as", usuario.getPassword());
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
                "CLIENTE",
                salario,
                ahora,"prueba1234"
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
        assertEquals("CLIENTE", usuario.getNombreRol());
        assertEquals("prueba1234", usuario.getPassword());
    }

    @Test
    @DisplayName("Debe permitir modificar campos con setters")
    void  debePermitirModificarCamposConSetters() {

        LocalDate fechaNacimiento = LocalDate.of(1985, 12, 25);
        BigDecimal salario = new BigDecimal("3500000");
        Instant ahora = Instant.now();

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
                "CLIENTE",
                salario,
                ahora,"prueba1234"
        );

        usuario.setId(2L);
        usuario.setNombres("Carlos Andrés");
        usuario.setApellidos("Ramírez Torres");
        usuario.setTelefono("+573112233445");
        usuario.setEmail("carlos@email.com");
        usuario.setFechaNacimiento(LocalDate.of(1995, 7, 20));
        usuario.setDireccion("Avenida Siempre Viva 742, Cali");
        usuario.setDocumentoIdentidad("1122334455");
        usuario.setIdRol(3L);
        usuario.setNombreRol("USER");
        usuario.setSalario(new BigDecimal("1500000"));
        usuario.setPassword("password123");

        assertNotNull(usuario);
        assertEquals(2L, usuario.getId(), "El ID inicial debe ser 2L");
        assertEquals("Carlos Andrés", usuario.getNombres());
        assertEquals("Ramírez Torres", usuario.getApellidos());
        assertEquals("+573112233445", usuario.getTelefono());
        assertEquals("carlos@email.com", usuario.getEmail());
        assertEquals(LocalDate.of(1995, 7, 20), usuario.getFechaNacimiento());
        assertEquals("Avenida Siempre Viva 742, Cali", usuario.getDireccion());
        assertEquals("1122334455", usuario.getDocumentoIdentidad());
        assertEquals(3L, usuario.getIdRol());
        assertEquals("USER", usuario.getNombreRol());
        assertEquals(new BigDecimal("1500000"), usuario.getSalario());
        assertNotNull(usuario.getCreado());
        assertEquals("password123", usuario.getPassword());

    }

    @Test
    @DisplayName("Debe clonar y modificar usuario con toBuilder")
    void debeClonarYModificarUsuarioConToBuilder() {
        Usuario original = Usuario.builder()
                .id(10L)
                .nombres("Pedro")
                .apellidos("López")
                .build();

        Usuario modificado = original.toBuilder()
                .nombres("Pedro Modificado")
                .build();

        assertEquals(10L, modificado.getId());
        assertEquals("Pedro Modificado", modificado.getNombres());
        assertEquals("López", modificado.getApellidos());
        assertNotEquals(original.getNombres(), modificado.getNombres());
    }

    @Test
    void debeTenerMetodosGetter(){
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombres("Juan")
                .apellidos("Pérez")
                .telefono("+573001234567")
                .email("juan.perez@email.com")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123")
                .documentoIdentidad("123456789")
                .idRol(2L)
                .nombreRol("ADMIN")
                .salario(new BigDecimal("3000000"))
                .creado(Instant.now())
                .password("securePassword")
                .build();

        assertEquals(1L, usuario.getId());
        assertEquals("Juan", usuario.getNombres());
        assertEquals("Pérez", usuario.getApellidos());
        assertEquals("+573001234567", usuario.getTelefono());
        assertEquals("juan.perez@email.com", usuario.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), usuario.getFechaNacimiento());
        assertEquals("Calle 123", usuario.getDireccion());
        assertEquals("123456789", usuario.getDocumentoIdentidad());
        assertEquals(2L, usuario.getIdRol());
        assertEquals("ADMIN", usuario.getNombreRol());
        assertEquals(new BigDecimal("3000000"), usuario.getSalario());
        assertNotNull(usuario.getCreado());
        assertEquals("securePassword", usuario.getPassword());
    }


}
