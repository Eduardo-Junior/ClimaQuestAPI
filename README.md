# ClimaQuest API

REST API para o **ClimaQuest**, jogo educativo sobre conscientização climática desenvolvido com LÖVE 2D. Responsável pela persistência de dados dos jogadores: progresso de missões, conquistas, ranking e recompensa diária.

## Stack

- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Data JPA** + Hibernate
- **Spring Security** + **JJWT** (autenticação via JSON Web Token)
- **PostgreSQL** (hospedado no [Supabase](https://supabase.com))
- **Lombok**
- **SpringDoc OpenAPI** (Swagger UI)
- Deploy: [Discloud](https://discloud.app) — `https://climaquest.discloud.app`

## Pré-requisitos

- Java 21+
- Maven 3.9+ (ou use o wrapper `mvnw` incluído no projeto)
- Banco PostgreSQL acessível (local ou Supabase)

## Rodando localmente

### 1. Clone o repositório

```bash
git clone <url-do-repo>
cd cqapi
```

### 2. Configure as variáveis de ambiente

Copie o arquivo de exemplo e preencha com suas credenciais:

```bash
cp .env.example .env
```

Edite o `.env`:

```env
DB_HOST=aws-1-us-west-2.pooler.supabase.com
DB_NAME=postgres
DB_USER=postgres.seu-project-ref
DB_PASSWORD=sua-senha

# Chave usada para assinar os JWTs (ex: openssl rand -base64 64)
JWT_SECRET=sua-chave-secreta
# Tempo de expiração do token em milissegundos (padrão: 24h)
JWT_EXPIRATION_MS=86400000
```

> O arquivo `.env` já está no `.gitignore` — nunca o commite.

### 3. Inicie a aplicação

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```cmd
mvnw.cmd spring-boot:run
```

A API sobe em **http://localhost:8080**.

### 4. Swagger UI

Documentação interativa disponível em:

```
http://localhost:8080/swagger-ui.html
```

## Autenticação

A maior parte da API exige um **JWT** no header `Authorization`. O fluxo é:

1. **Criar o jogador** — `POST /api/players` (rota pública, é o "cadastro").
2. **Login** — `POST /api/auth/login` enviando o `codename` do jogador, que retorna o token.
3. **Usar o token** — enviar `Authorization: Bearer <token>` em todas as demais chamadas.

**Body — login:**
```json
{
  "codename": "Guardião da Terra"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "playerId": "64ed2cab-f4bd-4c55-8622-908ae4fdee3d",
  "codename": "Guardião da Terra"
}
```

O token expira após `JWT_EXPIRATION_MS` (padrão 24h). Requisições sem token, com token inválido ou expirado recebem `401 Unauthorized`.

### Rotas públicas (sem token)

- `POST /api/players` (criação de jogador)
- `POST /api/auth/login`
- Swagger UI / OpenAPI (`/swagger-ui/**`, `/v3/api-docs/**`)

Todas as demais rotas exigem o token.

## Rate Limiting

A API limita o número de requisições por minuto para evitar abuso:

| Rotas | Limite | Identificado por |
|-------|--------|-------------------|
| `POST /api/auth/login` e `POST /api/players` | 5 req/min | endereço IP |
| Demais rotas autenticadas | 60 req/min | jogador (via token) |

Ao exceder o limite, a API responde `429 Too Many Requests` com header `Retry-After: 60`.

## Endpoints

### Autenticação — `/api/auth`

| Método | Caminho | Acesso | Descrição |
|--------|---------|--------|-----------|
| `POST` | `/api/auth/login` | público | Faz login pelo `codename` e retorna o JWT |

### Jogadores — `/api/players`

| Método | Caminho | Acesso | Descrição |
|--------|---------|--------|-----------|
| `POST` | `/api/players` | público | Cria um novo jogador |
| `GET` | `/api/players/{id}` | autenticado | Retorna dados do jogador pelo UUID |
| `POST` | `/api/players/{id}/daily-reward` | autenticado | Coleta a recompensa diária de XP |

**Body — criar jogador:**
```json
{
  "codename": "Guardião da Terra",
  "scientistType": "Ecológico",
  "avatarIndex": 1
}
```

### Missões — `/api/players/{playerId}/missions`

| Método | Caminho | Acesso | Descrição |
|--------|---------|--------|-----------|
| `GET` | `/api/players/{playerId}/missions` | autenticado | Lista o progresso de todas as missões |
| `POST` | `/api/players/{playerId}/missions/{missionId}` | autenticado | Registra o resultado de uma missão |

**Body — enviar missão:**
```json
{
  "success": true,
  "correctAnswers": 5,
  "totalQuestions": 5,
  "usedHints": false
}
```

### Conquistas — `/api/players/{playerId}/achievements`

| Método | Caminho | Acesso | Descrição |
|--------|---------|--------|-----------|
| `GET` | `/api/players/{playerId}/achievements` | autenticado | Lista conquistas desbloqueadas |
| `POST` | `/api/players/{playerId}/achievements/{achievementId}` | autenticado | Desbloqueia uma conquista |

### Habilidades — `/api/players/{playerId}/skills`

| Método | Caminho | Acesso | Descrição |
|--------|---------|--------|-----------|
| `GET` | `/api/players/{playerId}/skills` | autenticado | Lista habilidades desbloqueadas |
| `POST` | `/api/players/{playerId}/skills/{skillId}` | autenticado | Desbloqueia uma habilidade |

### Ranking — `/api/ranking`

| Método | Caminho | Acesso | Descrição |
|--------|---------|--------|-----------|
| `GET` | `/api/ranking?limit=10` | autenticado | Retorna os melhores jogadores por XP (máx. 50) |

## XP e Nível

O XP é concedido na recompensa diária e ao completar missões. O nível é calculado pela fórmula:

```
nivel = floor(xp / 500) + 1
```

## Deploy (Discloud)

Gere o JAR e faça o upload via painel ou CLI da Discloud:

```bash
./mvnw package -DskipTests
```

O arquivo gerado será `target/cqapi-0.0.1-SNAPSHOT.jar`. Junto com o `discloud.config`, é tudo que a Discloud precisa para subir a aplicação.

> As variáveis do `.env` (incluindo `JWT_SECRET` e `JWT_EXPIRATION_MS`) precisam ser configuradas nas **variáveis de ambiente do app** no painel da Discloud — o arquivo `.env` local não é enviado no deploy.
