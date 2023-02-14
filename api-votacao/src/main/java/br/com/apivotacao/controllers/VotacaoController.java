package br.com.apivotacao.controllers;

import br.com.apivotacao.controllers.exceptions.ApiException;
import br.com.apivotacao.dtos.TotalVotosDTO;
import br.com.apivotacao.dtos.VotoDTO;
import br.com.apivotacao.models.Votacao;
import br.com.apivotacao.models.enums.TipoVoto;
import br.com.apivotacao.services.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Votações")
@AllArgsConstructor
@RequestMapping("/api/votacoes")
public class VotacaoController {

    private final VotacaoService service;

    @GetMapping("/v1/contabilizar")
    @Operation(summary = "Consultar votação por id")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Votação não encotrada",
    content = @Content(schema = @Schema(implementation = ApiException.class)))
    public ResponseEntity<TotalVotosDTO> contabilizarVotosPorPauta(@RequestParam String idPauta) {
        TotalVotosDTO totalVotos = this.service.contabilizarVotosPorPauta(idPauta);
        return ResponseEntity.ok(totalVotos);
    }

    @GetMapping("/v1/consultar/{id}")
    @Operation(summary = "Consultar votação por id")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Votação não encotrada",
    content = @Content(schema = @Schema(defaultValue = "")))
    public ResponseEntity<Votacao> consulta(@PathVariable String id) {
        Optional<Votacao> optVotacao = this.service.buscarPorid(id);
        return optVotacao.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/v1/buscarTodos")
    @Operation(summary = "Consultar todas as votações")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Votação não encotrada",
    content = @Content(schema = @Schema(defaultValue = "")))
    public ResponseEntity<List<Votacao>> contabilizarVotosPorPauta() {
        List<Votacao> votacaoList = this.service.buscarTodos();

        if (votacaoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(votacaoList);
    }

    @PostMapping("/v1/votar")
    @Operation(summary = "Votar")
    @ApiResponse(responseCode = "200", description = "Voto enviado",
    content = @Content(schema = @Schema(defaultValue = "Solicitação do voto enviado")))
    @ApiResponse(responseCode = "400", description = "Requisição inválida",
    content = @Content(schema = @Schema(implementation = ApiException.class)))
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


    @DeleteMapping("/v1/excluir/{id}")
    @Operation(summary = "Exclusão da votação por id")
    @ApiResponse(responseCode = "204", description = "Votação excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Votação não encontrada",
    content = @Content(schema = @Schema(implementation = ApiException.class)))
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        this.service.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
