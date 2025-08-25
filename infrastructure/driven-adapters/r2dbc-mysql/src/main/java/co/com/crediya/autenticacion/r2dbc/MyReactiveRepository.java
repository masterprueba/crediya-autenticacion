package co.com.crediya.autenticacion.r2dbc;

import co.com.crediya.autenticacion.r2dbc.entity.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MyReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, Long>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

    Mono<Long> countByEmail(String correo);

    Mono<UsuarioEntity> findByEmail(String correo);
}
