package co.com.crediya.autenticacion.api.mapper;

import co.com.crediya.autenticacion.api.dto.RegistrarUsuarioRequest;
import co.com.crediya.autenticacion.api.dto.UsuarioResponse;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    private final UsuarioMapper mapper = Mappers.getMapper(UsuarioMapper.class);

    @Test
    void debeMapearRequestADomain() {
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest(
                "Juan", "Pérez", "+573001", LocalDate.of(1990,1,1),
                "Calle", "juan@example.com", "123", 2L, new BigDecimal("1000"),"P@ssw0rd!"
        );

        Usuario usuario = mapper.toDomain(request);
        assertEquals("Juan", usuario.getNombres());
        assertEquals("Pérez", usuario.getApellidos());
        assertEquals("123", usuario.getDocumentoIdentidad());
        assertEquals(2L, usuario.getIdRol());
        assertEquals(new BigDecimal("1000"), usuario.getSalario());
        assertEquals(LocalDate.of(1990,1,1), usuario.getFechaNacimiento());
        assertEquals("juan@example.com", usuario.getEmail());
        assertEquals("P@ssw0rd!", usuario.getPassword());
    }

    @Test
    void debeMapearUsuarioAResponse() {
        Usuario usuario = Usuario.builder()
                .nombres("Ana")
                .apellidos("García")
                .email("ana@example.com")
                .documentoIdentidad("999")
                .build();

        UsuarioResponse response = mapper.toResponse(usuario);
        assertEquals("Ana García", response.nombreCompleto());
        assertEquals("ana@example.com", response.email());
        assertEquals("999", response.documentoIdentidad());
    }
}


