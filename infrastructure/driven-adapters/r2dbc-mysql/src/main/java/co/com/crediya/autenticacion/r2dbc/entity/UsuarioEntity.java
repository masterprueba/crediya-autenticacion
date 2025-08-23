package co.com.crediya.autenticacion.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Table("usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {
    @Id
    private Long id;
    private String nombres;
    private String apellidos;
    private LocalDate nacimiento;
    private String direccion;
    private String telefono;
    private String email;
    private BigDecimal salario;
    private Instant creado;

   
}