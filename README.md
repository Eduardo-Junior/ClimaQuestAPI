# ClimaQuest API

REST API para o **ClimaQuest**, jogo educativo sobre conscientização climática desenvolvido com LÖVE 2D. Responsável pela persistência de dados dos jogadores: progresso de missões, conquistas, ranking e recompensa diária.

## Stack

- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Data JPA** + Hibernate
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

## Endpoints

### Jogadores — `/api/players`

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `POST` | `/api/players` | Cria um novo jogador |
| `GET` | `/api/players/{id}` | Retorna dados do jogador pelo UUID |
| `POST` | `/api/players/{id}/daily-reward` | Coleta a recompensa diária de XP |

**Body — criar jogador:**
```json
{
  "codename": "Guardião da Terra",
  "scientistType": "Ecológico",
  "avatarIndex": 1
}
```

### Missões — `/api/players/{playerId}/missions`

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `GET` | `/api/players/{playerId}/missions` | Lista o progresso de todas as missões |
| `POST` | `/api/players/{playerId}/missions/{missionId}` | Registra o resultado de uma missão |

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

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `GET` | `/api/players/{playerId}/achievements` | Lista conquistas desbloqueadas |
| `POST` | `/api/players/{playerId}/achievements/{achievementId}` | Desbloqueia uma conquista |

### Habilidades — `/api/players/{playerId}/skills`

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `GET` | `/api/players/{playerId}/skills` | Lista habilidades desbloqueadas |
| `POST` | `/api/players/{playerId}/skills/{skillId}` | Desbloqueia uma habilidade |

### Ranking — `/api/ranking`

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `GET` | `/api/ranking?limit=10` | Retorna os melhores jogadores por XP (máx. 50) |

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

> As variáveis do `.env` precisam ser configuradas nas **variáveis de ambiente do app** no painel da Discloud — o arquivo `.env` local não é enviado no deploy.
