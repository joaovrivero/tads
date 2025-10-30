package br.edu.ifsul.cstsi.tads_rivero.sala;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
}