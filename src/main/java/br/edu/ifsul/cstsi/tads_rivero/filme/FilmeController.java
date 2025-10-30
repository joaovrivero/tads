package br.edu.ifsul.cstsi.tads_rivero.filme;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/v1/filmes")
public class FilmeController {

    private final FilmeService service;

    public FilmeController(FilmeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FilmeDTOResponse>> findAll() {
        List<FilmeDTOResponse> filmes = service
            .findAll()
            .stream()
            .map(FilmeDTOResponse::new)
            .toList();
        return ResponseEntity.ok(filmes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmeDTOResponse> findById(@PathVariable Long id) {
        return service
            .findById(id)
            .map(filme -> ResponseEntity.ok(new FilmeDTOResponse(filme)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<FilmeDTOResponse>> findByTitulo(
        @PathVariable String titulo
    ) {
        List<FilmeDTOResponse> filmes = service
            .findByTitulo(titulo)
            .stream()
            .map(FilmeDTOResponse::new)
            .toList();

        if (filmes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(filmes);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<FilmeDTOResponse> insert(
        @Valid @RequestBody FilmeDTOPost dto
    ) {
        Filme filme = new Filme();
        filme.setTitulo(dto.titulo());
        filme.setDuracao(dto.duracao());

        Filme filmeSalvo = service.save(filme);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(filmeSalvo.getId())
            .toUri();

        return ResponseEntity.created(location).body(
            new FilmeDTOResponse(filmeSalvo)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<FilmeDTOResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody FilmeDTOPut dto
    ) {
        try {
            Filme filme = new Filme();
            filme.setTitulo(dto.titulo());
            filme.setDuracao(dto.duracao());

            Filme filmeAtualizado = service.update(id, filme);
            return ResponseEntity.ok(new FilmeDTOResponse(filmeAtualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
