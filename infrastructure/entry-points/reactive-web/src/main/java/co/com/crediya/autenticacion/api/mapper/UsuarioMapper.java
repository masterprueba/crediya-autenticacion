package co.com.crediya.autenticacion.api.mapper;

import co.com.crediya.autenticacion.api.dto.RegistrarUsuarioRequest;
import co.com.crediya.autenticacion.api.dto.UsuarioResponse;
import co.com.crediya.autenticacion.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creado", ignore = true)
    @Mapping(source = "fecha_nacimiento",target = "fechaNacimiento")
    @Mapping(source = "documento_identidad",target = "documentoIdentidad")
    @Mapping(source = "rol",target = "idRol")
    @Mapping(source = "salario_base",target = "salario")
    @Mapping(source = "correo_electronico",target = "email")
    @Mapping(target = "password",ignore = true)
    Usuario toDomain(RegistrarUsuarioRequest r);

    @Mapping(target = "nombreCompleto", expression = "java(usuario.getNombres() + \" \" + usuario.getApellidos())")
    UsuarioResponse toResponse(Usuario usuario);
}
