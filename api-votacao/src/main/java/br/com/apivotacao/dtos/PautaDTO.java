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
public class PautaDTO {

    @NotBlank(message = "Assunto não pode está nulo ou vazio.")
    private String assunto;

    @NotBlank(message = "Descrição não pode está nulo ou vazio.")
    private String descricao;


}
