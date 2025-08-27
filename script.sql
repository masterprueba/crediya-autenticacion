-- Tabla de Roles
CREATE TABLE crediya.roles (
  unique_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre varchar(50) NOT NULL,
  descripcion varchar(255) NOT NULL,
  creado TIMESTAMP NOT NULL DEFAULT now()
);

-- Tabla de Usuarios
CREATE TABLE crediya.usuarios (
  id_usuario BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre varchar(50) NOT NULL,
  apellido varchar(50) NOT NULL,
  email varchar(50) NOT NULL,
  documento_identidad varchar(20) NOT NULL,
  telefono varchar(50) NOT NULL,
  id_rol BIGINT NOT NULL,
  salario_base NUMERIC(12,2) NOT NULL CHECK (salario_base >= 0 AND salario_base <= 15000000),
  creado TIMESTAMP NOT NULL DEFAULT now(),
  CONSTRAINT fk_usuarios_rol FOREIGN KEY (id_rol) REFERENCES crediya.roles(unique_id)
);

-- Índices únicos
CREATE UNIQUE INDEX ux_usuarios_email ON crediya.usuarios (email);
CREATE UNIQUE INDEX ux_usuarios_documento ON crediya.usuarios (documento_identidad);

-- Insertar roles básicos
INSERT INTO crediya.roles (nombre, descripcion) VALUES 
('ADMIN', 'Administrador del sistema'),
('USER', 'Usuario estándar'),
('MANAGER', 'Gerente o supervisor');