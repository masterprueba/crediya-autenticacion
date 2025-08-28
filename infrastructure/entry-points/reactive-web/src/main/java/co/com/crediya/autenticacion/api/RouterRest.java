package co.com.crediya.autenticacion.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/usuarios",
                    method = RequestMethod.POST,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "registrar"
            ),
            @RouterOperation(
                    path = "/usuarios/cliente",
                    method = RequestMethod.GET,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "consultarPorEmail"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler usuarioHandler, LoginHandler loginHandler) {
        return RouterFunctions.route(POST("/usuarios"), usuarioHandler::registrar)
                .andRoute(GET("/usuarios/cliente"), usuarioHandler::consultarPorEmail)
                .andRoute(POST("/login"), loginHandler::login);
    }
}
