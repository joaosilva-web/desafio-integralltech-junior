package com.integralltech.chamados.dto;

import com.integralltech.chamados.model.enums.Prioridade;
import com.integralltech.chamados.model.enums.Setor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
