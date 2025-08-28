package co.com.crediya.autenticacion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos requeridos para iniciar sesión.")
public record LoginRequest(
        @Schema(description = "Correo electrónico del usuario.", example = "jors@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,
        @Schema(description = "Contraseña del usuario.", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}