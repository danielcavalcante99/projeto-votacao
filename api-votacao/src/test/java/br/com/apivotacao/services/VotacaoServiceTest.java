package br.com.apivotacao.services;

import br.com.apivotacao.dtos.TotalVotosDTO;
import br.com.apivotacao.exceptions.ResourceNotFoundException;
import br.com.apivotacao.models.Associado;
import br.com.apivotacao.models.Pauta;
import br.com.apivotacao.models.Votacao;
import br.com.apivotacao.models.enums.TipoVoto;
import br.com.apivotacao.repositories.PautaRepository;
import br.com.apivotacao.repositories.VotacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


class VotacaoServiceTest {

    private VotacaoService service;

    @Mock
    private VotacaoRepository repository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private EventoFilaService eventoFilaService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        this.service = new VotacaoService(this.repository, this.pautaRepository, this.eventoFilaService);
    }

    @Test
    @DisplayName("Teste - Contabilizar votos por pauta")
    void contabilizarVotosPorPauta() {

        Pauta pauta = new Pauta(null, "Assunto A", "Descrição A", true, LocalDateTime.now(), null);

        List<Associado> associadoList = List.of(
                new Associado(null, "Daniel", "25487965454"),
                new Associado(null, "Gabriel", "35487965457"),
                new Associado(null, "João", "22549965488"),
                new Associado(null, "Paulo", "25487565454"),
                new Associado(null, "Mauricio", "29487965454")
        );

        List<Votacao> votacaoList = List.of(
                new Votacao(null, associadoList.get(0), pauta, TipoVoto.NAO, LocalDateTime.now()),
                new Votacao(null, associadoList.get(1), pauta, TipoVoto.SIM, LocalDateTime.now()),
                new Votacao(null, associadoList.get(2), pauta, TipoVoto.NAO, LocalDateTime.now()),
                new Votacao(null, associadoList.get(3), pauta, TipoVoto.SIM, LocalDateTime.now()),
                new Votacao(null, associadoList.get(4), pauta, TipoVoto.NAO, LocalDateTime.now())
        );

        Long totalVotoSim = votacaoList.stream().filter(votacao -> votacao.getTipoVoto().equals(TipoVoto.SIM)).count();
        Long totalVotoNAO = votacaoList.stream().filter(votacao -> votacao.getTipoVoto().equals(TipoVoto.NAO)).count();

        when(this.pautaRepository.findById(any())).thenReturn(Optional.of(pauta));

        when(this.repository.findByPauta_Id(any())).thenReturn(votacaoList);

        TotalVotosDTO totalVotosDTO = this.service.contabilizarVotosPorPauta(pauta.getId());

        assertAll(() -> assertEquals(totalVotosDTO.getTotalVotosNao(), totalVotoNAO),
                  () -> assertEquals(totalVotosDTO.getTotalVotosSim(), totalVotoSim));
    }


    @Test
    @DisplayName("Teste - Tentar contabilizar votos da pauta que não existe")
    void contabilizarVotosPorPautaInexistente() {
        Pauta pauta = null;
        when(this.pautaRepository.findById(any())).thenReturn(Optional.ofNullable(pauta));
        assertThrows(ResourceNotFoundException.class, () -> this.service.contabilizarVotosPorPauta(any()));

    }
}