package br.com.apivotacao.services;

import br.com.apivotacao.dtos.StatusDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@AllArgsConstructor
public class ValidarCPFService {

    private final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

    private final String DOMAIN_VALIDADOR_CEP_API = "DOMAIN_VALIDATOR_CEP_API";
    private final Environment environment;

    public WebClient webClient() {
        String domain = this.environment.getProperty(this.DOMAIN_VALIDADOR_CEP_API);
        return WebClient.builder().baseUrl(domain).build();
    }

    /**
     * @author Daniel Cavalcante
     *
     * Método isValid — Iremos consumir uma API externa usando o WebClient para validar o CPF.
     *
     * @param cpf String — cpf.
     *
     * @return boolean — Retornará se o CPF é válido.
     */
    public boolean isValid(String cpf) {
        try {
            UriComponents uri = UriComponentsBuilder.newInstance().path("/users/{cpf}").buildAndExpand(cpf);

            StatusDTO status = this.webClient().method(HttpMethod.GET).uri(uri.toUriString()).retrieve().bodyToMono(StatusDTO.class).block();

            return status.getStatus().equals(ABLE_TO_VOTE);

        } catch (Exception e) {
            log.error("Ocorreu um problema ao consultar o cpf: {}, motivo: {}.", cpf, e.getMessage());
            return false;
        }

    }
}