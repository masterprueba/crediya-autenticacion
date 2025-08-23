package co.com.crediya.autenticacion.r2dbc;

import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.autenticacion.r2dbc.entity.UsuarioEntity;
import co.com.crediya.autenticacion.r2dbc.helper.ReactiveAdapterOperations;

import org.reactivecommons.utils.ObjectMapper;
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
        return save(usuario).as(transactionalOperator::transactional);
    }


    @Override
    public Mono<Boolean> existsByCorreo(String correoElectronico) {
        return repository.countByCorreo(correoElectronico)
                         .map(count -> count > 0);
    }

   
}
