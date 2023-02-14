package br.com.apivotacao.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerVotacaoService {
    private final String TOPICO_VOTO_PROCESSADO = "votoProcessado";

    private final String TOPICO_VOTO_NAO_PROCESSADO = "votoNaoProcessado";

    /**
     * @author Daniel Cavalcante
     *
     * Método executar — Responsável por ficar escutando se chegou alguma resposta
     * seja positiva ou negativa do envio dos votos dos associados que são processados
     * por outro microserviço.
     *
     */
    @KafkaListener(topics = {TOPICO_VOTO_PROCESSADO, TOPICO_VOTO_NAO_PROCESSADO}, groupId = "ConsumerVotacaoResposta")
    private void executar(ConsumerRecord<String, String> registro) {

        if (registro.topic().equals(TOPICO_VOTO_PROCESSADO)) {
            log.info("Voto processado com sucesso {}", registro.value());
        } else {
            log.error("Voto não foi processado {}", registro.value());
        }

    }
}
