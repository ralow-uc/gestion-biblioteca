package com.biblioteca.gestionbiblioteca.controller;

import com.biblioteca.gestionbiblioteca.dto.LibroRequest;
import com.biblioteca.gestionbiblioteca.repository.LibroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de los endpoints HTTP del controlador REST.
 * Replican las mismas solicitudes que se ejecutan manualmente en Postman.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LibroRepository libroRepository;

    @BeforeEach
    void limpiarTabla() {
        libroRepository.deleteAll();
    }

    private String json(LibroRequest solicitud) throws Exception {
        return objectMapper.writeValueAsString(solicitud);
    }

    private Long crearLibroDePrueba() throws Exception {
        String respuesta = mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LibroRequest("Rayuela", "Julio Cortazar", 1963, "Novela"))))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(respuesta).get("id").asLong();
    }

    @Test
    @DisplayName("POST /api/libros responde 201 con el libro creado")
    void postCreaLibro() throws Exception {
        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LibroRequest("Clean Code", "Robert C. Martin", 2008, "Informatica"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("Clean Code"))
                .andExpect(jsonPath("$.autor").value("Robert C. Martin"))
                .andExpect(jsonPath("$.anioPublicacion").value(2008))
                .andExpect(jsonPath("$.genero").value("Informatica"));
    }

    @Test
    @DisplayName("POST /api/libros responde 400 si faltan atributos obligatorios")
    void postSinTituloEsRechazado() throws Exception {
        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LibroRequest("  ", "Autor", 2000, "Novela"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.titulo").exists());
    }

    @Test
    @DisplayName("GET /api/libros/{id} responde 200 con el libro solicitado")
    void getPorIdDevuelveElLibro() throws Exception {
        Long id = crearLibroDePrueba();

        mockMvc.perform(get("/api/libros/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titulo").value("Rayuela"));
    }

    @Test
    @DisplayName("GET /api/libros/{id} responde 404 si el ID no existe")
    void getPorIdInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/api/libros/{id}", 9999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Libro no encontrado"));
    }

    @Test
    @DisplayName("GET /api/libros lista los libros y filtra por titulo")
    void getListaYFiltra() throws Exception {
        crearLibroDePrueba();

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/api/libros").param("titulo", "ray"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Rayuela"));
    }

    @Test
    @DisplayName("PUT /api/libros/{id} responde 200 con el libro actualizado")
    void putActualizaElLibro() throws Exception {
        Long id = crearLibroDePrueba();

        mockMvc.perform(put("/api/libros/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LibroRequest("Rayuela", "Julio Cortazar", 1963, "Novela experimental"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.genero").value("Novela experimental"));
    }

    @Test
    @DisplayName("DELETE /api/libros/{id} responde 204 y deja de encontrar el libro")
    void deleteEliminaElLibro() throws Exception {
        Long id = crearLibroDePrueba();

        mockMvc.perform(delete("/api/libros/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/libros/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/estado responde 200 con estado OK")
    void getEstadoDevuelveOk() throws Exception {
        mockMvc.perform(get("/api/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("OK"))
                .andExpect(jsonPath("$.baseDeDatos.conectada").value(true));
    }
}
