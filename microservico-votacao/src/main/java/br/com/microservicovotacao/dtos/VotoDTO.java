package br.com.microservicovotacao.dtos;

import br.com.microservicovotacao.models.enums.TipoVoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotoDTO implements Serializable {

    private static final long serialVersionUID = -189330610450748699L;

    private String idPauta;

    private String idAssociado;

    private TipoVoto tipoVoto;
}