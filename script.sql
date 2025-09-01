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
  password varchar(350),
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



CREATE TABLE crediya.estados (
  id_estado TINYINT UNSIGNED PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

INSERT INTO crediya.estados (id_estado, nombre) VALUES
  (1,'PENDIENTE_REVISION'), (2,'APROBADA'), (3,'RECHAZADA');

CREATE TABLE crediya.tipo_prestamo (
  id_tipo_prestamo VARCHAR(36) PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- transaccional
CREATE TABLE crediya.solicitud (
  id_solicitud VARCHAR(36) PRIMARY KEY,
  documento_cliente VARCHAR(50) NOT NULL,
  email VARCHAR(255),
  monto DECIMAL(12,2) NOT NULL CHECK (monto > 0),
  plazo INT NOT NULL CHECK (plazo > 0),
  id_estado TINYINT UNSIGNED NOT NULL DEFAULT 1,
  id_tipo_prestamo VARCHAR(36) NOT NULL,
  creada_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_estado) REFERENCES estados(id_estado),
  FOREIGN KEY (id_tipo_prestamo) REFERENCES tipo_prestamo(id_tipo_prestamo)
) ENGINE=InnoDB;