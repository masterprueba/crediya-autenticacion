package co.com.crediya.autenticacion.api;

import co.com.crediya.autenticacion.api.dto.RegistrarUsuarioRequest;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;


@Component
@RequiredArgsConstructor
public class Handler {
    private final RegistrarUsuarioUseCase useCase;
    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    public Mono<ServerResponse> registrar(ServerRequest req) {
        return req.bodyToMono(RegistrarUsuarioRequest.class)
                .doOnNext(dto -> log.info("registrar-usuario intento correo={}", dto.correo_electronico()))
                .flatMap(this::toDomain)
                .flatMap(useCase::ejecutar)
                .flatMap(u -> ServerResponse.created(URI.create("/api/v1/usuarios/" + u.getId())).build());
    }

    private Mono<Usuario> toDomain(RegistrarUsuarioRequest r) {
        return Mono.just(new Usuario(
                null, r.nombres(), r.apellidos(), r.fecha_nacimiento(),
                r.direccion(), r.telefono(), r.correo_electronico(), r.salario_base(), Instant.now()
        ));
    }
}
