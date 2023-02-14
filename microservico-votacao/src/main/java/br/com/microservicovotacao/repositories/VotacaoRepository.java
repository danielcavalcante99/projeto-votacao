package br.com.microservicovotacao.repositories;

import br.com.microservicovotacao.models.Associado;
import br.com.microservicovotacao.models.Pauta;
import br.com.microservicovotacao.models.Votacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VotacaoRepository extends MongoRepository<Votacao, String> {

    List<Votacao> findByAssociadoAndPauta(Associado associado, Pauta pauta);
}