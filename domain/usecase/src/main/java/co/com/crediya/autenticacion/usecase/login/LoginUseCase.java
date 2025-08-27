package co.com.crediya.autenticacion.usecase.login;

import co.com.crediya.autenticacion.model.exceptions.DomainException;
import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RequiredArgsConstructor
public class LoginUseCase {

    private final LoginRepository loginPort;
    private final UsuarioRepository usuarioPort;

    public Mono<String> login(String email, String rawPassword){
          return usuarioPort.findByEmail(email)
                .switchIfEmpty(Mono.error(new DomainException("Credenciales invalidas")))
                     .filterWhen(user -> loginPort.matches(rawPassword, user.getNombres()))
                  .switchIfEmpty(Mono.error(new DomainException("Credenciales invalidas")))
                  .flatMap(u -> loginPort.generate(
                          u.getId().toString(),
                          u.getEmail(),
                          u.getIdRol().toString(),
                          Map.of("Nombre", u.getNombres() + " " + u.getApellidos()),
                          Instant.now().plus(Duration.ofHours(8))
                  ));
    }
}
