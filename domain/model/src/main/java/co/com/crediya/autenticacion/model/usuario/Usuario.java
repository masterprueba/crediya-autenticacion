package co.com.crediya.autenticacion.model.usuario;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Usuario {
    private Long id;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;
    private String documentoIdentidad;
    private Long idRol;
    private BigDecimal salario;
    private Instant creado;
}
