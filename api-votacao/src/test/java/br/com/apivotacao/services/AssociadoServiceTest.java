package br.com.apivotacao.services;

import br.com.apivotacao.exceptions.BusinessException;
import br.com.apivotacao.models.Associado;
import br.com.apivotacao.repositories.AssociadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AssociadoServiceTest {


    private AssociadoService service;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private ValidarCPFService validarCPFService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        this.service = new AssociadoService(this.associadoRepository, this.validarCPFService);
    }

    @Test
    @DisplayName("Teste - Simulando Cadastro com associado com CPF valido")
    void salvarCadastroValido() {
        Associado associado = new Associado("63ea02a9db49444523669f73", "Daniel", "69462944083");
        when(this.validarCPFService.isValid(any())).thenReturn(true);
        when(this.associadoRepository.save(any())).thenReturn(associado);
        assertDoesNotThrow(() -> this.service.salvar(associado));
    }

    @Test
    @DisplayName("Teste - Simulando Cadastro com associado com CPF inválido")
    void salvarCadastroInvalido() {
        Associado associado = new Associado("63ea02a9db49444523669f73", "Daniel", "69462944077");
        when(this.validarCPFService.isValid(any())).thenReturn(false);
        assertThrows(BusinessException.class, () -> this.service.salvar(associado));
    }
}