package com.biblioteca.gestionbiblioteca.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos que el cliente envia en las solicitudes POST y PUT.
 *
 * Se usa un DTO en lugar de la entidad para que el cliente no pueda forzar el ID
 * ni enviar campos que no corresponden a la operacion.
 */
public record LibroRequest(

        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 200, message = "El titulo no puede superar los 200 caracteres")
        String titulo,

        @NotBlank(message = "El autor es obligatorio")
        @Size(max = 150, message = "El autor no puede superar los 150 caracteres")
        String autor,

        @NotNull(message = "El anio de publicacion es obligatorio")
        @Min(value = 1450, message = "El anio de publicacion debe ser posterior a 1450")
        @Max(value = 2100, message = "El anio de publicacion no puede ser mayor a 2100")
        Integer anioPublicacion,

        @NotBlank(message = "El genero es obligatorio")
        @Size(max = 100, message = "El genero no puede superar los 100 caracteres")
        String genero) {
}
