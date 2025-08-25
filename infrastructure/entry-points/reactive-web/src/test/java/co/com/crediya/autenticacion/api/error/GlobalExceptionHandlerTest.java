package co.com.crediya.autenticacion.api.error;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void debeConstruirRouterFunction() {
        ErrorAttributes errorAttributes = new GlobalErrorAttributes();
        WebProperties webProps = new WebProperties();
        StaticApplicationContext context = new StaticApplicationContext();
        DefaultServerCodecConfigurer codecs = new DefaultServerCodecConfigurer();

        GlobalExceptionHandler handler = new GlobalExceptionHandler(errorAttributes, webProps, context, codecs);
        assertNotNull(handler);

        var routing = handler.getRoutingFunction(errorAttributes);
        assertNotNull(routing);
    }
}


