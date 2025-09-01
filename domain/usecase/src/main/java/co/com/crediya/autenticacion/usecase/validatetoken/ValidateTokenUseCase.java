package co.com.crediya.autenticacion.usecase.validatetoken;

import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import co.com.crediya.autenticacion.model.login.gateways.LoginRepository.JwtPayload;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@RequiredArgsConstructor
public class ValidateTokenUseCase {

    private static final Logger log = Loggers.getLogger(ValidateTokenUseCase.class);

    private final LoginRepository loginRepository;

    public Mono<JwtPayload> validateToken(String token) {
        log.info("Validando token JWT");
        
        return Mono.fromCallable(() -> {
            // El método verify ya lanza DomainException si el token es inválido
            JwtPayload payload = loginRepository.verify(token);
            log.info("Token válido para usuario: {}, rol: {}", payload.email(), payload.rol());
            return payload;
        })
        .onErrorMap(throwable -> {
            log.error("Error validando token: {}", throwable.getMessage());
            return throwable;
        });
    }
}
