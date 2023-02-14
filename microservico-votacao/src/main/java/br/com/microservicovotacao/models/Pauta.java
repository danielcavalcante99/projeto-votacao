package br.com.microservicovotacao.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("pautas")
public class Pauta implements Serializable {

    private static final long serialVersionUID = -1874804392551468733L;

    @Id
    private String id;


    private String assunto;


    private String descricao;


    private Boolean sessaoAtiva;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataEncerramento;

}
