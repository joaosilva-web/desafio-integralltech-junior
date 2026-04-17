package com.integralltech.chamados.dto;

import com.integralltech.chamados.model.enums.Prioridade;
import com.integralltech.chamados.model.enums.Setor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnaliseResponseDTO {

    private Long chamadoId;
    private Prioridade prioridadeSugerida;
    private Setor setorSugerido;
    private String resumo;
    private LocalDateTime analisadoEm;
}
