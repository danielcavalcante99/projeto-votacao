package br.com.microservicovotacao.services;

import br.com.microservicovotacao.dtos.VotoDTO;
import br.com.microservicovotacao.exceptions.BusinessException;
import br.com.microservicovotacao.exceptions.ResourceNotFoundException;
import br.com.microservicovotacao.models.Associado;
import br.com.microservicovotacao.models.Pauta;
import br.com.microservicovotacao.models.Votacao;
import br.com.microservicovotacao.models.enums.TipoVoto;
import br.com.microservicovotacao.repositories.AssociadoRepository;
import br.com.microservicovotacao.repositories.PautaRepository;
import br.com.microservicovotacao.repositories.VotacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;


import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


class ConsumerVotacaoProcessarServiceTest {

    private ConsumerVotacaoProcessarService service;

    @Mock
    private VotacaoRepository votacaoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private EventoFilaService eventoFilaService;


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        this.service = new ConsumerVotacaoProcessarService(
                this.votacaoRepository, this.associadoRepository,
                this.pautaRepository, this.eventoFilaService
        );
    }

    @Test
    @DisplayName("Teste - Votar com pauta inesistente")
    void votarComPautaInesistente() {

        VotoDTO votoDTO = VotoDTO.builder()
               .idPauta("63eb41b94c658041fcc13a62")
               .idAssociado("83eb41b94c658041fcc13a77")
               .tipoVoto(TipoVoto.SIM).build();

        when(this.pautaRepository.findById(votoDTO.getIdPauta())).thenReturn(Optional.empty());

        try {
            this.service.votar(votoDTO);
        } catch (ResourceNotFoundException e) {
            String expectMessage = "Não é possível votar, pois a pauta não existe.";
            Assertions.assertEquals(expectMessage, e.getMessage());
        }
    }


    @Test
    @DisplayName("Teste - Votar com associado inesistente")
    void votarComAssociadoInesistente() {

        VotoDTO votoDTO = VotoDTO.builder()
                .idPauta("95eb41b94c658041fcc13a62")
                .idAssociado("49eb41b94c658041fcc13a77")
                .tipoVoto(TipoVoto.SIM).build();

        Pauta pauta = Pauta.builder()
                .id(votoDTO.getIdPauta())
                .assunto("Assunto A")
                .descricao("descricao B")
                .dataCadastro(LocalDateTime.now()).build();

        when(this.pautaRepository.findById(votoDTO.getIdPauta())).thenReturn(Optional.of(pauta));
        when(this.associadoRepository.findById(votoDTO.getIdPauta())).thenReturn(Optional.empty());

        try {
            this.service.votar(votoDTO);
        } catch (ResourceNotFoundException e) {
            String expectMessage = "Não é possível votar, pois associado não existe.";
            Assertions.assertEquals(expectMessage, e.getMessage());
        }
    }


    @Test
    @DisplayName("Teste - Votar sessão já encerrada")
    void votarComSessaoEncerrada() {

        VotoDTO votoDTO = VotoDTO.builder()
                .idPauta("99jk41b94c658041fcc13a62")
                .idAssociado("30eb45194c658041fcc13a77")
                .tipoVoto(TipoVoto.NAO).build();

        Pauta pauta = Pauta.builder()
                .id(votoDTO.getIdPauta())
                .assunto("Assunto A")
                .descricao("descricao B")
                .sessaoAtiva(false)
                .dataCadastro(LocalDateTime.now()).build();

       Associado associado = Associado.builder()
               .id(votoDTO.getIdAssociado())
               .nome("Cleber")
               .cpf("66729912049").build();

        when(this.pautaRepository.findById(votoDTO.getIdPauta())).thenReturn(Optional.of(pauta));
        when(this.associadoRepository.findById(votoDTO.getIdAssociado())).thenReturn(Optional.of(associado));

        try {
            this.service.votar(votoDTO);
        } catch (BusinessException e) {
            String expectMessage = "Sessão da votação dessa pauta já foi encerrada.";
            Assertions.assertEquals(expectMessage, e.getMessage());
        }
    }


    @Test
    @DisplayName("Teste - Votar mais de uma vez")
    void votarMaisDeUmaVez() {

        VotoDTO votoDTO = VotoDTO.builder()
                .idPauta("77jk41b94c658041fcc13a62")
                .idAssociado("88eb45194c658041fcc13a77")
                .tipoVoto(TipoVoto.NAO).build();

        Pauta pauta = Pauta.builder()
                .id(votoDTO.getIdPauta())
                .assunto("Assunto A")
                .descricao("descricao B")
                .sessaoAtiva(true)
                .dataCadastro(LocalDateTime.now()).build();

        Associado associado = Associado.builder()
                .id(votoDTO.getIdAssociado())
                .nome("Carlos")
                .cpf("99387249069").build();

        List<Votacao> votacaoList = List.of(
                new Votacao("44jk41b94c658041fcc13a99", associado, pauta, TipoVoto.SIM, LocalDateTime.now())
        );

        when(this.pautaRepository.findById(votoDTO.getIdPauta())).thenReturn(Optional.of(pauta));
        when(this.associadoRepository.findById(votoDTO.getIdAssociado())).thenReturn(Optional.of(associado));
        when(this.votacaoRepository.findByAssociadoAndPauta(associado, pauta)).thenReturn(votacaoList);

        try {
            this.service.votar(votoDTO);
        } catch (BusinessException e) {
            String expectMessage = "Associado não pode votar mais de uma vez na mesma pauta.";
            Assertions.assertEquals(expectMessage, e.getMessage());
        }
    }
}