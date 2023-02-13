package br.com.apivotacao.repositories;


import br.com.apivotacao.models.Associado;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssociadoRepository extends MongoRepository<Associado, String> {
}
