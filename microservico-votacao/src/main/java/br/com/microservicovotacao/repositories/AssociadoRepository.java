package br.com.microservicovotacao.repositories;

import br.com.microservicovotacao.models.Associado;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssociadoRepository extends MongoRepository<Associado, String> {
}
