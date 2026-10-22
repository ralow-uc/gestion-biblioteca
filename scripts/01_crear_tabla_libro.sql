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
-- 4. Datos iniciales: obras de autores chilenos
-- ---------------------------------------------------------------------------

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Desolación', 'Gabriela Mistral', 1922, 'Poesía');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Veinte poemas de amor y una canción desesperada', 'Pablo Neruda', 1924, 'Poesía');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Papelucho', 'Marcela Paz', 1947, 'Literatura infantil');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Hijo de ladrón', 'Manuel Rojas', 1951, 'Novela');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Poemas y antipoemas', 'Nicanor Parra', 1954, 'Antipoesía');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'El obsceno pájaro de la noche', 'José Donoso', 1970, 'Novela');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'La casa de los espíritus', 'Isabel Allende', 1982, 'Realismo mágico');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Ardiente paciencia', 'Antonio Skármeta', 1985, 'Novela');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Un viejo que leía novelas de amor', 'Luis Sepúlveda', 1989, 'Novela');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Los detectives salvajes', 'Roberto Bolaño', 1998, 'Novela');

INSERT INTO LIBRO (ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO)
VALUES (LIBRO_SEQ.NEXTVAL, 'Formas de volver a casa', 'Alejandro Zambra', 2011, 'Novela');

COMMIT;

-- ---------------------------------------------------------------------------
-- 5. Verificacion
-- ---------------------------------------------------------------------------
SELECT ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO
FROM   LIBRO
ORDER  BY ID;
