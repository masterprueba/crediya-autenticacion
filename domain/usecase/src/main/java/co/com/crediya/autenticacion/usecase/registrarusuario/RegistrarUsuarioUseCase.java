package co.com.crediya.autenticacion.usecase.registrarusuario;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {
    private final UsuarioRepository repo;

    public Mono<Usuario> ejecutar(Usuario u) {
        return Mono.just(u)
                .flatMap(this::validar)
                .flatMap(val -> repo.existsByCorreo(val.getEmail())
                        .flatMap(existe -> {
                            if (existe) {
                                return Mono.error(new IllegalArgumentException("El correo electrónico ya existe"));
                            }
                            return repo.saveTransactional(val);
                        })
                );
    }

    private Mono<Usuario> validar(Usuario u) {
        // Reglas de negocio adicionales (p.ej. salario 0..15'000.000)
        if (u.getSalario() == null ||
                u.getSalario().compareTo(BigDecimal.ZERO) < 0 ||
                u.getSalario().compareTo(new BigDecimal("15000000")) > 0) {
            return Mono.error(new IllegalArgumentException("El salario debe estar entre 0 y 15,000,000"));
        }
        if (u.getNombres() == null || u.getNombres().isBlank()) {
            return Mono.error(new IllegalArgumentException("Los nombres son requeridos"));
        }
        if (u.getApellidos() == null || u.getApellidos().isBlank()) {
            return Mono.error(new IllegalArgumentException("Los apellidos son requeridos"));
        }
        if (u.getEmail() == null || u.getEmail().isBlank()) {
            return Mono.error(new IllegalArgumentException("El correo electrónico es requerido"));
        }
        return Mono.just(u);
    }
}
