package br.com.apivotacao.controllers;

import br.com.apivotacao.dtos.TotalVotosDTO;
import br.com.apivotacao.dtos.VotoDTO;
import br.com.apivotacao.models.Votacao;
import br.com.apivotacao.models.enums.TipoVoto;
import br.com.apivotacao.services.VotacaoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/votacoes")
public class VotacaoController {

    private final VotacaoService service;

    @GetMapping("/v1/contabilizar")
    public ResponseEntity<TotalVotosDTO> contabilizarVotosPorPauta(@RequestParam String idPauta) {
        TotalVotosDTO totalVotos = this.service.contabilizarVotosPorPauta(idPauta);
        return ResponseEntity.ok(totalVotos);
    }

    @GetMapping("/v1/buscarTodos")
    public ResponseEntity<List<Votacao>> contabilizarVotosPorPauta() {
        List<Votacao> votacaoList = this.service.buscarTodos();

        if (votacaoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(votacaoList);
    }

    @PostMapping("/v1/votar")
    public ResponseEntity<String> votar(@RequestParam String idAssociado,
                                        @RequestParam String idPauta,
                                        @RequestParam TipoVoto tipoVoto) {

        VotoDTO votacaoDTO = VotoDTO.builder()
                .idAssociado(idAssociado)
                .idPauta(idPauta)
                .tipoVoto(tipoVoto).build();

        this.service.votar(votacaoDTO);
        return ResponseEntity.ok("Solicitação do voto enviado");
    }
}
