package co.com.crediya.autenticacion.usecase.validatoken;

import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ValidateTokenUseCase")
class ValidateTokenUseCaseTest {

    @Mock
    private LoginRepository loginRepository;

    @Test
    @DisplayName("Debe validar un token JWT válido")
    void debeValidarTokenValido() {
        String token = "token_valido";
        LoginRepository.JwtPayload expectedPayload = new LoginRepository.JwtPayload("1", "user@email.com", "USER");

        when(loginRepository.verify(token)).thenReturn(expectedPayload);

        Assertions.assertEquals(expectedPayload, loginRepository.verify(token));
    }

    @Test
    @DisplayName("Debe lanzar excepción para un token JWT inválido")
    void debeLanzarExcepcionParaTokenInvalido() {
        String token = "token_invalido";

        when(loginRepository.verify(token)).thenThrow(new RuntimeException("Token inválido"));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            loginRepository.verify(token);
        });
        Assertions.assertEquals("Token inválido", exception.getMessage());

    }


}
