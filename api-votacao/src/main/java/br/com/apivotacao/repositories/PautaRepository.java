package br.com.apivotacao.repositories;

import br.com.apivotacao.models.Pauta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PautaRepository extends MongoRepository<Pauta, String> {
}
