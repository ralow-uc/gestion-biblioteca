-- ===========================================================================
--  Microservicio gestion-biblioteca
--  Creacion del esquema en Oracle Autonomous Database.
--
--  Ejecutar como script completo (F5) desde Oracle SQL Developer o desde
--  Database Actions, con el usuario propietario del esquema.
-- ===========================================================================

-- ---------------------------------------------------------------------------
-- 1. Tabla LIBRO
--    Atributos: ID, titulo, autor, anio de publicacion y genero.
-- ---------------------------------------------------------------------------
CREATE TABLE LIBRO (
    ID                NUMBER(19)      NOT NULL,
    TITULO            VARCHAR2(200)   NOT NULL,
    AUTOR             VARCHAR2(150)   NOT NULL,
    ANIO_PUBLICACION  NUMBER(4)       NOT NULL,
    GENERO            VARCHAR2(100)   NOT NULL,
    CONSTRAINT PK_LIBRO PRIMARY KEY (ID),
    CONSTRAINT CK_LIBRO_ANIO CHECK (ANIO_PUBLICACION BETWEEN 1450 AND 2100)
);

COMMENT ON TABLE  LIBRO                   IS 'Libros disponibles en la biblioteca';
COMMENT ON COLUMN LIBRO.ID                IS 'Identificador unico del libro';
COMMENT ON COLUMN LIBRO.TITULO            IS 'Titulo del libro';
COMMENT ON COLUMN LIBRO.AUTOR             IS 'Autor del libro';
COMMENT ON COLUMN LIBRO.ANIO_PUBLICACION  IS 'Anio de publicacion del libro';
COMMENT ON COLUMN LIBRO.GENERO            IS 'Genero literario del libro';

-- ---------------------------------------------------------------------------
-- 2. Secuencia LIBRO_SEQ
--    JPA la usa a traves de @SequenceGenerator(sequenceName = "LIBRO_SEQ",
--    allocationSize = 1); por eso INCREMENT BY debe ser 1.
-- ---------------------------------------------------------------------------
CREATE SEQUENCE LIBRO_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ---------------------------------------------------------------------------
-- 3. Indices de apoyo a las busquedas del microservicio
--    (findByTituloContainingIgnoreCase y findByGeneroIgnoreCase)
-- ---------------------------------------------------------------------------
CREATE INDEX IX_LIBRO_TITULO ON LIBRO (UPPER(TITULO));
CREATE INDEX IX_LIBRO_GENERO ON LIBRO (UPPER(GENERO));

-- ---------------------------------------------------------------------------
-- 4. Datos iniciales de prueba
-- ---------------------------------------------------------------------------
INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Cien anos de soledad', 'Gabriel Garcia Marquez', 1967, 'Realismo magico');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'La casa de los espiritus', 'Isabel Allende', 1982, 'Realismo magico');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Clean Code', 'Robert C. Martin', 2008, 'Informatica');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Learning Spring Boot 3.0', 'Greg L. Turnquist', 2022, 'Informatica');

COMMIT;

-- ---------------------------------------------------------------------------
-- 5. Verificacion
-- ---------------------------------------------------------------------------
SELECT ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO
FROM   LIBRO
ORDER  BY ID;
