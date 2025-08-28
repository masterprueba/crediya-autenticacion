package co.com.crediya.autenticacion.api.config;

import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginRepository jwt;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        var authManager = reactiveJwtAuthManager(jwt);
        var converter = bearerConverter();

        var jwtWebFilter = new AuthenticationWebFilter(authManager);
        jwtWebFilter.setServerAuthenticationConverter(converter);

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers(HttpMethod.POST, "/login").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        // ejemplo: solo ADMIN/ASESOR registran usuarios
                        .pathMatchers(HttpMethod.POST, "/api/v1/usuarios")
                        .hasAnyRole("ADMIN","ASESOR")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();

    }

    @Bean
    ReactiveAuthenticationManager reactiveJwtAuthManager(LoginRepository jwt) {
        return authentication -> {
            var token = (String) authentication.getCredentials();
            LoginRepository.JwtPayload payload = jwt.verify(token); // DomainException si inválido
            var auths = java.util.List.of(new SimpleGrantedAuthority("ROLE_" + payload.rol()));
            return Mono.just(new UsernamePasswordAuthenticationToken(payload, token, auths));
        };
    }

    @Bean
    ServerAuthenticationConverter bearerConverter() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7))
                .map(t -> new UsernamePasswordAuthenticationToken(null, t));
    }
}

