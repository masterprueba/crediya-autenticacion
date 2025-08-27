package co.com.crediya.autenticacion.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    @Test
    void debeConstruirCorsWebFilterConOrigenesPermitidos() {
        CorsConfig config = new CorsConfig();
        String origenes = "https://example.com,https://otro.com";

        CorsWebFilter filter = config.corsWebFilter(origenes);
        assertNotNull(filter);

        MockServerHttpRequest request = MockServerHttpRequest
                .method(HttpMethod.OPTIONS, "/api")
                .header(HttpHeaders.ORIGIN, "https://example.com")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        WebFilterChain chain = ex -> Mono.empty();

        // No debe lanzar excepción al procesar preflight y debe completar normalmente
        assertDoesNotThrow(() -> filter.filter(exchange, chain).block());
    }
}


