package com.integralltech.chamados.controller;

import com.integralltech.chamados.dto.AnaliseResponseDTO;
import com.integralltech.chamados.dto.ChamadoRequestDTO;
import com.integralltech.chamados.dto.ChamadoResponseDTO;
import com.integralltech.chamados.model.enums.Setor;
import com.integralltech.chamados.service.ChamadoService;
import com.integralltech.chamados.service.IaAnaliseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;
    private final IaAnaliseService iaAnaliseService;

    public ChamadoController(ChamadoService chamadoService, IaAnaliseService iaAnaliseService) {
        this.chamadoService = chamadoService;
        this.iaAnaliseService = iaAnaliseService;
    }

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> criar(@Valid @RequestBody ChamadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chamadoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponseDTO>> listar() {
        return ResponseEntity.ok(chamadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ChamadoRequestDTO dto) {
        return ResponseEntity.ok(chamadoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(chamadoService.cancelar(id));
    }

    @GetMapping("/setor/{setor}")
    public ResponseEntity<List<ChamadoResponseDTO>> listarPorSetor(@PathVariable Setor setor) {
        return ResponseEntity.ok(chamadoService.listarPorSetor(setor));
    }

    @PostMapping("/{id}/analisar")
    public ResponseEntity<AnaliseResponseDTO> analisar(@PathVariable Long id) {
        return ResponseEntity.ok(iaAnaliseService.analisar(chamadoService.encontrar(id)));
    }
}
