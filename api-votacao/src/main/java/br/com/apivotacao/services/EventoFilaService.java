package br.com.apivotacao.services;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventoFilaService {

    private final KafkaTemplate<Object, Object> template;

    public <T> void add(String topico, T dados) {
        this.template.send(topico, dados);
    }
}
