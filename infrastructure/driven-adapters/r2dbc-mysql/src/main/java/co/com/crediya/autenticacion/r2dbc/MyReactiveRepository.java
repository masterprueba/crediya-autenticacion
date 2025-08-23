package co.com.crediya.autenticacion.r2dbc;

import co.com.crediya.autenticacion.r2dbc.entity.UsuarioEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MyReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, Long>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :correoElectronico")
    Mono<Long> countByCorreo(String correoElectronico);
}
