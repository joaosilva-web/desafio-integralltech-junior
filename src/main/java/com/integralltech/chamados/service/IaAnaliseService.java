package com.integralltech.chamados.service;

import com.integralltech.chamados.dto.AnaliseResponseDTO;
import com.integralltech.chamados.exception.IaIndisponivelException;
import com.integralltech.chamados.model.Chamado;
import com.integralltech.chamados.model.enums.Prioridade;
import com.integralltech.chamados.model.enums.Setor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class IaAnaliseService {

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama-3.3-70b-versatile";

    private final RestClient restClient;

    @Value("${groq.api.key}")
    private String apiKey;

    public IaAnaliseService() {
        this.restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public AnaliseResponseDTO analisar(Chamado chamado) {
        String prompt = montarPrompt(chamado);
        String conteudo = chamarGroq(prompt);
        return parseResposta(chamado.getId(), conteudo);
    }

    private String chamarGroq(String prompt) {
        Map<String, Object> body = Map.of(
                "model", MODEL,
                "temperature", 0.3,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        try {
            GroqResponse resposta = restClient.post()
                    .uri(GROQ_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .body(body)
                    .retrieve()
                    .body(GroqResponse.class);

            if (resposta == null || resposta.choices() == null || resposta.choices().isEmpty()) {
                throw new IaIndisponivelException("resposta vazia recebida da API");
            }

            return resposta.choices().get(0).message().content();

        } catch (RestClientException e) {
            throw new IaIndisponivelException(e.getMessage());
        }
    }

    protected String montarPrompt(Chamado chamado) {
        return String.format("""
                Você é um assistente de suporte técnico. Analise o chamado abaixo e responda APENAS em JSON válido, sem texto extra, sem markdown.

                Título: %s
                Descrição: %s

                Responda exatamente neste formato JSON:
                {
                  "prioridadeSugerida": "<BAIXA|MEDIA|ALTA|CRITICA>",
                  "setorSugerido": "<TI|MANUTENCAO|RH|FINANCEIRO>",
                  "resumo": "<resumo do problema em até 2 frases>"
                }
                """, chamado.getTitulo(), chamado.getDescricao());
    }

    protected AnaliseResponseDTO parseResposta(Long chamadoId, String respostaJson) {
        try {
            String conteudo = respostaJson.trim();
            // remove blocos markdown caso o modelo envolva com ```json ... ```
            if (conteudo.startsWith("```")) {
                int inicio = conteudo.indexOf('\n') + 1;
                int fim = conteudo.lastIndexOf("```");
                conteudo = conteudo.substring(inicio, fim).trim();
            }

            String prioridade = extrairValor(conteudo, "prioridadeSugerida");
            String setor = extrairValor(conteudo, "setorSugerido");
            String resumo = extrairValor(conteudo, "resumo");

            return new AnaliseResponseDTO(
                    chamadoId,
                    Prioridade.valueOf(prioridade.toUpperCase()),
                    Setor.valueOf(setor.toUpperCase()),
                    resumo,
                    LocalDateTime.now()
            );
        } catch (IllegalArgumentException e) {
            throw new IaIndisponivelException("valor fora do domínio esperado: " + e.getMessage());
        }
    }

    private String extrairValor(String json, String campo) {
        int inicio = json.indexOf("\"" + campo + "\"");
        if (inicio == -1) {
            throw new IaIndisponivelException("campo '" + campo + "' não encontrado na resposta da IA");
        }
        int doisPontos = json.indexOf(":", inicio);
        int abreAspas = json.indexOf("\"", doisPontos);
        int fechaAspas = json.indexOf("\"", abreAspas + 1);
        return json.substring(abreAspas + 1, fechaAspas).trim();
    }

    // Records para deserializar a resposta da API do Groq
    private record GroqResponse(List<Choice> choices) {}
    private record Choice(Message message) {}
    private record Message(String content) {}
}
