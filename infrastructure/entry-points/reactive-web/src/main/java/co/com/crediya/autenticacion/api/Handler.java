package co.com.crediya.autenticacion.api;

import co.com.crediya.autenticacion.api.dto.RegistrarUsuarioRequest;
import co.com.crediya.autenticacion.api.mapper.UsuarioMapper;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import co.com.crediya.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;


@Component
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class Handler {
    private final RegistrarUsuarioUseCase useCase;
    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private final UsuarioMapper usuarioMapper;

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un nuevo usuario en el sistema con la información proporcionada. " +
                         "Valida que todos los campos sean obligatorios, el formato del email sea correcto, " +
                         "y que el correo no esté previamente registrado.",
            operationId = "registrarUsuario"
    )
    @RequestBody(
            required = true,
            description = "Datos del usuario para el registro",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegistrarUsuarioRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos. Puede ser por: campos vacíos, " +
                                 "formato de email incorrecto, salario fuera de rango (0-15,000,000), " +
                                 "o correo electrónico ya registrado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "status": 400,
                                      "codigo": "DATOS_INVALIDOS",
                                      "mensaje": "La información proporcionada es inválida. El correo electrónico ya existe",
                                      "ruta": "/api/v1/usuarios"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "status": 500,
                                      "codigo": "ERROR_INTERNO",
                                      "mensaje": "Ha ocurrido un error inesperado en el sistema. Por favor, contacte a soporte.",
                                      "ruta": "/api/v1/usuarios"
                                    }
                                    """)
                    )
            )
    })
    public Mono<ServerResponse> registrar(ServerRequest req) {
        return req.bodyToMono(RegistrarUsuarioRequest.class)
                .doOnNext(dto -> log.info("registrar-usuario intento correo={}", dto.correo_electronico()))
                .map(this::toDomain)
                .flatMap(useCase::ejecutar)
                .doOnSuccess(u -> log.info("registrar-usuario exitoso correo={}", u.getEmail()))
                .flatMap(u -> ServerResponse.created(URI.create("/api/v1/usuarios/" + u.getId())).build());
    }

    private Usuario toDomain(RegistrarUsuarioRequest registrarUsuarioRequest) {
        Usuario usuario = usuarioMapper.toDomain(registrarUsuarioRequest);
        usuario.setIdRol(2L);
        usuario.setCreado(Instant.now());
        return usuario;
    }


}
