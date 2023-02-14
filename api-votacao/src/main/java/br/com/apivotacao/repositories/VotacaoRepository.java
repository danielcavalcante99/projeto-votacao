package br.com.apivotacao.repositories;

import br.com.apivotacao.models.Votacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VotacaoRepository extends MongoRepository<Votacao, String> {

    List<Votacao> findByPauta_Id(String idPauta);
    
}
