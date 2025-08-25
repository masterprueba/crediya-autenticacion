package co.com.crediya.autenticacion.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class SecurityHeadersConfigTest {

    @Test
    void debeAplicarEncabezadosDeSeguridad() {
        SecurityHeadersConfig filter = new SecurityHeadersConfig();

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/").build()
        );

        WebFilterChain chain = ex -> Mono.empty();

        filter.filter(exchange, chain).block();

        HttpHeaders headers = exchange.getResponse().getHeaders();
        assertEquals("default-src 'self'; frame-ancestors 'self'; form-action 'self'",
                headers.getFirst("Content-Security-Policy"));
        assertEquals("max-age=31536000;", headers.getFirst("Strict-Transport-Security"));
        assertEquals("nosniff", headers.getFirst("X-Content-Type-Options"));
        assertEquals("", headers.getFirst("Server"));
        assertEquals("no-store", headers.getFirst("Cache-Control"));
        assertEquals("no-cache", headers.getFirst("Pragma"));
        assertEquals("strict-origin-when-cross-origin", headers.getFirst("Referrer-Policy"));
    }
}


