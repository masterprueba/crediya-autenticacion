package co.com.crediya.autenticacion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response con los datos del usuario validado")
public record ValidateTokenResponse(
    @Schema(description = "ID del usuario", example = "123")
    String userId,
    
    @Schema(description = "Email del usuario", example = "cliente@example.com")
    String email,
    
    @Schema(description = "Rol del usuario", example = "CLIENTE")
    String role
) {}
