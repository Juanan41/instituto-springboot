-- =========================================================================
-- Desactivar temporalmente las restricciones de integridad
-- =========================================================================
SET REFERENTIAL_INTEGRITY FALSE;

-- =========================================================================
-- Borra tablas si existen (orden independiente de relaciones)
-- =========================================================================
DROP TABLE IF EXISTS ESTUDIANTES;
DROP TABLE IF EXISTS INSTITUTOS;
DROP TABLE IF EXISTS NOMBRES;

-- =========================================================================
-- Crear tabla INSTITUTOS (entidad principal)
-- =========================================================================
CREATE TABLE INSTITUTOS (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            codigo_instituto VARCHAR(20) NOT NULL UNIQUE,
                            nombre VARCHAR(100) NOT NULL,
                            ciudad VARCHAR(50),
                            direccion VARCHAR(100),
                            telefono VARCHAR(20),
                            email VARCHAR(100),
                            numero_profesores INT,
                            tipo VARCHAR(50),
                            anio_fundacion DATE,
                            is_deleted BOOLEAN DEFAULT FALSE,
                            uuid CHAR(36) NOT NULL UNIQUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
);

-- =========================================================================
-- Crear tabla ESTUDIANTES (entidad dependiente)
-- =========================================================================
CREATE TABLE ESTUDIANTES (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(100) NOT NULL,
                             apellidos VARCHAR(100) NOT NULL,
                             dni VARCHAR(9) NOT NULL UNIQUE,
                             email VARCHAR(100) NOT NULL UNIQUE,
                             fecha_nacimiento DATE NOT NULL,
                             instituto_id BIGINT NOT NULL,
                             is_deleted BOOLEAN DEFAULT FALSE,
                             uuid CHAR(36) NOT NULL UNIQUE,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                             CONSTRAINT fk_estudiantes_institutos FOREIGN KEY (instituto_id) REFERENCES INSTITUTOS(id)
);

-- =========================================================================
-- Insertar datos de ejemplo
-- =========================================================================
INSERT INTO INSTITUTOS (codigo_instituto, nombre, anio_fundacion, ciudad, direccion, telefono, email, numero_profesores, tipo, uuid)
VALUES
    ('INT-0011', 'Instituto Central', '1990-01-01', 'Ciudad A', 'Calle 1', '111-11-11-11', 'central@example.com', 10, 'Público', UUID()),
    ('INT-0022', 'Instituto Norte', '1985-01-01', 'Ciudad B', 'Calle 2', '222-22-22-22', 'norte@example.com', 8, 'Privado', UUID());

INSERT INTO ESTUDIANTES (nombre, apellidos, dni, instituto_id, email, fecha_nacimiento, uuid)
VALUES
    ('Juan', 'Diaz', '11111111A', 1, 'juan.diaz@instituto.com', '2000-05-15', UUID()),
    ('Ana', 'Lopez', '22222222B', 1, 'ana.lopez@instituto.com', '2001-08-20', UUID()),
    ('Carlos', 'Pérez', '33333333C', 2, 'carlos.perez@instituto.com', '1999-11-10', UUID());

-- =========================================================================
-- Reactivar restricciones
-- =========================================================================
SET REFERENTIAL_INTEGRITY TRUE;
