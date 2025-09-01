package co.com.crediya.autenticacion.api;

import co.com.crediya.autenticacion.api.dto.LoginRequest;
import co.com.crediya.autenticacion.api.dto.LoginResponse;
import co.com.crediya.autenticacion.usecase.login.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(name = "Login", description = "Operaciones de autenticación de usuarios")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario con email y contraseña. Devuelve un token JWT si las credenciales son válidas.",
            operationId = "login"
    )
    @RequestBody(
            required = true,
            description = "Credenciales de acceso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa, retorna el token JWT",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                {
                  "status": 400,
                  "code": "DATOS_INVALIDOS",
                  "message": "La información proporcionada es inválida. Credenciales invalidas",
                  "path": "/api/v1/login"
                }
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                {
                  "status": 500,
                  "code": "ERROR_INTERNO",
                  "message": "Ha ocurrido un error inesperado en el sistema. Por favor, contacte a soporte.",
                  "path": "/api/v1/login"
                }
                """)
                    )
            )
    })
    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(loginRequest -> loginUseCase.login(loginRequest.email(), loginRequest.password()))
                .flatMap(token -> ServerResponse.ok().bodyValue(new LoginResponse(token)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
    }
}
