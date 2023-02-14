package br.com.microservicovotacao.services;

import br.com.microservicovotacao.exceptions.BusinessException;
import br.com.microservicovotacao.exceptions.ResourceNotFoundException;
import br.com.microservicovotacao.jobs.EncerrarSessaoJob;
import br.com.microservicovotacao.models.Pauta;
import br.com.microservicovotacao.repositories.PautaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class ConsumerEncerrarSessaoPautaService {

    private final Long MINUTE = 5L;

    private final String TOPICO_ENCERRAR_SESSAO = "encerrarSessaoVotos";

    private final String GROUP_ENCERRRAR_SESSAO = "grupoEncerrarSessao";

    private final PautaRepository repository;


    /**
     * @author Daniel Cavalcante
     *
     * Método executar — Responsável por ficar escutando se chegou alguma solicitação
     * na fila para realizar encerramento da sessão dos votos de determinada pauta.
     *
     */
    @KafkaListener(topics = TOPICO_ENCERRAR_SESSAO, groupId = "ConsumerEncerrarSessaoPautaService")
    private void executar(ConsumerRecord<String, String> registro) {
        String idPauta = registro.value().replaceAll("\"", "");
        this.encerrarSessao(idPauta);
    }

    /**
     * @author Daniel Cavalcante
     *
     * Método encerrarSessao — Responsável por realizar o processamento e acionar o job para encerrar a sessão.
     *
     * @throws ResourceNotFoundException
     */
    public void encerrarSessao(String idPauta) {
        try {
            Pauta pauta = repository.findById(idPauta)
                    .orElseThrow(() -> new ResourceNotFoundException("Pauta: [id={}] não cadastrada, por isso não será possível finalizar a sessão de votação dessa pauta."));

            LocalDateTime dataCadastro = pauta.getDataCadastro();
            Date dataInicioExecucao = Date.from(dataCadastro.plusMinutes(MINUTE).atZone(ZoneId.systemDefault()).toInstant());

            SchedulerFactory shedFact = new StdSchedulerFactory();
            Scheduler scheduler = shedFact.getScheduler();
            scheduler.start();

            JobDetail job = JobBuilder.newJob(EncerrarSessaoJob.class).withIdentity(idPauta, GROUP_ENCERRRAR_SESSAO).build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(idPauta, GROUP_ENCERRRAR_SESSAO)
                    .startAt(dataInicioExecucao)
                    .usingJobData("idPauta", idPauta).build();

            scheduler.scheduleJob(job, trigger);
            log.info("Sessão de votação da pauta: [id={}] irá encerrar em {}.", idPauta, dataInicioExecucao);

        } catch (SchedulerException e) {
            log.error("Ocorreu um problema ao agendar a finalização da sessão de votação da pauta: [id={}], motivo: {}.", idPauta, e.getMessage());
        }
    }


}
