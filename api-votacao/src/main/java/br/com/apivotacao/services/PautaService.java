package br.com.apivotacao.services;

import br.com.apivotacao.exceptions.ResourceNotFoundException;
import br.com.apivotacao.models.Pauta;
import br.com.apivotacao.repositories.PautaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PautaService {

    private final EventoFilaService eventoFilaService;

    private final PautaRepository repository;

    private final String TOPICO_ENCERRAR_SESSAO = "encerrarSessaoVotos";


    public Optional<Pauta> buscarPorId(String id) {
        return this.repository.findById(id);
    }

    public List<Pauta> buscarTodos() {
        return this.repository.findAll();
    }

    /**
     * @author Daniel Cavalcante
     *
     * Método salvar — Cadastra a pauta e envia para fila o evento
     * para encerrar a sessão em um determinado tempo.
     *
     * @param pauta Pauta — pauta.
     */
    public void salvar(Pauta pauta) {
        Pauta entity = this.repository.save(pauta);
        this.eventoFilaService.add(this.TOPICO_ENCERRAR_SESSAO, entity.getId());
    }

    /**
     * @author Daniel Cavalcante
     *
     * Método encerrarSessaoVotosPauta — Finalizará a sessão de votação da pauta e também
     * irá adicionar a data do encerramento da sessão.
     *
     * @param id String — idPauta.
     *
     * @return Pauta — Retornará a pauta.
     *
     * @throws ResourceNotFoundException
     */
    public Pauta encerrarSessaoVotosPauta(String id) {
        Pauta pauta = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não será possível encerrar a sessão da pauta inesistente!"));

        pauta.setSessaoAtiva(false);
        pauta.setDataEncerramento(LocalDateTime.now());
        this.repository.save(pauta);
        return pauta;
    }

    public void deletarPorId(String id) {
        this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não será possível excluir a pauta, pois não existe!"));

        this.repository.deleteById(id);
    }
}
