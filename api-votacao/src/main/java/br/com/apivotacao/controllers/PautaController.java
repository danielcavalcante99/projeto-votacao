package br.com.apivotacao.controllers;

import br.com.apivotacao.dtos.PautaDTO;
import br.com.apivotacao.models.Pauta;
import br.com.apivotacao.services.PautaService;
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
@AllArgsConstructor
@RequestMapping("/api/pautas")
public class PautaController {

    private final PautaService service;

    @GetMapping("/v1/consultar/{id}")
    public ResponseEntity<Pauta> consulta(@PathVariable String id) {
        Optional<Pauta> optPauta = this.service.buscarPorId(id);
        return optPauta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/v1/todos")
    public ResponseEntity<List<Pauta>> consultaTodos() {
        List<Pauta> pautaList = this.service.buscarTodos();

        if (pautaList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(pautaList);
    }

    @PostMapping("/v1/salvar")
    public ResponseEntity<Pauta> salvar(@RequestBody @Valid PautaDTO pautaDTO) {
        pautaDTO.setDataCadastro(LocalDateTime.now());
        pautaDTO.setSessaoAtiva(true);

        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(pautaDTO, pauta);

        this.service.salvar(pauta);
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @PutMapping("/v1/encerrarsessao/{id}")
    public ResponseEntity<Pauta> encerrarSessao(@PathVariable String id) {
        Pauta pauta = this.service.encerrarSessaoVotosPauta(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @DeleteMapping("/v1/excluir/id")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        this.service.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}