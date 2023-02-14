package br.com.apivotacao.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("associados")
public class Associado implements Serializable {

    private static final long serialVersionUID = 8775514928714031982L;

    @Id
    private String id;

    private String nome;

    @Indexed(unique = true)
    private String cpf;
}
