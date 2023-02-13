package br.com.apivotacao.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociadoDTO {

    @NotBlank(message = "Nome do Associado não pode está nulo ou vazio!")
    private String nome;

    @NotBlank(message = "CPF do Associado não pode está nulo ou vazio!")
    private String cpf;
}
