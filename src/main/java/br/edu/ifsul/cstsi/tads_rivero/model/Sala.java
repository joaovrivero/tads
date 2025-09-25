package br.edu.ifsul.cstsi.tads_rivero.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int nrosala;
    private int capacidade;

    @OneToMany(mappedBy = "sala", fetch = FetchType.EAGER)
    private List<Sessao> sessoes = new ArrayList<>();

    public int onsala() {
        return this.nrosala;
    }
}