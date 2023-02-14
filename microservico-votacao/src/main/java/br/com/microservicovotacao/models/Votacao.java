package br.com.microservicovotacao.models;

import br.com.microservicovotacao.models.enums.TipoVoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("votacao")
public class Votacao implements Serializable {

    private static final long serialVersionUID = 5829669344958151372L;
    @Id
    private String id;

    @DBRef
    private Associado associado;

    @DBRef
    private Pauta pauta;

    private TipoVoto tipoVoto;
    private LocalDateTime dataVoto ;

}