package br.com.apivotacao.controllers;

import br.com.apivotacao.controllers.exceptions.ApiException;
import br.com.apivotacao.dtos.PautaDTO;
import br.com.apivotacao.models.Pauta;
import br.com.apivotacao.services.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Pautas")
@AllArgsConstructor
@RequestMapping("/api/pautas")
public class PautaController {

    private final PautaService service;

    @GetMapping("/v1/consultar/{id}")
    @Operation(summary = "Consultar pauta por id")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Pauta não encotrada",
    content = @Content(schema = @Schema(defaultValue = "")))
    public ResponseEntity<Pauta> consulta(@PathVariable String id) {
        Optional<Pauta> optPauta = this.service.buscarPorId(id);
        return optPauta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/v1/todos")
    @Operation(summary = "Consultar todas as pautas")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Pauta não encotrada",
    content = @Content(schema = @Schema(defaultValue = "")))
    public ResponseEntity<List<Pauta>> consultaTodos() {
        List<Pauta> pautaList = this.service.buscarTodos();

        if (pautaList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(pautaList);
    }

    @PostMapping("/v1/salvar")
    @Operation(summary = "Cadastrar Pauta")
    @ApiResponse(responseCode = "201", description = "Pauta cadastrada com sucesso",
    content = @Content(schema = @Schema(implementation = PautaDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida",
    content = @Content(schema = @Schema(implementation = ApiException.class)))
    public ResponseEntity<Pauta> salvar(@RequestBody @Valid PautaDTO pautaDTO) {

        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(pautaDTO, pauta);
        pauta.setDataCadastro(LocalDateTime.now());
        pauta.setSessaoAtiva(true);

        this.service.salvar(pauta);
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @PutMapping("/v1/encerrarsessao/{id}")
    @Operation(summary = "Encerrar sessão de votação da pauta")
    @ApiResponse(responseCode = "201", description = "Sessão encerrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
    content = @Content(schema = @Schema(implementation = ApiException.class)))
    public ResponseEntity<Pauta> encerrarSessao(@PathVariable String id) {
        Pauta pauta = this.service.encerrarSessaoVotosPauta(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @DeleteMapping("/v1/excluir/{id}")
    @Operation(summary = "Exclusão da pauta por id")
    @ApiResponse(responseCode = "204", description = "Pauta excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
    content = @Content(schema = @Schema(implementation = ApiException.class)))
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        this.service.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}