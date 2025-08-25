package co.com.crediya.autenticacion.usecase.consultarusuario;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.usuario.exceptions.DomainException;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ConsultarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private static final Long ROL_CLIENTE = 4L;

    public Mono<Usuario> ejecutar(String email) {
        return usuarioRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new DomainException("Usuario no encontrado")))
                .filter(usuario -> ROL_CLIENTE.equals(usuario.getIdRol()))
                .switchIfEmpty(Mono.error(new DomainException("El usuario no tiene el rol de cliente")));
    }
}
