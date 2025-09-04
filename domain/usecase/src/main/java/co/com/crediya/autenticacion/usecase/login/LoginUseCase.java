package co.com.crediya.autenticacion.usecase.login;

import co.com.crediya.autenticacion.model.exceptions.DomainException;
import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import co.com.crediya.autenticacion.model.login.gateways.PasswordEncoderPort;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;


@RequiredArgsConstructor
public class LoginUseCase {

    private static final Logger log = Loggers.getLogger(LoginUseCase.class);

    private final LoginRepository loginPort;
    private final UsuarioRepository usuarioPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public Mono<String> login(String email, String rawPassword){
            log.info("Inicio proceso de autenticacion para el usuario con email: {}", email);
          return usuarioPort.findByEmail(email)
                .switchIfEmpty(Mono.error(new DomainException("Credenciales invalidas.")))
                  .filter(user -> passwordEncoderPort.matches(rawPassword, user.getPassword()))
                  .switchIfEmpty(Mono.error(new DomainException("Credenciales invalidas.")))
                  .flatMap(userValido-> loginPort.generate(
                          userValido.getId().toString(),
                          userValido.getEmail(),
                          userValido.getNombreRol(),
                          Map.of("Nombre", userValido.getNombres() + " " + userValido.getApellidos()),
                          Instant.now().plus(Duration.ofMinutes(30))
                  ));
    }
}
