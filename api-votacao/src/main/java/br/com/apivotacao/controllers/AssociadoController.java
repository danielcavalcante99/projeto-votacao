package br.com.apivotacao.controllers;

import br.com.apivotacao.dtos.AssociadoDTO;
import br.com.apivotacao.models.Associado;
import br.com.apivotacao.services.AssociadoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/associados")
public class AssociadoController {

    private final AssociadoService service;

    @GetMapping("/v1/consultar/{id}")
    public ResponseEntity<Associado> consulta(@PathVariable String id) {
        Optional<Associado> optAssociado = this.service.buscarPorId(id);
        return optAssociado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());

    }

    @GetMapping("/v1/todos")
    public ResponseEntity<List<Associado>> consultaTodos() {
        List<Associado> associadoList = this.service.buscarTodos();

        if (associadoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(associadoList);
    }

    @PostMapping("/v1/salvar")
    public ResponseEntity<Associado> salvar(@RequestBody @Valid AssociadoDTO associadoDTO) {
        Associado associado = new Associado();
        BeanUtils.copyProperties(associadoDTO, associado);
        this.service.salvar(associado);
        return ResponseEntity.status(HttpStatus.CREATED).body(associado);
    }

    @DeleteMapping("/v1/excluir/id")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        this.service.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}