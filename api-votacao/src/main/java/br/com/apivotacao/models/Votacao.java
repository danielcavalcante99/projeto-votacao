package br.com.apivotacao.models;

import br.com.apivotacao.models.enums.TipoVoto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("votacoes")
public class Votacao implements Serializable {
    private static final long serialVersionUID = 3478229786652459444L;

    @Id
    private String id;

    @DBRef
    private Associado associado;

    @DBRef
    private Pauta pauta;

    private TipoVoto tipoVoto;

    private LocalDateTime dataVoto;
}
