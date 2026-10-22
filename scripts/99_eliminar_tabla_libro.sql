-- ===========================================================================
--  Script de limpieza: elimina el esquema creado por 01_crear_tabla_libro.sql.
--  Usar solo si necesitas volver a crear la tabla desde cero.
-- ===========================================================================

DROP TABLE LIBRO PURGE;
DROP SEQUENCE LIBRO_SEQ;
