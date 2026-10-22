# gestion-biblioteca

Microservicio REST para la gestión de los libros de una biblioteca, construido con
**Java 21** y **Spring Boot 3.5.16**, con persistencia en **Oracle Autonomous
Database** a través de **JPA / Hibernate**.

---

## 1. Arquitectura

El microservicio sigue una arquitectura por capas, con la lógica de negocio
aislada tanto del transporte HTTP como del acceso a datos:

```
Cliente (Postman / navegador)
        │  HTTP + JSON
        ▼
  LibroController          @RestController   ← capa de presentación
        │
        ▼
  LibroService / LibroServiceImpl   @Service ← capa de negocio
        │
        ▼
  LibroRepository          @Repository (JpaRepository)  ← acceso a datos
        │  JPA / Hibernate
        ▼
  Oracle Autonomous Database — tabla LIBRO
```

Los datos que entran y salen del controlador viajan como DTO (`LibroRequest` y
`LibroResponse`), no como la entidad. Así el cliente no puede forzar el `ID` ni
enviar campos que no corresponden a la operación, y el esquema de la API queda
desacoplado del modelo de persistencia.

### Estructura

```
gestion-biblioteca/
├── pom.xml                              Dependencias y build (Maven)
├── mvnw, mvnw.cmd, .mvn/                Maven Wrapper (Maven 3.9.16)
├── scripts/
│   ├── 01_crear_tabla_libro.sql         Esquema Oracle: tabla, secuencia, índices
│   ├── 99_eliminar_tabla_libro.sql      Limpieza del esquema
│   └── run-oracle.sh                    Arranque contra Oracle (Wallet + Llavero)
├── postman/
│   └── gestion-biblioteca.postman_collection.json
├── docs/
│   └── registros/                       Salidas reales de ejecución y pruebas
└── src/
    ├── main/java/com/biblioteca/gestionbiblioteca/
    │   ├── GestionBibliotecaApplication.java
    │   ├── model/Libro.java                      Entidad JPA
    │   ├── repository/LibroRepository.java       Repositorio JPA
    │   ├── service/LibroService.java             Contrato de servicio
    │   ├── service/impl/LibroServiceImpl.java    Lógica CRUD
    │   ├── controller/LibroController.java       Controlador REST
    │   ├── controller/EstadoController.java      Estado del servicio y de la BD
    │   ├── dto/LibroRequest.java                 Entrada validada
    │   ├── dto/LibroResponse.java                Salida
    │   ├── exception/                            Errores 400 y 404 con detalle
    │   └── config/DatosDeEjemplo.java            Semilla del perfil local
    ├── main/resources/
    │   ├── application.properties
    │   ├── application-local.properties          H2 en memoria
    │   └── application-oracle.properties         Oracle Autonomous Database
    └── test/java/...                             17 pruebas automatizadas
```

---

## 2. Modelo de datos

### Entidad `Libro`

| Atributo Java | Columna Oracle | Tipo | Restricción |
|---|---|---|---|
| `id` | `ID` | `NUMBER(19)` | PK, generado por la secuencia `LIBRO_SEQ` |
| `titulo` | `TITULO` | `VARCHAR2(200)` | obligatorio |
| `autor` | `AUTOR` | `VARCHAR2(150)` | obligatorio |
| `anioPublicacion` | `ANIO_PUBLICACION` | `NUMBER(4)` | obligatorio, entre 1450 y 2100 |
| `genero` | `GENERO` | `VARCHAR2(100)` | obligatorio |

La tabla lleva además dos índices funcionales sobre `UPPER(TITULO)` y
`UPPER(GENERO)` que dan soporte a las búsquedas sin distinción de mayúsculas.

El esquema se crea ejecutando `scripts/01_crear_tabla_libro.sql`. El perfil
`oracle` arranca con `spring.jpa.hibernate.ddl-auto=validate`: Hibernate
comprueba que el mapeo coincida con la tabla real, pero nunca la modifica.

---

## 3. API REST

Base: `http://localhost:8080`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| `GET` | `/api/libros` | Lista los libros. Acepta `?titulo=texto` para filtrar | `200` |
| `GET` | `/api/libros/{id}` | Obtiene un libro por su ID | `200` · `404` |
| `POST` | `/api/libros` | Agrega un libro nuevo | `201` + `Location` · `400` |
| `PUT` | `/api/libros/{id}` | Actualiza un libro existente | `200` · `404` · `400` |
| `DELETE` | `/api/libros/{id}` | Elimina un libro por su ID | `204` · `404` |
| `GET` | `/api/estado` | Estado del servicio y de la conexión a la BD | `200` |

Cuerpo de `POST` y `PUT`:

```json
{
  "titulo": "Rayuela",
  "autor": "Julio Cortazar",
  "anioPublicacion": 1963,
  "genero": "Novela"
}
```

Los errores se devuelven como `application/problem+json`. Una petición inválida
detalla cada campo rechazado:

```json
{
  "title": "Datos del libro invalidos",
  "status": 400,
  "errores": {
    "titulo": "El titulo es obligatorio",
    "anioPublicacion": "El anio de publicacion debe ser posterior a 1450"
  }
}
```

---

## 4. Ejecución

### Requisitos

