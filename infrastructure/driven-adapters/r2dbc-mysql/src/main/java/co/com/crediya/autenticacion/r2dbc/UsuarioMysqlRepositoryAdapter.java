package co.com.crediya.autenticacion.r2dbc;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.exceptions.DomainException;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.autenticacion.r2dbc.entity.RoleEntity;
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
public class UsuarioMysqlRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        Long,
        UsuarioMysqlRepository
> implements UsuarioRepository {
    private final TransactionalOperator transactionalOperator;
    private final RoleMysqlRepository roleRepository;
    private static final Logger log = LoggerFactory.getLogger(UsuarioMysqlRepositoryAdapter.class);
    
    public UsuarioMysqlRepositoryAdapter(UsuarioMysqlRepository repository, ObjectMapper mapper,
                                         TransactionalOperator transactionalOperator, RoleMysqlRepository roleRepository) {

        super(repository, mapper, d -> mapper.map(d, Usuario.class));
        this.transactionalOperator = transactionalOperator;
        this.roleRepository = roleRepository;
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
                .flatMap(usuarioRol -> roleRepository.findById(usuarioRol.getIdRol())
                        .map(RoleEntity::getNombre)
                        .defaultIfEmpty("CLIENTE")
                        .map(nombreRol -> {
                            usuarioRol.setNombreRol(nombreRol);
                            return usuarioRol;
                        })
                        .defaultIfEmpty(usuarioRol)
                )
                .doOnNext(u -> log.info("Usuario encontrado: {}", u))
                .doOnSuccess(u -> {               // se ejecuta al completar exitosamente
                    if (u != null) {
                        log.info("Usuario encontrado id={} email={}", u.getId(), u.getEmail());
                    } else {
                        log.warn("Usuario NO encontrado email={}", email); // Mono.empty()
                    }
                })
                .doOnError(e -> log.error("Error consultando usuario email={}", email));

    }


}
