package co.com.crediya.autenticacion.api.error;

import co.com.crediya.autenticacion.model.usuario.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalErrorAttributesTest {

    @Test
    void debeConstruirAtributosParaDomainException() {
        GlobalErrorAttributes attrs = new GlobalErrorAttributes();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/ruta").build());
        ServerRequest request = ServerRequest.create(exchange, new DefaultServerCodecConfigurer().getReaders());
        exchange.getAttributes().putIfAbsent("org.springframework.boot.web.reactive.error.DefaultErrorAttributes.ERROR", new DomainException("Fallo en dominio"));

        Map<String,Object> map = attrs.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        assertEquals(400, map.get("status"));
        assertEquals("DATOS_INVALIDOS", map.get("code"));
        assertTrue(map.get("message").toString().contains("Fallo en dominio"));
        assertEquals("/ruta", map.get("path"));
    }

    @Test
    void debeConstruirAtributosParaErrorGenerico() {
        GlobalErrorAttributes attrs = new GlobalErrorAttributes();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/otra").build());
        ServerRequest request = ServerRequest.create(exchange, new DefaultServerCodecConfigurer().getReaders());
        exchange.getAttributes().putIfAbsent("org.springframework.boot.web.reactive.error.DefaultErrorAttributes.ERROR", new RuntimeException("oops"));

        Map<String,Object> map = attrs.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        assertEquals(500, map.get("status"));
        assertEquals("ERROR_INTERNO", map.get("code"));
        assertTrue(map.get("message").toString().contains("Ha ocurrido un error inesperado"));
        assertEquals("/otra", map.get("path"));
    }
}


