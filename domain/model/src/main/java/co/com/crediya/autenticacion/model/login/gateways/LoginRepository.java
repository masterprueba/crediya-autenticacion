package co.com.crediya.autenticacion.model.login.gateways;

import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

public interface LoginRepository {
     Mono<Boolean> matches(CharSequence raw, String hash);

     Mono<String> generate(String sub, String email, String rol, Map<String,Object> extra, Instant exp);
}
