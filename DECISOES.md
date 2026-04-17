# Decisões Técnicas

Este documento explica as principais decisões tomadas durante o desenvolvimento do sistema de chamados.

---

## 1. Java + Spring Boot

**Decisão:** Usar Java 21 com Spring Boot 3.2.5.

**Por quê:** Spring Boot é o padrão de mercado para APIs REST em Java. Ele fornece autoconfiguração, servidor embutido (Tomcat), integração fácil com banco de dados via JPA e validação via Bean Validation — tudo sem configuração manual. Java 21 é a versão LTS mais recente e estável.

---

## 2. H2 como banco de dados

**Decisão:** Usar H2 (banco em memória) em vez de PostgreSQL ou MySQL.

**Por quê:** O desafio não exige persistência entre reinicializações, e o H2 elimina a necessidade de instalar e configurar um banco externo para rodar o projeto. Qualquer pessoa pode clonar o repositório e rodar sem dependências externas. Em produção, trocar para PostgreSQL seria apenas uma questão de mudar a dependência e as propriedades de conexão no `application.properties` — o código da aplicação não mudaria.

---

## 3. Arquitetura em camadas (Controller → Service → Repository)

**Decisão:** Separar o código em três camadas distintas.

**Por quê:** Cada camada tem uma responsabilidade clara:
- **Controller:** recebe a requisição HTTP e devolve a resposta. Não contém regras de negócio.
- **Service:** contém toda a lógica de negócio (validar status, preencher dataFechamento, etc.).
- **Repository:** único ponto de acesso ao banco de dados.

Essa separação facilita manutenção, testes e evolução do sistema. Se o banco mudar, só o repository é afetado. Se uma regra de negócio mudar, só o service é afetado.

---

## 4. DTOs separados da entidade

**Decisão:** Criar `ChamadoRequestDTO` e `ChamadoResponseDTO` em vez de usar a entidade `Chamado` diretamente nos endpoints.

**Por quê:**
- **Segurança:** evita expor campos internos da entidade (como `status` no request — o cliente não deve poder definir o status manualmente ao criar um chamado).
- **Validação isolada:** as anotações `@NotBlank`, `@Size` ficam no DTO e não poluem a entidade JPA.
- **Flexibilidade:** o formato da resposta pode evoluir sem impactar o banco de dados.

---

## 5. Soft delete no DELETE

**Decisão:** O `DELETE /api/chamados/{id}` não remove o registro do banco. Apenas muda o status para `CANCELADO` e preenche `dataFechamento`.

**Por quê:** É um requisito explícito do sistema. Em sistemas de suporte, o histórico de chamados tem valor: auditorias, relatórios e rastreabilidade dependem de que nenhum registro seja apagado. Esse padrão é chamado de *soft delete*.

---

## 6. Regra de negócio: chamado fechado não pode ser alterado

**Decisão:** Lançar `ChamadoFechadoException` (HTTP 422) ao tentar atualizar ou cancelar um chamado com status `RESOLVIDO` ou `CANCELADO`.

**Por quê:** Reabrir chamados encerrados geraria inconsistência no histórico. Um chamado cancelado ou resolvido representa um estado final — qualquer necessidade nova deve gerar um novo chamado.

---

## 7. Tratamento de erros centralizado com @ControllerAdvice

**Decisão:** Criar um `GlobalExceptionHandler` com `@RestControllerAdvice` para capturar todas as exceções.

**Por quê:** Sem isso, cada controller precisaria de blocos try/catch repetidos. Com o handler global, os controllers ficam limpos e o tratamento de erros fica em um único lugar. O formato da resposta de erro (`{ "erro": "..." }` ou `{ "erros": [...] }`) fica consistente em toda a API.

---

## 8. Integração com Groq via RestClient

**Decisão:** Usar o `RestClient` do Spring (disponível desde Spring Boot 3.2) para chamar a API do Groq, sem adicionar dependências externas como OkHttp ou Feign.

**Por quê:** O `RestClient` já está disponível via `spring-boot-starter-web` e tem uma API fluente e moderna. O Groq foi escolhido por ser gratuito, rápido e compatível com o formato da API da OpenAI.

**Se a API do Groq cair:** a exceção `RestClientException` é capturada e relançada como `IaIndisponivelException`, que o handler global trata retornando HTTP 503 com mensagem clara. O chamado original não é afetado.

---

## 9. Prompt estruturado para retorno em JSON

**Decisão:** O prompt enviado à IA instrui explicitamente a responder **apenas em JSON válido**, sem texto extra ou markdown.

**Por quê:** O parse da resposta é feito manualmente por extração de string. Se a IA retornasse texto livre, o parse falharia. O prompt também inclui os valores aceitos por cada campo (`BAIXA|MEDIA|ALTA|CRITICA`) para reduzir a chance de a IA inventar um valor fora do enum. Caso isso aconteça mesmo assim, o `parseResposta` lança `IaIndisponivelException` com uma mensagem descritiva.

---

## 10. Chave de API fora do controle de versão

**Decisão:** A chave do Groq é armazenada em `application-local.properties`, que está no `.gitignore` e nunca é commitada.

**Por quê:** Commitar chaves de API em repositórios públicos é uma vulnerabilidade grave — bots escaneiam o GitHub em tempo real em busca de chaves expostas. O `application.properties` contém apenas o placeholder `INSIRA_SUA_CHAVE_AQUI`, e o README explica como configurar localmente.

---

## 11. Ausência de Lombok

**Decisão:** Não usar Lombok — getters e setters foram escritos explicitamente.

**Por quê:** O projeto usa Java 25 no ambiente de desenvolvimento, e houve conflito entre a versão do Lombok e o Java 25 durante o processamento de anotações em tempo de compilação. A solução foi remover o Lombok e escrever o código explicitamente. Isso também torna o código mais transparente para leitura e explicação na apresentação.
