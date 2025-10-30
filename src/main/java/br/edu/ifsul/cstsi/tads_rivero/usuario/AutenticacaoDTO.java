package br.edu.ifsul.cstsi.tads_rivero.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AutenticacaoDTO(
    @NotBlank(message = "O email não pode ser nulo ou vazio")
    @Email(message = "Email deve ser válido")
    String email,

    @NotBlank(message = "A senha não pode ser nula ou vazia")
    String senha
) {
}
