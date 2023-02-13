package br.com.apivotacao.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("pautas")
public class Pauta implements Serializable {

    private static final long serialVersionUID = 3855168593517640618L;

    @Id
    private String id;

    private String assunto;

    private String descricao;

    private Boolean sessaoAtiva = true;

    private LocalDateTime dataCadastro = LocalDateTime.now();

    private LocalDateTime dataEncerramento;
}
