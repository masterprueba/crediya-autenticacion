package co.com.crediya.autenticacion.api;

import co.com.crediya.autenticacion.api.dto.RegistrarUsuarioRequest;
import co.com.crediya.autenticacion.api.mapper.UsuarioMapper;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.usecase.consultarusuario.ConsultarUsuarioUseCase;
import co.com.crediya.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios para Handler")
class HandlerTest {

    @Mock
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Mock
    private ConsultarUsuarioUseCase consultarUsuarioUseCase;

    private Handler handler;

    @BeforeEach
    void setUp() {
        UsuarioMapper usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
        handler = new Handler(registrarUsuarioUseCase, consultarUsuarioUseCase, usuarioMapper);
    }

    @Test
    @DisplayName("Debe crear Handler con use case")
    void debeCrearHandlerConUseCase() {
        // Assert
        assertNotNull(handler);
    }

    @Test
    @DisplayName("Debe mapear DTO a Usuario correctamente")
    void debeMapearDTOAUsuarioCorrectamente() throws Exception {
        // Arrange
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                "Juan Carlos",
                "Pérez García",
                "+573001234567",
                LocalDate.of(1990, 5, 15),
                "Calle Falsa 123",
                "juan.perez@example.com",
                "1234567890",
                2L,
                new BigDecimal("2500000")
        );

        // Act
        Usuario usuarioMapeado = invocarToDomain(request);

        // Assert
        assertNotNull(usuarioMapeado);
        assertNull(usuarioMapeado.getId()); // Debe ser null porque es un nuevo usuario
        assertEquals("Juan Carlos", usuarioMapeado.getNombres());
        assertEquals("Pérez García", usuarioMapeado.getApellidos());
        assertEquals("+573001234567", usuarioMapeado.getTelefono());
        assertEquals("juan.perez@example.com", usuarioMapeado.getEmail());
        assertEquals(LocalDate.of(1990, 5, 15), usuarioMapeado.getFechaNacimiento());
        assertEquals("Calle Falsa 123", usuarioMapeado.getDireccion());
        assertEquals("1234567890", usuarioMapeado.getDocumentoIdentidad());
        assertEquals(2L, usuarioMapeado.getIdRol()); // Se asigna por defecto
        assertEquals(new BigDecimal("2500000"), usuarioMapeado.getSalario());
        assertNotNull(usuarioMapeado.getCreado()); // Debe tener timestamp de creación
    }

    @Test
    @DisplayName("Debe mapear DTO con campos nulos")
    void debeMapearDTOConCamposNulos() throws Exception {
        // Arrange
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                null, null, null, null, null, null, null, null, null
        );

        // Act
        Usuario usuarioMapeado = invocarToDomain(request);

        // Assert
        assertNotNull(usuarioMapeado);
        assertNull(usuarioMapeado.getId());
        assertNull(usuarioMapeado.getNombres());
        assertNull(usuarioMapeado.getApellidos());
        assertNull(usuarioMapeado.getTelefono());
        assertNull(usuarioMapeado.getEmail());
        assertNull(usuarioMapeado.getFechaNacimiento());
        assertNull(usuarioMapeado.getDireccion());
        assertNull(usuarioMapeado.getDocumentoIdentidad());
        assertEquals(2L, usuarioMapeado.getIdRol()); // Se asigna por defecto
        assertNull(usuarioMapeado.getSalario());
        assertNotNull(usuarioMapeado.getCreado()); // Siempre se asigna
    }

    @Test
    @DisplayName("Debe asignar rol por defecto como 2L")
    void debeAsignarRolPorDefectoComo2L() throws Exception {
        // Arrange
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                "Ana", "García", "+573001234567", LocalDate.of(1990, 1, 1),
                "Calle 123", "ana@example.com", "1234567890", 1L, new BigDecimal("1000000")
        );

        // Act
        Usuario usuarioMapeado = invocarToDomain(request);

        // Assert
        assertEquals(2L, usuarioMapeado.getIdRol()); // Siempre asigna 2L independientemente del input
    }

    @Test
    @DisplayName("Debe asignar timestamp de creación automáticamente")
    void debeAsignarTimestampDeCreacionAutomaticamente() throws Exception {
        // Arrange
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                "Pedro", "Silva", null, null, null, "pedro@example.com", null, null, new BigDecimal("1500000")
        );

        // Act
        Usuario usuarioMapeado = invocarToDomain(request);

        // Assert
        assertNotNull(usuarioMapeado.getCreado());
        assertTrue(usuarioMapeado.getCreado().toEpochMilli() > 0);
    }

    @Test
    @DisplayName("Debe manejar diferentes tipos de datos correctamente")
    void debeManejarDiferentesTiposDeDatosCorrectamente() throws Exception {
        // Arrange
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                "María Elena",
                "González López",
                "+573109876543",
                LocalDate.of(1985, 12, 25),
                "Carrera 15 #45-67, Medellín",
                "maria.gonzalez@example.com",
                "9876543210",
                1L,
                new BigDecimal("5000000.50")
        );

        // Act
        Usuario usuarioMapeado = invocarToDomain(request);

        // Assert
        assertEquals("María Elena", usuarioMapeado.getNombres());
        assertEquals("González López", usuarioMapeado.getApellidos());
        assertEquals("+573109876543", usuarioMapeado.getTelefono());
        assertEquals(LocalDate.of(1985, 12, 25), usuarioMapeado.getFechaNacimiento());
        assertEquals("Carrera 15 #45-67, Medellín", usuarioMapeado.getDireccion());
        assertEquals("maria.gonzalez@example.com", usuarioMapeado.getEmail());
        assertEquals("9876543210", usuarioMapeado.getDocumentoIdentidad());
        assertEquals(new BigDecimal("5000000.50"), usuarioMapeado.getSalario());
    }

    private Usuario invocarToDomain(RegistrarUsuarioRequest request) throws Exception {
        Method method = Handler.class.getDeclaredMethod("toDomain", RegistrarUsuarioRequest.class);
        method.setAccessible(true);
        return (Usuario) method.invoke(handler, request);
    }
}