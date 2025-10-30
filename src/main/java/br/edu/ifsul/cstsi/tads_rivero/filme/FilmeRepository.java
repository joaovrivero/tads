package br.edu.ifsul.cstsi.tads_rivero.filme;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    List<Filme> findByTituloContainingIgnoreCase(String titulo);
}
