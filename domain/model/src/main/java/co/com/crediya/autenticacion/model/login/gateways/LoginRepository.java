package co.com.crediya.autenticacion.model.login.gateways;

import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

public interface LoginRepository {

     Mono<String> generate(String sub, String email, String rol, Map<String,Object> extra, Instant exp);
     JwtPayload verify(String token); // lanza DomainException si inválido
     record JwtPayload(String subject, String email, String rol) {}
}
