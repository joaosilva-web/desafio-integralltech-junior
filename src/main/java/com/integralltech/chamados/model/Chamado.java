package com.integralltech.chamados.model;

import com.integralltech.chamados.model.enums.Prioridade;
import com.integralltech.chamados.model.enums.Setor;
import com.integralltech.chamados.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chamados")
@Getter
@Setter
@NoArgsConstructor
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Setor setor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ABERTO;

    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    @Column(nullable = false)
    private String solicitante;

    @PrePersist
    private void prePersist() {
        this.dataAbertura = LocalDateTime.now();
        this.status = Status.ABERTO;
    }
}
