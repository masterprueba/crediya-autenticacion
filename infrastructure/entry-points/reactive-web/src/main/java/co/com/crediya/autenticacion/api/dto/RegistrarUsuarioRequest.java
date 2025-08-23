package co.com.crediya.autenticacion.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistrarUsuarioRequest(
        String nombres,
        String apellidos,
        LocalDate fecha_nacimiento,
        String direccion,
        String telefono,
        String correo_electronico,
        BigDecimal salario_base
) {}
