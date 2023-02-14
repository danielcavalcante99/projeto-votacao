package br.com.microservicovotacao.services;

import br.com.microservicovotacao.exceptions.ResourceNotFoundException;
import br.com.microservicovotacao.repositories.PautaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;


import java.util.Optional;


class ConsumerEncerrarSessaoPautaServiceTest {

    private ConsumerEncerrarSessaoPautaService service;

    @Mock
    private PautaRepository repository;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        this.service = new ConsumerEncerrarSessaoPautaService(this.repository);
    }

    @Test
    @DisplayName("Teste - Encerrar sessão de pauta não cadastrada")
    void encerrarSessaoPautaInesistente() {
        when(this.repository.findById(any())).thenReturn(Optional.empty());

        try {
            this.service.encerrarSessao(any());
        } catch (ResourceNotFoundException e) {
            String expectMessage = "Pauta: [id={}] não cadastrada, por isso não será possível finalizar a sessão de votação dessa pauta.";
            Assertions.assertEquals(expectMessage, e.getMessage());
        }
    }

}