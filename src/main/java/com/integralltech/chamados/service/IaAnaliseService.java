package com.integralltech.chamados.service;

import com.integralltech.chamados.dto.AnaliseResponseDTO;
import com.integralltech.chamados.model.Chamado;
import com.integralltech.chamados.model.enums.Prioridade;
import com.integralltech.chamados.model.enums.Setor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class IaAnaliseService {

    public AnaliseResponseDTO analisar(Chamado chamado) {
        // integração com a API de IA será implementada aqui
        return null;
    }

    protected String montarPrompt(Chamado chamado) {
        return String.format("""
                Você é um assistente de suporte técnico. Analise o chamado abaixo e responda APENAS em JSON válido, sem texto extra.

                Título: %s
                Descrição: %s

                Responda no seguinte formato:
                {
                  "prioridadeSugerida": "<BAIXA|MEDIA|ALTA|CRITICA>",
                  "setorSugerido": "<TI|MANUTENCAO|RH|FINANCEIRO>",
                  "resumo": "<resumo do problema em até 2 frases>"
                }
                """, chamado.getTitulo(), chamado.getDescricao());
    }

    protected AnaliseResponseDTO parseResposta(Long chamadoId, String respostaJson) {
        try {
            String prioridade = extrairValor(respostaJson, "prioridadeSugerida");
            String setor = extrairValor(respostaJson, "setorSugerido");
            String resumo = extrairValor(respostaJson, "resumo");

            return new AnaliseResponseDTO(
                    chamadoId,
                    Prioridade.valueOf(prioridade.toUpperCase()),
                    Setor.valueOf(setor.toUpperCase()),
                    resumo,
                    LocalDateTime.now()
            );
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("A IA retornou um valor fora do domínio esperado: " + e.getMessage());
        }
    }

    private String extrairValor(String json, String campo) {
        int inicio = json.indexOf("\"" + campo + "\"");
        if (inicio == -1) throw new RuntimeException("Campo '" + campo + "' não encontrado na resposta da IA");
        int doisPontos = json.indexOf(":", inicio);
        int abreAspas = json.indexOf("\"", doisPontos);
        int fechaAspas = json.indexOf("\"", abreAspas + 1);
        return json.substring(abreAspas + 1, fechaAspas).trim();
    }
}
