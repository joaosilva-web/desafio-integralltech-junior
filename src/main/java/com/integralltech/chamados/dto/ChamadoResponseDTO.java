package com.integralltech.chamados.dto;

import com.integralltech.chamados.model.Chamado;
import com.integralltech.chamados.model.enums.Prioridade;
import com.integralltech.chamados.model.enums.Setor;
import com.integralltech.chamados.model.enums.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChamadoResponseDTO {

    private final Long id;
    private final String titulo;
    private final String descricao;
    private final Setor setor;
    private final Prioridade prioridade;
    private final Status status;
    private final LocalDateTime dataAbertura;
    private final LocalDateTime dataFechamento;
    private final String solicitante;

    public ChamadoResponseDTO(Chamado chamado) {
        this.id = chamado.getId();
        this.titulo = chamado.getTitulo();
        this.descricao = chamado.getDescricao();
        this.setor = chamado.getSetor();
        this.prioridade = chamado.getPrioridade();
        this.status = chamado.getStatus();
        this.dataAbertura = chamado.getDataAbertura();
        this.dataFechamento = chamado.getDataFechamento();
        this.solicitante = chamado.getSolicitante();
    }
}
