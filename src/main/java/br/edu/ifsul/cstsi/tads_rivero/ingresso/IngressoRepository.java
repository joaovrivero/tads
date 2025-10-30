package br.edu.ifsul.cstsi.tads_rivero.ingresso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
}