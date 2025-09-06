package co.com.crediya.autenticacion.config;

import co.com.crediya.autenticacion.model.login.gateways.LoginRepository;
import co.com.crediya.autenticacion.model.login.gateways.PasswordEncoderPort;
import co.com.crediya.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.autenticacion.usecase.consultarusuario.ConsultarUsuarioUseCase;
import co.com.crediya.autenticacion.usecase.login.LoginUseCase;
import co.com.crediya.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import co.com.crediya.autenticacion.usecase.validatetoken.ValidateTokenUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.crediya.autenticacion.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase(UsuarioRepository usuarioRepository, PasswordEncoderPort passwordEncoderPort) {
        return new RegistrarUsuarioUseCase(usuarioRepository, passwordEncoderPort);
    }

    @Bean
    public ConsultarUsuarioUseCase consultarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new ConsultarUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public LoginUseCase loginUseCase(LoginRepository loginRepository, UsuarioRepository usuarioRepository, PasswordEncoderPort passwordEncoderPort) {
        return new LoginUseCase(loginRepository, usuarioRepository, passwordEncoderPort);
    }

    @Bean
    public ValidateTokenUseCase validateTokenUseCase(LoginRepository loginRepository) {
        return new ValidateTokenUseCase(loginRepository);
    }
}
