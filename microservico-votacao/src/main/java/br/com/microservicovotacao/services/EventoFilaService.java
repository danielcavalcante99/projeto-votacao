package br.com.microservicovotacao.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
@NoArgsConstructor
@AllArgsConstructor
public class EventoFilaService {

    private KafkaTemplate<Object, Object> template;

    public <T> void add(String topico, T dados) {
        this.template.send(topico, dados);
    }
}
