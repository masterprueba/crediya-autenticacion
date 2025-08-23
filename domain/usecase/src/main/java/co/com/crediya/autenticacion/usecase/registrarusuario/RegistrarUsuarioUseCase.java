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
            System.out.println("salario_invalido");
        }
        if (u.getNombres() == null || u.getNombres().isBlank())
            System.out.println("nombres_requeridos");
        if (u.getApellidos() == null || u.getApellidos().isBlank())
            System.out.println("apellidos_requeridos");
        if (u.getEmail() == null || u.getEmail().isBlank())
            System.out.println("correo_requerido");
        return Mono.just(u);
    }
}
