package com.integralltech.chamados.dto;

import com.integralltech.chamados.model.enums.Prioridade;
import com.integralltech.chamados.model.enums.Setor;

import java.time.LocalDateTime;

public class AnaliseResponseDTO {

    private Long chamadoId;
    private Prioridade prioridadeSugerida;
    private Setor setorSugerido;
    private String resumo;
    private LocalDateTime analisadoEm;

    public AnaliseResponseDTO(Long chamadoId, Prioridade prioridadeSugerida, Setor setorSugerido, String resumo, LocalDateTime analisadoEm) {
        this.chamadoId = chamadoId;
        this.prioridadeSugerida = prioridadeSugerida;
        this.setorSugerido = setorSugerido;
        this.resumo = resumo;
        this.analisadoEm = analisadoEm;
    }

    public Long getChamadoId() { return chamadoId; }
    public Prioridade getPrioridadeSugerida() { return prioridadeSugerida; }
    public Setor getSetorSugerido() { return setorSugerido; }
    public String getResumo() { return resumo; }
    public LocalDateTime getAnalisadoEm() { return analisadoEm; }
}
