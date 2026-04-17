package com.integralltech.chamados.repository;

import com.integralltech.chamados.model.Chamado;
import com.integralltech.chamados.model.enums.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findBySetor(Setor setor);
}
