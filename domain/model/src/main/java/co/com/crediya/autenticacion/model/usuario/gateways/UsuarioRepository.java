package co.com.crediya.autenticacion.model.usuario.gateways;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
    Mono<Boolean> existsByCorreo(String correoElectronico);
    Mono<Usuario> saveTransactional(Usuario usuario);
    Mono<Usuario> findByEmail(String email);
    Flux<Usuario> findByRol(Long rol);
}
