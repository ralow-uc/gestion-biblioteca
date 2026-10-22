package com.biblioteca.gestionbiblioteca;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Comprueba que el contexto de Spring Boot arranca con todos los beans
 * (entidad, repositorio JPA, servicio y controlador REST).
 */
@SpringBootTest
@ActiveProfiles("test")
class GestionBibliotecaApplicationTests {

    @Test
    void elContextoDeSpringBootCarga() {
        // El test falla si algun bean no se puede construir.
    }
}
