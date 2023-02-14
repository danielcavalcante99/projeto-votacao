package br.com.microservicovotacao.repositories;

import br.com.microservicovotacao.models.Pauta;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PautaRepository extends MongoRepository<Pauta, String> {

    List<Pauta> findByIdAndSessaoAtivaOrderByDataCadastroAsc(String id, boolean Sessao);
}
