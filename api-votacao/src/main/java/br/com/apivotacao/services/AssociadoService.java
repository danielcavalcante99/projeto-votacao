package br.com.apivotacao.services;

import br.com.apivotacao.exceptions.BusinessException;
import br.com.apivotacao.models.Associado;
import br.com.apivotacao.repositories.AssociadoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AssociadoService {

    private final AssociadoRepository repository;

    private final ValidarCPFService validarCPFService;

    public Optional<Associado> buscarPorId(String id) {
        return this.repository.findById(id);
    }

    public List<Associado> buscarTodos() {
        return this.repository.findAll();
    }

    /**
     * @author Daniel Cavalcante
     *
     * Método salvar — Cadastra o associado e verifica se o CPF está válido pora que o mesmo
     * possa participar da processo das votações por pauta.
     *
     * @param associado Associado — associado.
     *
     * @return Associado — Retornará o associado.
     *
     * @throws BusinessException
     */
    public Associado salvar(Associado associado) {
        boolean cpfValid = this.validarCPFService.isValid(associado.getCpf());
        if (cpfValid) {
            return this.repository.save(associado);
        } else {
            String error = String.format("Associado não poderá ser cadastrado para realizar a votação, pois CPF: %s está inválido!", associado.getCpf());
            log.error(error);
            throw new BusinessException(error);
        }
    }

    public void deletarPorId(String id) {
        this.repository.deleteById(id);
    }

}
