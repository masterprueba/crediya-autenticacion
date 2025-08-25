package co.com.crediya.autenticacion.usecase.registrarusuario;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.usuario.exceptions.DomainException;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.autenticacion.model.usuario.validation.UsuarioValidations;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {
    private final UsuarioRepository repo;

    public Mono<Usuario> ejecutar(Usuario u) {
        return UsuarioValidations.completa()
                .validate(u)
                .flatMap(usuario -> repo.existsByCorreo(usuario.getEmail())
                        .flatMap(existe -> {
                            if (Boolean.TRUE.equals(existe)) {
                                return Mono.error(new DomainException("El correo electrónico ya existe"));
                            }
                            return repo.saveTransactional(usuario);
                        })
                );
    }
}
