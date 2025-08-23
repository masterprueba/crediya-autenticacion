package co.com.crediya.autenticacion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Datos requeridos para registrar un nuevo usuario.")
public record RegistrarUsuarioRequest(
        @Schema(description = "Nombres del usuario.", example = "Juan Carlos", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombres,
        @Schema(description = "Apellidos del usuario.", example = "Pérez García", requiredMode = Schema.RequiredMode.REQUIRED)
        String apellidos,
        @Schema(description = "Número de teléfono de contacto.", example = "+573001234567", requiredMode = Schema.RequiredMode.REQUIRED)
        String telefono,
        @Schema(description = "Correo electrónico del usuario. Debe ser único.", example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String correo_electronico,
        @Schema(description = "Numero de documento de identidad.", example = "1234567")
        String documento_identidad,
        @Schema(description = "Rol .", example = "ADMIN,USER,MANAGER")
        Long rol,
        @Schema(description = "Salario base mensual.", example = "2500000", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal salario_base
) {}
