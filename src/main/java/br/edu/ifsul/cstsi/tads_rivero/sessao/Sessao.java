package br.edu.ifsul.cstsi.tads_rivero.sessao;

import br.edu.ifsul.cstsi.tads_rivero.filme.Filme;
import br.edu.ifsul.cstsi.tads_rivero.ingresso.Ingresso;
import br.edu.ifsul.cstsi.tads_rivero.sala.Sala;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessoes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dtSessao;
    private LocalTime horSessao;
    private double valorInteira;
    private double valorMeia;
    private int encerrada;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @ManyToOne
    @JoinColumn(name = "filme_id")
    private Filme filme;

    @OneToMany(mappedBy = "sessao", fetch = FetchType.EAGER)
    private List<Ingresso> ingressos = new ArrayList<>();

    public String selSessao() {
        return "Sessão de " + filme.getTitulo() + " em " + dtSessao + " às " + horSessao;
    }
}
