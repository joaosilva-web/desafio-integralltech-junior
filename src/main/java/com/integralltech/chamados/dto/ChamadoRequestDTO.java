package com.integralltech.chamados.dto;

import com.integralltech.chamados.model.enums.Prioridade;
import com.integralltech.chamados.model.enums.Setor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChamadoRequestDTO {

    @NotBlank(message = "titulo: campo obrigatorio")
    @Size(min = 5, message = "titulo: deve ter no minimo 5 caracteres")
    private String titulo;

    @NotBlank(message = "descricao: campo obrigatorio")
    private String descricao;

    @NotNull(message = "setor: campo obrigatorio")
    private Setor setor;

    @NotNull(message = "prioridade: campo obrigatorio")
    private Prioridade prioridade;

    @NotBlank(message = "solicitante: campo obrigatorio")
    private String solicitante;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Setor getSetor() { return setor; }
    public void setSetor(Setor setor) { this.setor = setor; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
}
