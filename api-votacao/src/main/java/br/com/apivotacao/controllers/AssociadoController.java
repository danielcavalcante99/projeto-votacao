package br.com.apivotacao.controllers;

import br.com.apivotacao.controllers.exceptions.ApiException;
import br.com.apivotacao.dtos.AssociadoDTO;
import br.com.apivotacao.models.Associado;
import br.com.apivotacao.services.AssociadoService;
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
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Associados")
@AllArgsConstructor
@RequestMapping("/api/associados")
public class AssociadoController {

    private final AssociadoService service;

    @GetMapping("/v1/consultar/{id}")
    @Operation(summary = "Consultar associado por id")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Associado não encotrada",
    content = @Content(schema = @Schema(defaultValue = "")))
    public ResponseEntity<Associado> consulta(@PathVariable String id) {
        Optional<Associado> optAssociado = this.service.buscarPorId(id);
        return optAssociado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());

    }

    @GetMapping("/v1/todos")
    @Operation(summary = "Consultar todos os associados")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Associado não encotrado",
    content = @Content(schema = @Schema(defaultValue = "")))
    public ResponseEntity<List<Associado>> consultaTodos() {
        List<Associado> associadoList = this.service.buscarTodos();

        if (associadoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(associadoList);
    }

    @PostMapping("/v1/salvar")
    @Operation(summary = "Cadastrar associado")
    @ApiResponse(responseCode = "201", description = "Cadastro realizado com sucesso",
    content = @Content(schema = @Schema(implementation = AssociadoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida",
    content = @Content(schema = @Schema(implementation = ApiException.class)))
    public ResponseEntity<Associado> salvar(@RequestBody @Valid AssociadoDTO associadoDTO) {
        Associado associado = new Associado();
        BeanUtils.copyProperties(associadoDTO, associado);
        this.service.salvar(associado);
        return ResponseEntity.status(HttpStatus.CREATED).body(associado);
    }

    @DeleteMapping("/v1/excluir/{id}")
    @Operation(summary = "Exclusão do associado por id")
    @ApiResponse(responseCode = "204", description = "Associado excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Associado não encontrado",
    content = @Content(schema = @Schema(implementation = ApiException.class)))
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        this.service.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}