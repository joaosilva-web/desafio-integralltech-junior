# Plano de Execução - Desafio Técnico IntegrAllTech

## Visão Geral
API REST em **Java + Spring Boot** para gerenciamento de chamados de suporte técnico, com integração de IA para análise.

**Prazo:** 21/04/2026 às 12h  
**Repositório:** `desafio-integralltech-junior` (público no GitHub)

---

## Checklist de Entrega

### Repositório e Documentação
- [ ] Criar repositório público no GitHub: `desafio-integralltech-junior`
- [ ] Escrever `README.md` com instruções de como rodar o projeto
- [ ] Escrever `DECISOES.md` com as decisões técnicas tomadas e seus porquês
- [ ] Commits semânticos ao longo do desenvolvimento (feat:, fix:, refactor:, etc.)

---

## Parte 1 — API REST de Chamados (Obrigatório)

### Estrutura do Projeto Spring Boot
- [ ] Criar projeto via Spring Initializr com:
  - Spring Web
  - Spring Data JPA
  - H2 Database
  - Validation (Bean Validation)
- [ ] Organizar pacotes em camadas: `controller`, `service`, `repository`, `model`, `dto`, `exception`

### Entidade `Chamado`
- [ ] `id` — Long, gerado automaticamente
- [ ] `titulo` — String, obrigatório, mínimo 5 caracteres
- [ ] `descricao` — String, obrigatório
- [ ] `setor` — Enum: `TI`, `MANUTENCAO`, `RH`, `FINANCEIRO`
- [ ] `prioridade` — Enum: `BAIXA`, `MEDIA`, `ALTA`, `CRITICA`
- [ ] `status` — Enum: `ABERTO`, `EM_ATENDIMENTO`, `RESOLVIDO`, `CANCELADO`
- [ ] `dataAbertura` — LocalDateTime, gerada automaticamente na criação
- [ ] `dataFechamento` — LocalDateTime, preenchida ao RESOLVER ou CANCELAR
- [ ] `solicitante` — String, obrigatório

### Endpoints Obrigatórios

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/chamados` | Criar novo chamado |
| GET | `/api/chamados` | Listar todos os chamados |
| GET | `/api/chamados/{id}` | Buscar chamado por ID |
| PUT | `/api/chamados/{id}` | Atualizar chamado existente |
| DELETE | `/api/chamados/{id}` | Cancelar (não deletar) um chamado |
| GET | `/api/chamados/setor/{setor}` | Filtrar chamados por setor |

- [ ] `POST /api/chamados` — retorna 201 Created
- [ ] `GET /api/chamados` — retorna lista de chamados
- [ ] `GET /api/chamados/{id}` — retorna 404 se não encontrado
- [ ] `PUT /api/chamados/{id}` — atualiza campos permitidos
- [ ] `DELETE /api/chamados/{id}` — muda status para CANCELADO + preenche dataFechamento
- [ ] `GET /api/chamados/setor/{setor}` — filtra por setor

### Regras de Negócio
- [ ] Todo chamado nasce com status `ABERTO`
- [ ] DELETE não remove do banco — apenas cancela
- [ ] Não é possível reabrir chamado `CANCELADO` ou `RESOLVIDO`
- [ ] Ao mudar status para `RESOLVIDO` ou `CANCELADO`, preencher `dataFechamento`
- [ ] Validações retornam mensagens claras com HTTP adequado (400, 404, 409, etc.)

### Tratamento de Erros
- [ ] Handler global com `@ControllerAdvice`
- [ ] Retornar `{ "erros": [...] }` para erros de validação (400)
- [ ] Retornar mensagem clara para 404 (chamado não encontrado)
- [ ] Tratar enum inválido no request

---

## Parte 2 — Integração com IA (Obrigatório)

### Endpoint de Análise

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/chamados/{id}/analisar` | Enviar chamado para análise por IA |

- [ ] Buscar chamado pelo ID
- [ ] Montar prompt com título e descrição do chamado
- [ ] Chamar API de IA (Claude API via `claude-sonnet-4-6` ou Groq como alternativa gratuita)
- [ ] Parsear resposta da IA e extrair:
  - `prioridadeSugerida` (BAIXA/MEDIA/ALTA/CRITICA)
  - `setorSugerido` (TI/MANUTENCAO/RH/FINANCEIRO)
  - `resumo` (até 2 frases)
- [ ] Retornar `AnaliseResponseDTO` com os campos acima + `chamadoId` + `analisadoEm`
- [ ] **Não alterar** o chamado original automaticamente
- [ ] Tratar falha na API de IA (timeout, indisponibilidade) com mensagem adequada
- [ ] Tratar caso em que IA retorne valor fora do enum

### Estrutura do Response de Análise
```json
{
  "chamadoId": 1,
  "prioridadeSugerida": "ALTA",
  "setorSugerido": "TI",
  "resumo": "Equipamento inoperante afetando produtividade.",
  "analisadoEm": "2026-04-16T10:32:15"
}
```

---

## Parte 3 — Front-end React (Bônus)

- [ ] Criar app React (Vite ou CRA)
- [ ] Tela: listagem de chamados
- [ ] Tela/modal: formulário para criar novo chamado
- [ ] Funcionalidade: exibir resultado da análise de IA de um chamado
- [ ] Consumir a API Spring Boot local

---

## Arquitetura Proposta

```
src/main/java/com/joao/chamados/
├── controller/
│   └── ChamadoController.java
├── service/
│   ├── ChamadoService.java
│   └── IaAnaliseService.java
├── repository/
│   └── ChamadoRepository.java
├── model/
│   ├── Chamado.java
│   └── enums/
│       ├── Setor.java
│       ├── Prioridade.java
│       └── Status.java
├── dto/
│   ├── ChamadoRequestDTO.java
│   ├── ChamadoResponseDTO.java
│   └── AnaliseResponseDTO.java
└── exception/
    ├── GlobalExceptionHandler.java
    └── ChamadoNaoEncontradoException.java
```

---

## Ordem de Desenvolvimento

1. Criar projeto Spring Boot e configurar H2
2. Criar enums e entidade `Chamado`
3. Criar repository e service básico (CRUD)
4. Criar controller com os 6 endpoints
5. Implementar regras de negócio no service
6. Implementar tratamento de erros global
7. Implementar integração com IA (serviço separado)
8. Criar endpoint `/analisar`
9. Testar todos os endpoints manualmente
10. Escrever README.md e DECISOES.md
11. [Bônus] Criar frontend React
12. Revisar commits e organizar histórico

---

## Decisões Técnicas (rascunho para DECISOES.md)

- **H2** para banco em memória — facilita rodar sem configuração externa
- **DTOs** separados de entity — evita expor detalhes internos e facilita validação no request
- **`@ControllerAdvice`** para centralizar tratamento de erros
- **Soft delete** no DELETE — requisito explícito do desafio
- **Serviço de IA isolado** — facilita trocar provedor ou mockar nos testes
- **Prompt estruturado** para IA retornar JSON parseable com os campos esperados

---

## Perguntas da Apresentação (preparação)

- Fluxo de uma requisição POST do controller até o banco
- Por que usar DTOs?
- O que acontece com enum inválido no request?
- Como funciona `@RestController` vs `@Controller`?
- Como montar o prompt para a IA?
- O que acontece se a API da IA cair?
- Como adicionar autenticação (Spring Security + JWT)?
- Como escalar para 10.000 chamados/dia?
- Como adicionar testes automatizados?
