package co.com.crediya.autenticacion.config;

import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.Mockito;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        new ApplicationContextRunner()
                .withUserConfiguration(UseCasesConfig.class)
                .withBean(UsuarioRepository.class, () -> Mockito.mock(UsuarioRepository.class))
                .run(context -> {
                    assertThat(context).hasBean("registrarUsuarioUseCase");
                });
    }
}