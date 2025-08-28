package co.com.crediya.autenticacion.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
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
    @Column("id_usuario")
    private Long id;
    @Column("nombre")
    private String nombres;
    @Column("apellido")
    private String apellidos;
    private String telefono;
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    private String direccion;
    private String email;
    @Column("documento_identidad")
    private String documentoIdentidad;
    @Column("id_rol")
    private Long idRol;
    @Column("salario_base")
    private BigDecimal salario;
    private Instant creado;
    private String password;
   
}