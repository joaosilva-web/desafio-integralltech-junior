package com.integralltech.chamados.service;

import com.integralltech.chamados.dto.ChamadoRequestDTO;
import com.integralltech.chamados.dto.ChamadoResponseDTO;
import com.integralltech.chamados.exception.ChamadoFechadoException;
import com.integralltech.chamados.exception.ChamadoNaoEncontradoException;
import com.integralltech.chamados.model.Chamado;
import com.integralltech.chamados.model.enums.Setor;
import com.integralltech.chamados.model.enums.Status;
import com.integralltech.chamados.repository.ChamadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository repository;

    public ChamadoService(ChamadoRepository repository) {
        this.repository = repository;
    }

    public ChamadoResponseDTO criar(ChamadoRequestDTO dto) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setSetor(dto.getSetor());
        chamado.setPrioridade(dto.getPrioridade());
        chamado.setSolicitante(dto.getSolicitante());
        return new ChamadoResponseDTO(repository.save(chamado));
    }

    public List<ChamadoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(ChamadoResponseDTO::new)
                .toList();
    }

    public ChamadoResponseDTO buscarPorId(Long id) {
        return new ChamadoResponseDTO(encontrar(id));
    }

    public ChamadoResponseDTO atualizar(Long id, ChamadoRequestDTO dto) {
        Chamado chamado = encontrar(id);
        validarNaoFechado(chamado);

        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setSetor(dto.getSetor());
        chamado.setPrioridade(dto.getPrioridade());
        chamado.setSolicitante(dto.getSolicitante());

        return new ChamadoResponseDTO(repository.save(chamado));
    }

    public ChamadoResponseDTO cancelar(Long id) {
        Chamado chamado = encontrar(id);
        validarNaoFechado(chamado);

        chamado.setStatus(Status.CANCELADO);
        chamado.setDataFechamento(LocalDateTime.now());
        return new ChamadoResponseDTO(repository.save(chamado));
    }

    public List<ChamadoResponseDTO> listarPorSetor(Setor setor) {
        return repository.findBySetor(setor)
                .stream()
                .map(ChamadoResponseDTO::new)
                .toList();
    }

    public Chamado encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException(id));
    }

    private void validarNaoFechado(Chamado chamado) {
        if (chamado.getStatus() == Status.CANCELADO || chamado.getStatus() == Status.RESOLVIDO) {
            throw new ChamadoFechadoException(chamado.getId());
        }
    }
}
