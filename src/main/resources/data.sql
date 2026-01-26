-- ==========================================================
-- DATOS FÁCILES DE DEPURAR
-- 50 INSTITUTOS + 50 ESTUDIANTES (NOMBRES ESPAÑOLES)
-- ==========================================================

SET REFERENTIAL_INTEGRITY FALSE;

DROP TABLE IF EXISTS ESTUDIANTE_ROLES;
DROP TABLE IF EXISTS ESTUDIANTES;
DROP TABLE IF EXISTS INSTITUTOS;

SET REFERENTIAL_INTEGRITY TRUE;

-- ==========================================================
-- TABLAS
-- ==========================================================

CREATE TABLE IF NOT EXISTS INSTITUTOS (
                                          ID BIGINT AUTO_INCREMENT PRIMARY KEY,

                                          CODIGO_INSTITUTO VARCHAR(20) NOT NULL UNIQUE,
                                          NOMBRE VARCHAR(100) NOT NULL,
                                          CIUDAD VARCHAR(50),
                                          DIRECCION VARCHAR(100),
                                          TELEFONO VARCHAR(20),
                                          EMAIL VARCHAR(100),
                                          NUMERO_PROFESORES INT,
                                          TIPO VARCHAR(50),
                                          ANIO_FUNDACION DATE,

                                          IS_DELETED BOOLEAN DEFAULT FALSE,
                                          UUID UUID NOT NULL UNIQUE,
                                          CREATED_AT TIMESTAMP NOT NULL,
                                          UPDATED_AT TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ESTUDIANTES (
                                           ID BIGINT AUTO_INCREMENT PRIMARY KEY,

                                           NOMBRE VARCHAR(100) NOT NULL,
                                           APELLIDOS VARCHAR(100) NOT NULL,
                                           DNI VARCHAR(9) NOT NULL UNIQUE,

                                           FECHA_NACIMIENTO DATE NOT NULL,

                                           USERNAME VARCHAR(100) NOT NULL UNIQUE,
                                           PASSWORD VARCHAR(255) NOT NULL,
                                           EMAIL VARCHAR(100) NOT NULL UNIQUE,

                                           INSTITUTO_ID BIGINT NOT NULL,

                                           IS_DELETED BOOLEAN DEFAULT FALSE,
                                           UUID UUID NOT NULL UNIQUE,
                                           CREATED_AT TIMESTAMP NOT NULL,
                                           UPDATED_AT TIMESTAMP NOT NULL,

                                           CONSTRAINT FK_ESTUDIANTE_INSTITUTO
                                               FOREIGN KEY (INSTITUTO_ID) REFERENCES INSTITUTOS(ID)
);

CREATE TABLE IF NOT EXISTS ESTUDIANTE_ROLES (
                                                ESTUDIANTE_ID BIGINT NOT NULL,
                                                ROLES VARCHAR(50) NOT NULL,

                                                CONSTRAINT FK_ESTUDIANTE_ROLES
                                                    FOREIGN KEY (ESTUDIANTE_ID) REFERENCES ESTUDIANTES(ID)
);

-- ==========================================================
-- 50 INSTITUTOS (insert fácil)
-- ==========================================================

INSERT INTO INSTITUTOS (
    CODIGO_INSTITUTO, NOMBRE, CIUDAD, DIRECCION,
    TELEFONO, EMAIL, NUMERO_PROFESORES, TIPO,
    ANIO_FUNDACION, IS_DELETED, UUID, CREATED_AT, UPDATED_AT
) VALUES
      ('INT-0001','IES Quevedo','Madrid','C/ Alcalá 10','910000001','quevedo@edu.es',75,'Publico','1982-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0002','IES Cervantes','Madrid','C/ Gran Vía 50','910000002','cervantes@edu.es',68,'Publico','1975-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0003','IES Velázquez','Sevilla','Av. Andalucía 21','950000003','velazquez@edu.es',55,'Publico','1990-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0004','IES Goya','Zaragoza','C/ Independencia 12','976000004','goya@edu.es',60,'Publico','1988-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0005','IES Picasso','Málaga','Av. Larios 1','952000005','picasso@edu.es',72,'Publico','1995-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('INT-0006','IES Machado','Soria','C/ Collado 2','975000006','machado@edu.es',40,'Publico','1970-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0007','IES Lorca','Granada','C/ Reyes Católicos 8','958000007','lorca@edu.es',58,'Publico','1980-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0008','IES Unamuno','Bilbao','C/ Ercilla 9','944000008','unamuno@edu.es',47,'Publico','1986-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0009','IES Rosalía','A Coruña','Av. Marina 3','981000009','rosalia@edu.es',52,'Publico','1992-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0010','IES Miró','Barcelona','C/ Diagonal 100','934000010','miro@edu.es',88,'Publico','1965-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('INT-0011','IES Dalí','Figueres','C/ Museo 1','972000011','dali@edu.es',35,'Privado','2001-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0012','IES Gaudí','Tarragona','C/ Modernismo 7','977000012','gaudi@edu.es',49,'Publico','1998-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0013','IES Colón','Valencia','C/ Colón 22','963000013','colon@edu.es',61,'Publico','1984-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0014','IES Turia','Valencia','Av. Turia 9','963000014','turia@edu.es',57,'Publico','1978-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0015','IES Alhambra','Granada','C/ Albaicín 5','958000015','alhambra@edu.es',45,'Privado','2005-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('INT-0016','IES Atlántico','Cádiz','C/ Mar 4','956000016','atlantico@edu.es',38,'Publico','1991-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0017','IES Mediterráneo','Alicante','Av. Costa 18','965000017','mediterraneo@edu.es',53,'Publico','1987-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0018','IES Sierra','Jaén','C/ Olivo 11','953000018','sierra@edu.es',41,'Publico','1983-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0019','IES Nervión','Sevilla','C/ Nervión 15','954000019','nervion@edu.es',66,'Publico','1976-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0020','IES Ebro','Logroño','C/ Laurel 6','941000020','ebro@edu.es',44,'Publico','1993-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('INT-0021','IES Duero','Valladolid','C/ Mayor 30','983000021','duero@edu.es',50,'Publico','1981-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0022','IES Tajo','Toledo','C/ Catedral 1','925000022','tajo@edu.es',39,'Publico','1979-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0023','IES Guadiana','Badajoz','Av. Extremadura 99','924000023','guadiana@edu.es',42,'Publico','1989-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0024','IES Segura','Murcia','C/ Huerta 14','968000024','segura@edu.es',64,'Publico','1996-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0025','IES Júcar','Albacete','C/ Feria 2','967000025','jucar@edu.es',37,'Publico','1994-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('INT-0026','IES Cantábrico','Santander','Paseo Pereda 3','942000026','cantabrico@edu.es',55,'Publico','1985-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0027','IES Pirineos','Huesca','C/ Peña 10','974000027','pirineos@edu.es',33,'Publico','2002-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0028','IES Teide','Tenerife','Av. Volcán 1','922000028','teide@edu.es',48,'Publico','1999-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0029','IES Calima','Las Palmas','C/ Playa 7','928000029','calima@edu.es',36,'Privado','2003-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0030','IES Marisma','Huelva','C/ Doñana 5','959000030','marisma@edu.es',40,'Publico','1997-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('INT-0031','IES Litoral','Castellón','Av. Mar 8','964000031','litoral@edu.es',46,'Publico','1980-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0032','IES Alborán','Almería','C/ Cabo 12','950000032','alboran@edu.es',52,'Publico','1982-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0033','IES Senda','Cuenca','C/ Puente 1','969000033','senda@edu.es',29,'Publico','2000-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0034','IES Niebla','Salamanca','C/ Plaza 9','923000034','niebla@edu.es',43,'Publico','1974-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0035','IES Encina','Cáceres','C/ Bosque 4','927000035','encina@edu.es',31,'Publico','1986-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('INT-0036','IES Prado','Ciudad Real','C/ Mancha 13','926000036','prado@edu.es',39,'Publico','1988-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0037','IES Altamira','Santillana','C/ Cuevas 1','942000037','altamira@edu.es',28,'Privado','2004-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0038','IES Rías','Pontevedra','C/ Ría 6','986000038','rias@edu.es',47,'Publico','1991-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0039','IES Aneto','Lleida','C/ Montaña 2','973000039','aneto@edu.es',34,'Publico','1990-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0040','IES Alisio','Lanzarote','C/ Viento 3','928000040','alisio@edu.es',27,'Publico','2006-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('INT-0041','IES Estrella','Pamplona','C/ Navarra 12','948000041','estrella@edu.es',44,'Publico','1983-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0042','IES Horizonte','Gijón','Av. Costa 3','984000042','horizonte@edu.es',51,'Publico','1987-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0043','IES Camino','León','C/ Catedral 7','987000043','camino@edu.es',36,'Publico','1977-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0044','IES Minerva','Oviedo','C/ Universidad 1','984000044','minerva@edu.es',62,'Publico','1992-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0045','IES Aurelio','Teruel','C/ Mudéjar 4','978000045','aurelio@edu.es',30,'Privado','2002-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('INT-0046','IES Moncayo','Soria','C/ Castillo 2','975000046','moncayo@edu.es',26,'Publico','1998-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0047','IES Mirador','Segovia','C/ Acueducto 1','921000047','mirador@edu.es',37,'Publico','1985-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0048','IES Vega','Zamora','C/ Duero 2','980000048','vega@edu.es',33,'Publico','1990-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0049','IES Alba','Burgos','C/ Catedral 11','947000049','alba@edu.es',41,'Publico','1981-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('INT-0050','IES Aurora','Palencia','C/ Mayor 5','979000050','aurora@edu.es',29,'Privado','2007-01-01',FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

-- ==========================================================
-- 50 ESTUDIANTES (NOMBRES ESPAÑOLES)
-- ==========================================================
-- Password BCrypt de "1234" (válida para login)
-- ==========================================================

INSERT INTO ESTUDIANTES (
    NOMBRE, APELLIDOS, DNI, FECHA_NACIMIENTO,
    USERNAME, PASSWORD, EMAIL, INSTITUTO_ID,
    IS_DELETED, UUID, CREATED_AT, UPDATED_AT
) VALUES
      ('Juan','García López','00000001A','2004-02-12','user1','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno1@instituto.com',1,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('María','Fernández Ruiz','00000002A','2003-07-21','user2','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno2@instituto.com',2,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Carlos','Martínez Sánchez','00000003A','2005-01-15','user3','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno3@instituto.com',3,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Lucía','Pérez Gómez','00000004A','2004-11-09','user4','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno4@instituto.com',4,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('David','Romero Díaz','00000005A','2003-05-30','user5','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno5@instituto.com',5,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Paula','Navarro Torres','00000006A','2004-09-14','user6','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno6@instituto.com',6,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Alejandro','Gutiérrez Ramos','00000007A','2003-12-03','user7','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno7@instituto.com',7,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Sara','Molina Castro','00000008A','2005-03-19','user8','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno8@instituto.com',8,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Javier','Ortiz Vega','00000009A','2004-06-22','user9','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno9@instituto.com',9,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Marta','Serrano Campos','00000010A','2003-04-08','user10','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno10@instituto.com',10,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Hugo','Iglesias Pardo','00000050A','2004-10-10','user50','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno50@instituto.com',50,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Álvaro','Suárez Medina','00000051A','2004-01-20','user51','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno51@instituto.com',1,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Andrea','Cano Morales','00000052A','2003-08-11','user52','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno52@instituto.com',2,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Sergio','Hernández Núñez','00000053A','2004-05-03','user53','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno53@instituto.com',3,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Elena','Lorenzo Gil','00000054A','2005-02-27','user54','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno54@instituto.com',4,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Mario','Vidal Romero','00000055A','2003-10-09','user55','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno55@instituto.com',5,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Carmen','Santos Prieto','00000056A','2004-09-18','user56','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno56@instituto.com',6,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Diego','Aguilar Rojas','00000057A','2005-06-14','user57','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno57@instituto.com',7,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Isabel','Domínguez Ortega','00000058A','2004-12-02','user58','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno58@instituto.com',8,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Pablo','Crespo Blanco','00000059A','2003-03-25','user59','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno59@instituto.com',9,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Noelia','Herrera Fuentes','00000060A','2004-07-07','user60','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno60@instituto.com',10,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Iván','Méndez Molina','00000061A','2005-01-13','user61','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno61@instituto.com',11,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Laura','Calvo Ramos','00000062A','2003-11-29','user62','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno62@instituto.com',12,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Raúl','Pascual Vega','00000063A','2004-04-05','user63','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno63@instituto.com',13,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Patricia','Rubio León','00000064A','2003-02-16','user64','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno64@instituto.com',14,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Adrián','Sánchez Soto','00000065A','2004-06-30','user65','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno65@instituto.com',15,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Natalia','Carrasco Peña','00000066A','2005-09-01','user66','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno66@instituto.com',16,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Marcos','Reyes Campos','00000067A','2004-08-19','user67','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno67@instituto.com',17,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Rocío','Vázquez Cruz','00000068A','2003-05-12','user68','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno68@instituto.com',18,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Rubén','Garrido Castillo','00000069A','2004-03-03','user69','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno69@instituto.com',19,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Beatriz','Flores Vega','00000070A','2005-12-15','user70','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno70@instituto.com',20,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Óscar','Nieto Navarro','00000071A','2003-01-08','user71','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno71@instituto.com',21,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Irene','Guerrero Serrano','00000072A','2004-10-21','user72','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno72@instituto.com',22,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Héctor','Moreno Vidal','00000073A','2005-04-09','user73','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno73@instituto.com',23,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Claudia','Cabrera Iglesias','00000074A','2003-09-27','user74','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno74@instituto.com',24,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Nicolás','Luna Ortega','00000075A','2004-02-03','user75','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno75@instituto.com',25,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Julia','Cortés Márquez','00000076A','2003-06-17','user76','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno76@instituto.com',26,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Manuel','Arias Delgado','00000077A','2004-11-06','user77','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno77@instituto.com',27,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Silvia','Marín Sánchez','00000078A','2005-08-24','user78','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno78@instituto.com',28,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Gonzalo','Vega Prieto','00000079A','2004-05-29','user79','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno79@instituto.com',29,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Teresa','Sanz Herrera','00000080A','2003-03-10','user80','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno80@instituto.com',30,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Ángel','Sierra Benítez','00000081A','2004-07-22','user81','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno81@instituto.com',31,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Aitana','Gil Pardo','00000082A','2005-01-04','user82','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno82@instituto.com',32,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Borja','Soto Vargas','00000083A','2003-12-28','user83','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno83@instituto.com',33,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Carla','Vargas Núñez','00000084A','2004-04-14','user84','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno84@instituto.com',34,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Víctor','Bravo Santos','00000085A','2003-09-09','user85','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno85@instituto.com',35,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Eva','Ramos Medina','00000086A','2004-06-02','user86','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno86@instituto.com',36,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Daniel','Campos León','00000087A','2005-10-16','user87','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno87@instituto.com',37,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Lidia','Márquez Nieto','00000088A','2003-02-08','user88','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno88@instituto.com',38,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Samuel','Cruz Sánchez','00000089A','2004-11-23','user89','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno89@instituto.com',39,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Sofía','Peña Serrano','00000090A','2005-04-30','user90','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno90@instituto.com',40,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Luis','Ibarra Calvo','00000091A','2003-07-19','user91','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno91@instituto.com',41,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Miriam','Sánchez Cano','00000092A','2004-03-07','user92','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno92@instituto.com',42,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Cristina','Rojas Álvarez','00000093A','2005-12-04','user93','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno93@instituto.com',43,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Ignacio','Álvarez Vidal','00000094A','2004-09-09','user94','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno94@instituto.com',44,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Nerea','Salas Romero','00000095A','2003-01-31','user95','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno95@instituto.com',45,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),

      ('Tomás','Giménez Fuentes','00000096A','2004-05-18','user96','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno96@instituto.com',46,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Alicia','Benítez Prieto','00000097A','2005-08-08','user97','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno97@instituto.com',47,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Bruno','Martín Ortega','00000098A','2004-02-12','user98','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno98@instituto.com',48,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Inés','Delgado Sanz','00000099A','2003-10-26','user99','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno99@instituto.com',49,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
      ('Hugo','Paredes Molina','00000100A','2004-06-06','user100','$2a$10$7QkX3q8Qq2cZ7d7mJ9I1pO6h4o7bIu8x7E9N0cYdZQp9Gx2i3W6mK','alumno100@instituto.com',50,FALSE,RANDOM_UUID(),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

-- ==========================================================
-- ROLES
-- ==========================================================

-- Todos USER
INSERT INTO ESTUDIANTE_ROLES (ESTUDIANTE_ID, ROLES)
SELECT ID, 'USER' FROM ESTUDIANTES;

-- El primero además ADMIN
INSERT INTO ESTUDIANTE_ROLES (ESTUDIANTE_ID, ROLES)
SELECT MIN(ID), 'ADMIN' FROM ESTUDIANTES;


