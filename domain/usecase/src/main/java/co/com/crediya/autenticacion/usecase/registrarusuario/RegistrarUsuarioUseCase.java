package co.com.crediya.autenticacion.usecase.registrarusuario;

import co.com.crediya.autenticacion.model.login.gateways.PasswordEncoderPort;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.model.exceptions.DomainException;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.autenticacion.model.usuario.validation.UsuarioValidations;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {
    private final UsuarioRepository repo;
    private final PasswordEncoderPort passwordEncoderPort;

    public Mono<Usuario> ejecutar(Usuario u) {
        return UsuarioValidations.completa()
                .validate(u)
                .flatMap(usuario -> repo.existsByCorreo(usuario.getEmail())
                        .flatMap(existe -> {
                            if (Boolean.TRUE.equals(existe)) {
                                return Mono.error(new DomainException("El correo electrónico ya existe"));
                            }
                            String hashedPassword = passwordEncoderPort.encode(usuario.getPassword());
                            usuario.setPassword(hashedPassword);
                            return repo.saveTransactional(usuario);
                        })
                );
    }
}
