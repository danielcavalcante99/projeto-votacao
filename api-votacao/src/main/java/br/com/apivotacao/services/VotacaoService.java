package br.com.apivotacao.services;

import br.com.apivotacao.dtos.TotalVotosDTO;
import br.com.apivotacao.dtos.VotoDTO;
import br.com.apivotacao.exceptions.ResourceNotFoundException;
import br.com.apivotacao.models.Votacao;
import br.com.apivotacao.models.enums.TipoVoto;
import br.com.apivotacao.repositories.PautaRepository;
import br.com.apivotacao.repositories.VotacaoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class VotacaoService {

    private final String TOPICO_VOTACAO = "votacao";

    private final VotacaoRepository repository;

    private final PautaRepository pautaRepository;

    private final EventoFilaService eventoFilaService;

    /**
     * @author Daniel Cavalcante
     *
     * Método votar — Pensando em que milhões de pessoas podem votar ao mesmo tempo,
     * para ser performático iremos trabalhar com mensageria, adicionando esse evento
     * na fila.
     *
     * @param votoDTO VotoDTO — Voto.
     */
    public void votar(VotoDTO votoDTO) {
        this.eventoFilaService.add(this.TOPICO_VOTACAO, votoDTO);
    }

    /**
     * @author Daniel Cavalcante
     *
     * Método contabilizarVotosPorPauta — Contabilizará o total de votos SIM e NAO por pauta.
     *
     * @param idPauta String — idPauta.
     *
     * @return TotalVotosDTO — Retornará o total dos votos da pauta.
     *
     * @throws ResourceNotFoundException
     */
    public TotalVotosDTO contabilizarVotosPorPauta(String idPauta) {
        this.pautaRepository.findById(idPauta)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível contabilizar os votos, pois a pauta não existe."));

        List<Votacao> votacaoList = this.repository.findByPauta_Id(idPauta);

        if(votacaoList.isEmpty()) {
           throw new ResourceNotFoundException("Nenhuma votação foi realizada para essa pauta.");
        }

        Long totalVotoSim = votacaoList.stream().filter(votacao -> votacao.getTipoVoto().equals(TipoVoto.SIM)).count();
        Long totalVotoNAO = votacaoList.stream().filter(votacao -> votacao.getTipoVoto().equals(TipoVoto.NAO)).count();

        TotalVotosDTO totalVotos = TotalVotosDTO.builder()
                .pauta(votacaoList.get(0).getPauta())
                .totalVotosSim(totalVotoSim)
                .totalVotosNao(totalVotoNAO).build();

        return totalVotos;
    }

    public void deletarPorId(String id) {
        this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não será possível excluir a votação, pois não existe!"));

        this.repository.deleteById(id);
    }

    public Optional<Votacao> buscarPorid(String id) {
        return this.repository.findById(id);
    }

    public List<Votacao> buscarTodos() {
        return this.repository.findAll();
    }
}