package co.com.crediya.autenticacion.r2dbc;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.usuario.exceptions.DomainException;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.autenticacion.r2dbc.entity.UsuarioEntity;
import co.com.crediya.autenticacion.r2dbc.helper.ReactiveAdapterOperations;

import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        Long, 
        MyReactiveRepository
> implements UsuarioRepository {
    private final TransactionalOperator transactionalOperator;
    private static final Logger log = LoggerFactory.getLogger(MyReactiveRepositoryAdapter.class);
    
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper, 
                                      TransactionalOperator transactionalOperator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Usuario> saveTransactional(Usuario usuario) {
        log.info("Iniciando guardado transaccional para el usuario con correo: {}", usuario.getEmail());
        
        Mono<Usuario> saveOperation = Mono.just(usuario)
                .map(this::toData)
                .flatMap(repository::save)
                .map(this::toEntity)
                .onErrorMap(DataIntegrityViolationException.class, ex -> {
                    log.warn("Se violó una restricción de integridad al guardar el correo: {} error: {}", 
                        usuario.getEmail(), ex.getMessage());
                    return new DomainException("El correo electrónico ya existe en el sistema.");
                });

        return saveOperation.as(transactionalOperator::transactional)
                .doOnSuccess(u -> log.info("Usuario guardado exitosamente en la base de datos con id: {}", u.getId()));
    }


    @Override
    public Mono<Boolean> existsByCorreo(String correoElectronico) {
        return repository.countByEmail(correoElectronico)
                         .map(count -> count > 0);
    }

    @Override
    public Mono<Usuario> findByEmail(String email) {
        Mono<UsuarioEntity> user = repository.findByEmail(email);
        return user
                .map(this::toEntity)
                .doOnNext(u -> log.info("Usuario encontrado: {}", u))
                .doOnSuccess(u -> {               // se ejecuta al completar exitosamente
                    if (u != null) {
                        log.info("Usuario encontrado id={} email={}", u.getId(), u.getEmail());
                    } else {
                        log.warn("Usuario NO encontrado email={}", email); // Mono.empty()
                    }
                })
                .doOnError(e -> log.error("Error consultando usuario email={}", email, e));

    }


}
