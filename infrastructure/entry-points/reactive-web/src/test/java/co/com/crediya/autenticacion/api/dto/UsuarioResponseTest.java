package co.com.crediya.autenticacion.api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioResponseTest {

    @Test
    void debeSerializarYDeserializar() throws Exception {
        UsuarioResponse resp = new UsuarioResponse("Juan Pérez", "juan@ejemplo.com", "123");
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(resp);
        assertTrue(json.contains("\"usuario\""));
        assertTrue(json.contains("\"email\""));
        assertTrue(json.contains("\"documento_identidad\""));

        UsuarioResponse back = mapper.readValue(json, UsuarioResponse.class);
        assertEquals(resp, back);
    }
}


