package co.com.crediya.autenticacion.securityadapter;

import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para LoginSecurityRepositoryAdapter")
class LoginSecurityRepositoryAdapterTest {

    @Mock
    private LoginRepository loginRepository;

    @Test
    void testGenerateToken() {
        String secret = "dGhpc2lzYXNlY3JldGtleWZvcnRlc3Rpbmc"; // Base64URL de "thisisasecretkeyfortesting"
        LoginSecurityRepositoryAdapter adapter = new LoginSecurityRepositoryAdapter(secret);
        String sub = "123";
        String email = "test@crediya.com";
        String rol = "USER";
        Map<String, Object> extra = Map.of("custom", "valor");
        Instant exp = Instant.now().plusSeconds(3600);

// Act
        String token = adapter.generate(sub, email, rol, extra, exp).block();

// Assert
        assertNotNull(token);
        assertEquals(Boolean.FALSE, token.trim().isEmpty());

    }
    
    @Test
    void testGenerateAndVerifyToken() {
        String secret = "dGhpc2lzYXNlY3JldGtleWZvcnRlc3Rpbmc"; // Base64URL de
        LoginSecurityRepositoryAdapter adapter = new LoginSecurityRepositoryAdapter(secret);
        String sub = "123";
        String email = "test@test.com";
        String rol = "USER";
        Map<String, Object> extra = Map.of("custom", "value");
        Instant exp = Instant.now().plusSeconds(3600);  // 1 hour expiration
        String token = adapter.generate(sub, email, rol, extra, exp).block();
        assertNotNull(token);
        LoginRepository.JwtPayload payload = adapter.verify(token);
        assertNotNull(payload);
        assertEquals(sub, payload.subject());
        assertEquals(email, payload.email());
        assertEquals(rol, payload.rol());
    }
}
