# Chamados - Sistema de Gerenciamento de Suporte Técnico

API REST desenvolvida em Java com Spring Boot para gerenciamento de chamados de suporte técnico.  
Projeto desenvolvido como desafio técnico para o processo seletivo da **IntegrAllTech 2026**.

---

## Tecnologias

**Back-end**
- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Validation
- H2 Database (banco em memória)
- Maven

**Front-end**
- React 19
- Vite
- CSS puro (sem biblioteca de UI)

**Integração**
- Groq API (modelo `llama-3.3-70b-versatile`)

---

## Pré-requisitos

- [Java 21+](https://adoptium.net/) instalado
- Maven instalado **ou** usar o wrapper incluído (`mvnw`)
- [Node.js 18+](https://nodejs.org/) instalado (para o front-end)
- Chave de API do [Groq](https://console.groq.com) (gratuita)

Verifique suas versões:

```bash
java --version
mvn --version
node --version
```

---

## Como rodar o projeto

### 1. Clone o repositório

```bash
git clone https://github.com/joaosilva-web/desafio-integralltech-junior.git
cd desafio-integralltech-junior
```

### 2. Configure a chave da API de IA

Crie o arquivo `src/main/resources/application-local.properties` com sua chave do Groq:

```properties
groq.api.key=SUA_CHAVE_AQUI
```

> Obtenha sua chave gratuitamente em [console.groq.com](https://console.groq.com) → API Keys → Create API Key

### 3. Rode o back-end

**Com Maven instalado:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

**Com o wrapper (sem Maven instalado):**
```bash
# Linux / macOS
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Windows
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

A API sobe na porta `8080`. Console H2 disponível em:
```
http://localhost:8080/h2-console
```
> JDBC URL: `jdbc:h2:mem:chamadosdb` | Usuário: `sa` | Senha: *(vazio)*

### 4. Rode o front-end

Em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

O front-end sobe na porta `5173`:
```
http://localhost:5173
```

> O Vite possui um proxy configurado — todas as chamadas para `/api` são redirecionadas automaticamente para `http://localhost:8080`. O back-end precisa estar rodando.

---

## Endpoints

### Chamados

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/chamados` | Criar novo chamado |
| `GET` | `/api/chamados` | Listar todos os chamados |
| `GET` | `/api/chamados/{id}` | Buscar chamado por ID |
| `PUT` | `/api/chamados/{id}` | Atualizar chamado existente |
| `DELETE` | `/api/chamados/{id}` | Cancelar chamado (soft delete) |
| `GET` | `/api/chamados/setor/{setor}` | Filtrar chamados por setor |
| `POST` | `/api/chamados/{id}/analisar` | Analisar chamado com IA |

---

## Exemplos de Uso

### Criar um chamado

```bash
curl -X POST http://localhost:8080/api/chamados \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Computador nao liga",
    "descricao": "O computador da sala 302 nao liga desde ontem.",
    "setor": "TI",
    "prioridade": "MEDIA",
    "solicitante": "Maria Silva"
  }'
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "titulo": "Computador nao liga",
  "descricao": "O computador da sala 302 nao liga desde ontem.",
  "setor": "TI",
  "prioridade": "MEDIA",
  "status": "ABERTO",
  "dataAbertura": "2026-04-17T10:30:00",
  "dataFechamento": null,
  "solicitante": "Maria Silva"
}
```

---

### Listar todos os chamados

```bash
curl http://localhost:8080/api/chamados
```

---

### Buscar por ID

```bash
curl http://localhost:8080/api/chamados/1
```

---

### Atualizar chamado

```bash
curl -X PUT http://localhost:8080/api/chamados/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Computador nao liga - URGENTE",
    "descricao": "Maquina principal do gestor da sala 302.",
    "setor": "TI",
    "prioridade": "ALTA",
    "solicitante": "Maria Silva"
  }'
```

---

### Cancelar chamado (soft delete)

O chamado **não é removido** do banco — apenas tem seu status alterado para `CANCELADO` e a `dataFechamento` preenchida.

```bash
curl -X DELETE http://localhost:8080/api/chamados/1
```

---

### Filtrar por setor

Setores disponíveis: `TI`, `MANUTENCAO`, `RH`, `FINANCEIRO`

```bash
curl http://localhost:8080/api/chamados/setor/TI
```

---

### Analisar chamado com IA

```bash
curl -X POST http://localhost:8080/api/chamados/1/analisar
```

**Resposta (200 OK):**
```json
{
  "chamadoId": 1,
  "prioridadeSugerida": "ALTA",
  "setorSugerido": "TI",
  "resumo": "Equipamento inoperante afetando a produtividade do setor.",
  "analisadoEm": "2026-04-17T10:32:15"
}
```

---

## Valores aceitos nos campos Enum

| Campo | Valores aceitos |
|-------|----------------|
| `setor` | `TI`, `MANUTENCAO`, `RH`, `FINANCEIRO` |
| `prioridade` | `BAIXA`, `MEDIA`, `ALTA`, `CRITICA` |
| `status` | `ABERTO`, `EM_ATENDIMENTO`, `RESOLVIDO`, `CANCELADO` |

---

## Regras de negócio

- Todo chamado é criado com status `ABERTO`
- Não é possível alterar ou cancelar um chamado com status `RESOLVIDO` ou `CANCELADO`
- O `DELETE` realiza um **soft delete**: muda o status para `CANCELADO` e registra a `dataFechamento`
- A `dataAbertura` é gerada automaticamente no momento da criação
- A `dataFechamento` é preenchida automaticamente ao cancelar ou resolver um chamado

---

## Tratamento de erros

**Dados inválidos (400):**
```json
{
  "erros": [
    "titulo: deve ter no minimo 5 caracteres",
    "descricao: campo obrigatorio",
    "solicitante: campo obrigatorio"
  ]
}
```

**Chamado não encontrado (404):**
```json
{
  "erro": "Chamado não encontrado com id: 99"
}
```

**Chamado fechado (422):**
```json
{
  "erro": "Chamado 1 está fechado e não pode ser alterado"
}
```

---

## Estrutura do projeto

```
desafio-integralltech-junior/
│
├── src/main/java/com/integralltech/chamados/
│   ├── controller/         # Recebe as requisições HTTP
│   │   └── ChamadoController.java
│   ├── service/            # Regras de negócio
│   │   ├── ChamadoService.java
│   │   └── IaAnaliseService.java
│   ├── repository/         # Acesso ao banco de dados
│   │   └── ChamadoRepository.java
│   ├── model/              # Entidade JPA
│   │   ├── Chamado.java
│   │   └── enums/
│   │       ├── Setor.java
│   │       ├── Prioridade.java
│   │       └── Status.java
│   ├── dto/                # Objetos de transferência de dados
│   │   ├── ChamadoRequestDTO.java
│   │   ├── ChamadoResponseDTO.java
│   │   └── AnaliseResponseDTO.java
│   └── exception/          # Tratamento de erros
│       ├── GlobalExceptionHandler.java
│       ├── ChamadoNaoEncontradoException.java
│       ├── ChamadoFechadoException.java
│       └── IaIndisponivelException.java
│
└── frontend/               # Interface React (bônus)
    └── src/
        ├── App.jsx          # Componente raiz com estado global
        └── components/
            ├── ChamadoList.jsx   # Tabela de chamados
            ├── ChamadoForm.jsx   # Formulário de criação
            └── AnaliseModal.jsx  # Modal com resultado da IA
```

---

## Autor

**João Gustavo Ribeiro da Silva**  
Desafio Técnico — IntegrAllTech Processo Seletivo 2026
