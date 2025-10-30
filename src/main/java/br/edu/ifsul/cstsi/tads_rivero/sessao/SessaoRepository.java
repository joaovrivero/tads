package br.edu.ifsul.cstsi.tads_rivero.sessao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
}