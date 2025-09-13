// domain/usecase/src/test/java/co/com/crediya/autenticacion/usecase/validatoken/ValidateTokenUseCaseTest.java
package co.com.crediya.autenticacion.usecase.validatoken;

import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import co.com.crediya.autenticacion.usecase.validatetoken.ValidateTokenUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ValidateTokenUseCase")
class ValidateTokenUseCaseTest {

    @Mock
    private LoginRepository loginRepository;

    @Test
    @DisplayName("Debe validar un token JWT válido usando el use case")
    void debeValidarTokenValido() {
        String token = "token_valido";
        LoginRepository.JwtPayload expectedPayload = new LoginRepository.JwtPayload("1", "user@email.com", "USER");

        when(loginRepository.verify(token)).thenReturn(expectedPayload);

        ValidateTokenUseCase useCase = new ValidateTokenUseCase(loginRepository);

        Mono<LoginRepository.JwtPayload> result = useCase.validateToken(token);

        StepVerifier.create(result)
                .expectNext(expectedPayload)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe propagar excepción para token inválido usando el use case")
    void debeLanzarExcepcionParaTokenInvalido() {
        String token = "token_invalido";
        RuntimeException exception = new RuntimeException("Token inválido");

        when(loginRepository.verify(token)).thenThrow(exception);

        ValidateTokenUseCase useCase = new ValidateTokenUseCase(loginRepository);

        Mono<LoginRepository.JwtPayload> result = useCase.validateToken(token);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e.getMessage().equals("Token inválido"))
                .verify();
    }
}