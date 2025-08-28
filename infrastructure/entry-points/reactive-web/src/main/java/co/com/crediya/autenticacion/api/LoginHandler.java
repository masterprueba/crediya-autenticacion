package co.com.crediya.autenticacion.api;

import co.com.crediya.autenticacion.api.dto.LoginRequest;
import co.com.crediya.autenticacion.api.dto.LoginResponse;
import co.com.crediya.autenticacion.usecase.login.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoginHandler {
    private final LoginUseCase loginUseCase;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(loginRequest -> loginUseCase.login(loginRequest.email(), loginRequest.password()))
                .flatMap(token -> ServerResponse.ok().bodyValue(new LoginResponse(token)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
    }
}
