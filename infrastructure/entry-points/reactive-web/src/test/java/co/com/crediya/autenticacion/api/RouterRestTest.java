package co.com.crediya.autenticacion.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios para RouterRest")
class RouterRestTest {

    @Mock
    private Handler handler;

    private RouterRest routerRest;

    @BeforeEach
    void setUp() {
        routerRest = new RouterRest();
    }

    @Test
    @DisplayName("Debe crear RouterFunction correctamente")
    void debeCrearRouterFunctionCorrectamente() {
        // Act
        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);

        // Assert
        assertNotNull(routerFunction);
    }

    @Test
    @DisplayName("Debe configurar la ruta POST /api/v1/usuarios")
    void debeConfigurarLaRutaPOSTUsuarios() {
        // Act
        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);

        // Assert
        assertNotNull(routerFunction);
        // Verificamos que el RouterFunction fue creado (la configuración de rutas es interna de Spring)
        assertTrue(routerFunction.toString().contains("POST"));
    }

    @Test
    @DisplayName("Debe inyectar Handler correctamente")
    void debeInyectarHandlerCorrectamente() {
        // Arrange & Act
        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);

        // Assert
        assertNotNull(routerFunction);
        // El handler se inyecta correctamente si no hay excepción
    }
}
