package br.com.microservicovotacao.services;

import br.com.microservicovotacao.dtos.VotoDTO;
import br.com.microservicovotacao.exceptions.BusinessException;
import br.com.microservicovotacao.exceptions.ResourceNotFoundException;
import br.com.microservicovotacao.models.Associado;
import br.com.microservicovotacao.models.Pauta;
import br.com.microservicovotacao.models.Votacao;
import br.com.microservicovotacao.repositories.AssociadoRepository;
import br.com.microservicovotacao.repositories.PautaRepository;
import br.com.microservicovotacao.repositories.VotacaoRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ConsumerVotacaoProcessarService {

    private final VotacaoRepository votacaoRepository;

    private final AssociadoRepository associadoRepository;

    private final PautaRepository pautaRepository;

    private final EventoFilaService eventoFilaService;

    private final String TOPICO_VOTACAO = "votacao";
    private final String TOPICO_VOTO_PROCESSADO = "votoProcessado";

    private final String TOPICO_VOTO_NAO_PROCESSADO = "votoNaoProcessado";


    /**
     * @author Daniel Cavalcante
     *
     * Método executar — Responsável por ficar escutando se chegou algum envio do voto
     * na fila para realizar o processamento e depois devolver a resposta para o solicitante.
     *
     */
    @KafkaListener(topics = TOPICO_VOTACAO, groupId = "ConsumerVotacaoProcessarService")
    private void executar(ConsumerRecord<String, String> registro) {
        Gson gson = new Gson();
        String strVotoDTO = registro.value();

        try {
            VotoDTO votoDTO = gson.fromJson(strVotoDTO, VotoDTO.class);
            this.votar(votoDTO);

        } catch (Exception e) {
            String erro = String.format("votoDTO: %s, message: %s", strVotoDTO, e.getMessage());
            this.eventoFilaService.add(TOPICO_VOTO_NAO_PROCESSADO, erro);
            log.error(e.getMessage());
        }
    }

    /**
     * @author Daniel Cavalcante
     *
     * Método votar — Responsável por realizar o processamento do voto.
     *
     * @throws ResourceNotFoundException
     * @throws BusinessException
     */
    public void votar(VotoDTO votoDTO) {

        Pauta pauta = this.pautaRepository.findById(votoDTO.getIdPauta())
                .orElseThrow(() -> new ResourceNotFoundException("Não é possível votar, pois a pauta não existe."));

        Associado associado = this.associadoRepository.findById(votoDTO.getIdAssociado())
                .orElseThrow(() -> new ResourceNotFoundException("Não é possível votar, pois associado não existe."));

        if (!pauta.getSessaoAtiva()) {
            throw new BusinessException("Sessão da votação dessa pauta já foi encerrada.");
        }

        List<Votacao> votacaoList = this.votacaoRepository.findByAssociadoAndPauta(associado, pauta);

        if (votacaoList.isEmpty()) {
            Votacao votacao = Votacao.builder()
                    .dataVoto(LocalDateTime.now())
                    .tipoVoto(votoDTO.getTipoVoto())
                    .associado(associado)
                    .pauta(pauta).build();

            this.votacaoRepository.save(votacao);
            this.eventoFilaService.add(TOPICO_VOTO_PROCESSADO, votoDTO.toString());

            log.info("Voto recebido = {}", votacao);
            log.info("Voto recebido com sucesso [id={}, dataHora={}]", votacao.getId(), votacao.getDataVoto());

        } else {
            throw new BusinessException("Associado não pode votar mais de uma vez na mesma pauta.");
        }
    }

}