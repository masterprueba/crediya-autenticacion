package co.com.crediya.autenticacion.securityadapter;

import co.com.crediya.autenticacion.model.exceptions.DomainException;
import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static reactor.core.scheduler.Schedulers.boundedElastic;

@Repository
public class LoginSecurityRepositoryAdapter implements LoginRepository {

    private final String secret;

    public LoginSecurityRepositoryAdapter(@Value("${app.secret.key}") String secret) {
        this.secret = secret;
    }

    @Override
    public Mono<String> generate(String sub, String email, String rol, Map<String, Object> extra, Instant exp) {
        return Mono.fromCallable(()-> Jwts.builder()
                .subject(sub)
                .expiration(Date.from(exp))
                .claim("email", email)
                .claim("rol", rol)
                .claims(extra == null ? Map.of() : extra)
                .signWith(getKey(secret), Jwts.SIG.HS256)
                .compact()
        ).subscribeOn(boundedElastic());
    }

    @Override
    public JwtPayload verify(String token) {
        try {
            var jws = Jwts.parser().verifyWith(getKey(secret)).build().parseSignedClaims(token);
            var c = jws.getPayload();
            return new JwtPayload(c.getSubject(), c.get("email", String.class), c.get("rol", String.class));
        } catch (JwtException e) {
            throw new DomainException("token_invalido");
        }
    }

    private SecretKey getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
