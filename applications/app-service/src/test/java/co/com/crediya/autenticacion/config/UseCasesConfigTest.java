package co.com.crediya.autenticacion.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ComponentScan(basePackages = {"co.com.crediya.autenticacion.usecase", "co.com.crediya.autenticacion.securityadapter"})
class UseCasesConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testUseCaseBeansExist() {
        assertNotNull(context.getBean("registrarUsuarioUseCase"));
        assertNotNull(context.getBean("consultarUsuarioUseCase"));
        assertNotNull(context.getBean("loginUseCase"));
    }
}