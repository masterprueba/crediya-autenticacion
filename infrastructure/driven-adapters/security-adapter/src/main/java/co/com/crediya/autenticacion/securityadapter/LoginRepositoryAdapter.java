package co.com.crediya.autenticacion.securityadapter;

import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Repository
public class LoginRepositoryAdapter implements LoginRepository {

    private final String secretKey;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginRepositoryAdapter(@Value("${app.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public Mono<Boolean> matches(CharSequence raw, String hash) {
        return Mono.fromCallable(()-> passwordEncoder.matches(raw,hash));
    }

    @Override
    public Mono<String> generate(String sub, String email, String rol, Map<String, Object> extra, Instant exp) {
        return Mono.fromCallable(()-> Jwts.builder()
                .setSubject(sub)
                .setExpiration(Date.from(exp))
                .claim("email", email)
                .claim("rol", rol)
                .addClaims(extra)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact());
    }
}
