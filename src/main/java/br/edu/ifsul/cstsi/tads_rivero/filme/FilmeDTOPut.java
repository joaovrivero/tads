package br.edu.ifsul.cstsi.tads_rivero.filme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

/**
 * DTO for {@link Filme}
 */
public record FilmeDTOPut(
    @NotBlank(message = "O título não pode ser nulo ou vazio")
    @Size(min = 2, max = 200, message = "Tamanho mínimo de 2 e máximo de 200")
    String titulo,

    @NotNull(message = "A duração não pode ser nula")
    LocalTime duracao
) {
    public FilmeDTOPut(Filme filme) {
        this(filme.getTitulo(), filme.getDuracao());
    }
}
