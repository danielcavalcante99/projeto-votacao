package br.com.apivotacao.dtos;

import br.com.apivotacao.models.Pauta;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotalVotosDTO {

    private Pauta pauta;
    private Long totalVotosSim;
    private Long totalVotosNao;
}
