package co.com.crediya.autenticacion.api;

import co.com.crediya.autenticacion.api.dto.ValidateTokenRequest;
import co.com.crediya.autenticacion.api.dto.ValidateTokenResponse;
import co.com.crediya.autenticacion.usecase.validatetoken.ValidateTokenUseCase;
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
@Tag(name = "Token Validation", description = "Operaciones para validación de tokens JWT")
public class TokenValidationHandler {
    
    private final ValidateTokenUseCase validateTokenUseCase;

    @Operation(
        summary = "Validar token JWT",
        description = "Valida un token JWT y retorna la información del usuario si es válido. " +
                     "Este endpoint es utilizado por otros microservicios para validar tokens.",
        operationId = "validateToken"
    )
    @RequestBody(
        required = true,
        description = "Token JWT a validar",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ValidateTokenRequest.class)
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token válido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ValidateTokenResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token inválido o expirado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = """
                    {
                      "status": 401,
                      "codigo": "TOKEN_INVALIDO",
                      "mensaje": "El token proporcionado es inválido o ha expirado",
                      "ruta": "/api/v1/auth/validate"
                    }
                    """)
            )
        )
    })
    public Mono<ServerResponse> validateToken(ServerRequest request) {
        return request.bodyToMono(ValidateTokenRequest.class)
            .flatMap(req -> validateTokenUseCase.validateToken(req.token()))
            .map(payload -> new ValidateTokenResponse(
                payload.subject(),
                payload.email(),
                payload.rol()
            ))
            .flatMap(response -> ServerResponse.ok().bodyValue(response))
            .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
    }
}
