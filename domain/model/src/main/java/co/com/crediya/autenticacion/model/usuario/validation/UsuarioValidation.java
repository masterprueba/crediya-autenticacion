package co.com.crediya.autenticacion.model.usuario.validation;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.usuario.exceptions.DomainException;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UsuarioValidation {
    Mono<Usuario> validate(Usuario usuario);
    
    default UsuarioValidation and(UsuarioValidation other) {
        return usuario -> this.validate(usuario)
                .flatMap(other::validate);
    }
}
