package br.edu.ifsul.cstsi.tads_rivero.filme;

import java.time.LocalTime;

/**
 * DTO for {@link Filme}
 */
public record FilmeDTOResponse(
    Long id,
    String titulo,
    LocalTime duracao
) {
    public FilmeDTOResponse(Filme filme) {
        this(filme.getId(), filme.getTitulo(), filme.getDuracao());
    }
}
