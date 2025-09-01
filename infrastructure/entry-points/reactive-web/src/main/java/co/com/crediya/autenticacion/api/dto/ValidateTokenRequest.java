package co.com.crediya.autenticacion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para validar un token JWT")
public record ValidateTokenRequest(
    @Schema(description = "Token JWT a validar", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token
) {}
