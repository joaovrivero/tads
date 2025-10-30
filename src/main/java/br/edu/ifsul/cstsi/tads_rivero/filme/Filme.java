package br.edu.ifsul.cstsi.tads_rivero.filme;

import br.edu.ifsul.cstsi.tads_rivero.sessao.Sessao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "filmes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Filme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private LocalTime duracao;

    // @OneToMany(mappedBy = "filme", fetch = FetchType.EAGER)
    // private List<Sessao> sessoes = new ArrayList<>();

    public String confilme() {
        return this.titulo;
    }
}

