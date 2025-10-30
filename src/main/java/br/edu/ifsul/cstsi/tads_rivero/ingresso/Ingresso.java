package br.edu.ifsul.cstsi.tads_rivero.ingresso;

import br.edu.ifsul.cstsi.tads_rivero.sessao.Sessao;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingressos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ingresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tipo;

    @ManyToOne
    @JoinColumn(name = "sessao_id")
    private Sessao sessao;

    public int gerIngr() {
        return this.tipo;
    }
}
