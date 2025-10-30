package br.edu.ifsul.cstsi.tads_rivero.filme;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FilmeService {

    private final FilmeRepository repository;

    public FilmeService(FilmeRepository repository) {
        this.repository = repository;
    }

    public List<Filme> findAll() {
        return repository.findAll();
    }

    public Optional<Filme> findById(Long id) {
        return repository.findById(id);
    }

    public List<Filme> findByTitulo(String titulo) {
        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    @Transactional
    public Filme save(Filme filme) {
        return repository.save(filme);
    }

    @Transactional
    public Filme update(Long id, Filme filmeAtualizado) {
        Filme filme = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado com id: " + id));

        filme.setTitulo(filmeAtualizado.getTitulo());
        filme.setDuracao(filmeAtualizado.getDuracao());

        return repository.save(filme);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Filme não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
