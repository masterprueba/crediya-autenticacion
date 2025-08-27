package co.com.crediya.autenticacion.model.usuario.validation;

import co.com.crediya.autenticacion.model.exceptions.DomainException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class UsuarioValidations {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );
    
    private static final BigDecimal MIN_SALARY = BigDecimal.ZERO;
    private static final BigDecimal MAX_SALARY = new BigDecimal("15000000");
    
    private UsuarioValidations() {
        // Utility class
    }
    
    public static UsuarioValidation nombresRequeridos() {
        return usuario -> {
            if (usuario.getNombres() == null || usuario.getNombres().isBlank()) {
                return Mono.error(new DomainException("Los nombres son requeridos"));
            }
            return Mono.just(usuario);
        };
    }
    
    public static UsuarioValidation apellidosRequeridos() {
        return usuario -> {
            if (usuario.getApellidos() == null || usuario.getApellidos().isBlank()) {
                return Mono.error(new DomainException("Los apellidos son requeridos"));
            }
            return Mono.just(usuario);
        };
    }
    
    public static UsuarioValidation emailRequerido() {
        return usuario -> {
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                return Mono.error(new DomainException("El campo 'correo_electronico' es requerido."));
            }
            return Mono.just(usuario);
        };
    }
    
    public static UsuarioValidation formatoEmailValido() {
        return usuario -> {
            if (!EMAIL_PATTERN.matcher(usuario.getEmail()).matches()) {
                return Mono.error(new DomainException("El formato del correo electrónico no es válido."));
            }
            return Mono.just(usuario);
        };
    }
    
    public static UsuarioValidation salarioEnRango() {
        return usuario -> {
            if (usuario.getSalario() == null ||
                    usuario.getSalario().compareTo(MIN_SALARY) < 0 ||
                    usuario.getSalario().compareTo(MAX_SALARY) > 0) {
                return Mono.error(new DomainException("El salario base debe estar entre 0 y 15,000,000"));
            }
            return Mono.just(usuario);
        };
    }
    
    public static UsuarioValidation completa() {
        return nombresRequeridos()
                .and(apellidosRequeridos())
                .and(emailRequerido())
                .and(formatoEmailValido())
                .and(salarioEnRango());
    }
}
