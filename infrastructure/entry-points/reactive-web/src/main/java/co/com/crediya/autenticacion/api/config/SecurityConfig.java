package co.com.crediya.autenticacion.api.config;

import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
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
                        .pathMatchers(HttpMethod.POST, "/usuarios/auth/validate").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/webjars/swagger-ui/**").permitAll()
                        .pathMatchers("/swagger-ui.html").permitAll()
                        .pathMatchers("/v3/api-docs/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/usuarios")
                        .hasAnyRole("ADMIN","ASESOR")
                        .pathMatchers(HttpMethod.GET, "/usuarios/rol/**").hasAnyRole("ASESOR")
                        .pathMatchers(HttpMethod.GET, "/usuarios/**").hasAnyRole("CLIENTE")
                        .anyExchange().authenticated()
                )
                .exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedHandler()))
                .addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();

    }

    @Bean
    ReactiveAuthenticationManager reactiveJwtAuthManager(LoginRepository jwt) {
        return authentication -> {
            var token = (String) authentication.getCredentials();
            LoginRepository.JwtPayload payload = jwt.verify(token); // DomainException si inválido
            var auths = java.util.List.of(new SimpleGrantedAuthority("ROLE_"+payload.rol()));
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


    @Bean
    public ServerAccessDeniedHandler customAccessDeniedHandler() {
        return (exchange, denied) -> {
            var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            var dataBuffer = response.bufferFactory()
                    .wrap("{\"mensaje\":\"Acceso denegado: no tienes permisos suficientes.\"}".getBytes());
            return response.writeWith(Mono.just(dataBuffer));
        };
    }
}