- **JDK 21** (Eclipse Temurin). Maven no hace falta: se usa el wrapper `./mvnw`.

### 4.1 Perfil `local` — H2 en memoria

Perfil por defecto. Levanta el servicio sin depender de la red ni de
credenciales, con cuatro libros de ejemplo ya cargados:

```bash
./mvnw spring-boot:run
```

- API: `http://localhost:8080/api/libros`
- Consola H2: `http://localhost:8080/h2-console` — JDBC `jdbc:h2:mem:biblioteca`,
  usuario `sa`, sin contraseña.

H2 corre en `MODE=Oracle`, de modo que acepta la misma sintaxis que la base real.

### 4.2 Perfil `oracle` — Oracle Autonomous Database

| | |
|---|---|
| Base | `FULLSTACK3` |
| Motor | Oracle AI Database 26ai Enterprise Edition |
| Servicio TNS | `fullstack3_tp` |
| Conexión | TLS mutuo (mTLS) con Wallet |

**Preparación, una sola vez:**

1. Ejecutar `scripts/01_crear_tabla_libro.sql` contra la base, desde Oracle SQL
   Developer o desde *Database Actions ▸ SQL*.
2. Descargar el Wallet de la base y descomprimirlo en `~/.oracle/wallets/FULLSTACK3`.
3. Guardar la contraseña del usuario de base de datos en el Llavero de macOS:

   ```zsh
   read -s "P?Password: " && \
     security add-generic-password -a "$USER" -s fullstack3-admin -w "$P" -U && \
     unset P
   ```

**Arranque:**

```bash
./scripts/run-oracle.sh
```

El script compone la URL JDBC, lee la contraseña del Llavero y activa el perfil
`oracle`. Equivalente manual:

```bash
export ORACLE_JDBC_URL="jdbc:oracle:thin:@fullstack3_tp?TNS_ADMIN=$HOME/.oracle/wallets/FULLSTACK3"
export ORACLE_USER="..."
export ORACLE_PASSWORD="..."
./mvnw spring-boot:run -Dspring-boot.run.profiles=oracle
```

Para comprobar que quedó conectado, `GET /api/estado` informa el motor real:

```json
{
  "estado": "OK",
  "servicio": "gestion-biblioteca",
  "baseDeDatos": {
    "conectada": true,
    "motor": "Oracle AI Database 26ai Enterprise Edition Release 23.26.4.1.0"
  }
}
```

> **La ruta del Wallet no puede contener espacios.** El driver JDBC de Oracle los
> elimina al parsear el parámetro `TNS_ADMIN` de la URL y la conexión falla con
> `ORA-12263` ("no se puede acceder a tnsnames.ora"), mostrando en el mensaje la
> ruta ya mutilada. Por eso el Wallet vive en `~/.oracle/wallets/`.

### Credenciales

Ninguna credencial se guarda en el repositorio. El perfil `oracle` lee usuario,
contraseña y URL de variables de entorno, y el `.gitignore` excluye los archivos
del Wallet (`*.sso`, `*.p12`, `*.jks`, `tnsnames.ora`, `sqlnet.ora`) y los `.env`.

---

## 5. Pruebas

### 5.1 Automatizadas

```bash
./mvnw test
```

17 pruebas sobre H2 en modo Oracle, de forma que validan el mismo mapeo JPA que
se usa contra la base real sin depender de la red:

| Clase | Cobertura |
|---|---|
| `LibroRepositoryTest` | Mapeo JPA de los cinco atributos y consultas derivadas |
| `LibroServiceImplTest` | CRUD completo por la capa de servicio, incluido el ID inexistente |
| `LibroControllerTest` | Los seis endpoints y sus códigos 200/201/204/400/404 |
| `GestionBibliotecaApplicationTests` | Arranque del contexto de Spring |

### 5.2 Manuales con Postman

1. **File ▸ Import** → `postman/gestion-biblioteca.postman_collection.json`
2. Levantar el servicio (§4) y ejecutar las peticiones en orden, o usar
   **Run collection**.

Cada petición incluye sus propios *tests*. El `POST` guarda el ID generado en la
variable `libroId`, que reutilizan el `GET`, el `PUT` y el `DELETE`, de modo que
la colección se puede correr de principio a fin sin editar nada.

Para contrastar contra la base:

```sql
SELECT ID, TITULO, AUTOR, ANIO_PUBLICACION, GENERO FROM LIBRO ORDER BY ID;
```

En `docs/registros/` quedan las salidas reales de las pruebas automatizadas y de
las ejecuciones del CRUD, tanto en H2 como contra Oracle.

---

## 6. Referencias

Git. (s. f.). *Git documentation*. https://git-scm.com/doc

Oracle. (2026). *Connect to Autonomous Database using JDBC Thin Driver*. Oracle Cloud Infrastructure Documentation. https://docs.oracle.com/en/cloud/paas/autonomous-database/

Spring. (2026). *Spring Boot reference documentation* (versión 3.5.16). https://docs.spring.io/spring-boot/

Turnquist, G., Syer, D., Heckler, M., & Long, J. (2022). *Learning Spring Boot 3.0: Simplify the development of production-grade applications using Java and Spring* (3.ª ed.). Packt Publishing.
