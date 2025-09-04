package co.com.crediya.autenticacion.usecase.consultarusuario;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.exceptions.DomainException;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

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


    public Mono<List<Usuario>> consultarUsuariios(Long rol) {
        return usuarioRepository.findByRol(rol)
                .collectList()
                .flatMap(usuarios -> {
                    if (usuarios.isEmpty()) {
                        return Mono.error(new DomainException("No se encontraron usuarios con el rol especificado"));
                    } else {
                        return Mono.just(usuarios);
                    }
                });
    }
}
