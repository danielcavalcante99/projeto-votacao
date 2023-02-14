package br.com.microservicovotacao.jobs;

import br.com.microservicovotacao.models.Pauta;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
public class EncerrarSessaoJob implements Job {

    private final String DOMAIN_VOTACAO_API = "DOMAIN_VOTACAO_API";

    public WebClient webClient() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        String domain = applicationContext.getEnvironment().getProperty(DOMAIN_VOTACAO_API);
        return WebClient.builder()
                .baseUrl(domain)
                .build();
    }

    @Override
    public void execute(JobExecutionContext context) {
         String idPauta = context.getTrigger().getJobDataMap().getString("idPauta");

        try {
            UriComponents uri = UriComponentsBuilder.newInstance()
                    .path("/api/pautas/v1/encerrarsessao/{id}")
                    .buildAndExpand(idPauta);

            WebClient.RequestBodySpec requestBodySpec = this.webClient().method(HttpMethod.PUT).uri(uri.toUriString());
            Mono<Pauta> monoPauta = requestBodySpec.retrieve().bodyToMono(Pauta.class);

            monoPauta.subscribe(pauta -> {
                log.info("Sessão de votação encerrada para pauta: [{}]", pauta);
            });

        } catch (Exception e) {
            log.error("Ocorreu um problema ao tentar finalizar a sessão de votação da pauta: [id={}], motivo: {}.", idPauta, e.getMessage());
        }

    }


}