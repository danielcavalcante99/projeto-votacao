package br.com.apivotacao.services;

import br.com.apivotacao.exceptions.ResourceNotFoundException;
import br.com.apivotacao.models.Pauta;
import br.com.apivotacao.repositories.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PautaServiceTest {


    private PautaService service;

    @Mock
    private PautaRepository repository;

    @Mock
    private EventoFilaService eventoFilaService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        this.service = new PautaService(this.eventoFilaService, this.repository);
    }

    @Test
    @DisplayName("Teste - Encerrar sessão de uma pauta existente")
    void encerrarSessaoVotosPauta() {
        Pauta pauta = new Pauta("63ea02a9db49444523669f73", "Assunto A", "Descrição A", true, LocalDateTime.now(), null);
        when(this.repository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        Pauta entity = this.service.encerrarSessaoVotosPauta(pauta.getId());

        assertAll(() -> verify(this.repository).save(pauta),
                () -> assertFalse(entity.getSessaoAtiva()));
    }

    @Test
    @DisplayName("Teste - Encerrar sessão de uma pauta inesistente")
    void encerrarSessaoVotosPautaInesistente() {
        Pauta pauta = new Pauta("79ea02a9db49444523669f73", "Assunto B", "Descrição B", true, LocalDateTime.now(), null);
        when(this.repository.findById(pauta.getId())).thenReturn(Optional.empty());
        assertAll(() -> assertThrows(ResourceNotFoundException.class, ()-> this.service.encerrarSessaoVotosPauta(pauta.getId())));
    }
}