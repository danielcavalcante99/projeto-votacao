package br.com.apivotacao.dtos;

import br.com.apivotacao.models.enums.TipoVoto;
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

    private static final long serialVersionUID = 6163424230367092979L;
    private String idPauta;

    private String idAssociado;

    private TipoVoto tipoVoto;

}
