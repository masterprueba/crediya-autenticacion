package co.com.crediya.autenticacion.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un usuario consultado.")
public record UsuarioResponse(
        @JsonProperty("usuario")
        @Schema(description = "Nombre completo del usuario.", example = "Juan Pérez")
        String nombreCompleto,

        @JsonProperty("email")
        @Schema(description = "Correo electrónico del usuario.", example = "juan.perez@example.com")
        String email,

        @JsonProperty("documento_identidad")
        @Schema(description = "Número de documento de identidad.", example = "123456789")
        String documentoIdentidad
) {
}
