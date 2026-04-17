package com.integralltech.chamados.dto;

import com.integralltech.chamados.model.Chamado;
import com.integralltech.chamados.model.enums.Prioridade;
import com.integralltech.chamados.model.enums.Setor;
import com.integralltech.chamados.model.enums.Status;

import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public Setor getSetor() { return setor; }
    public Prioridade getPrioridade() { return prioridade; }
    public Status getStatus() { return status; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public String getSolicitante() { return solicitante; }
}
