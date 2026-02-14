# Tech Challenge 5 - Vaga Liberada Notificação

Microsserviço de notificação que consome eventos de **vaga liberada** de uma fila SQS, simula o envio por WhatsApp e registra o histórico no banco de dados.

## O que o projeto faz

Quando uma vaga é liberada no sistema de agenda, uma mensagem é publicada na fila AWS SQS. Este serviço:

1. **Consome** mensagens da fila `notificacao-agenda-queue`
2. **Processa** o payload (paciente, consulta e mensagem)
3. **Simula** o envio via WhatsApp (incluindo latência de rede)
4. **Persiste** cada notificação na tabela `historico_envios` no MySQL

## Stack

- **Java 21** · **Spring Boot 3.4**
- **Spring Data JPA** + **MySQL**
- **Spring Cloud AWS** (SQS) com **LocalStack** em desenvolvimento
- **Docker** + **Docker Compose** para infraestrutura local

## Pré-requisitos

- JDK 21
- Maven
- Docker e Docker Compose

## Como executar

### 1. Subir a infraestrutura

```bash
docker compose up -d
```

Isso sobe:

- **MySQL** na porta `3306` (banco `vaga_liberada_notificacao_db`)
- **LocalStack** na porta `4566` (SQS e SNS)

**Importante:** a fila `notificacao-agenda-queue` precisa existir no LocalStack. Se for criada por outro projeto (ex.: microsserviço de agenda), use o mesmo LocalStack; caso contrário, crie a fila manualmente ou via script de inicialização.

### 2. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação sobe na porta **8081**.

## Configuração

Principais propriedades em `src/main/resources/application.properties`:

| Propriedade | Descrição |
|-------------|-----------|
| `server.port` | Porta HTTP (8081) |
| `spring.datasource.url` | URL do MySQL (localhost:3306) |
| `spring.cloud.aws.endpoint` | Endpoint do LocalStack (http://localhost:4566) |
| `spring.cloud.aws.region.static` | Região AWS (us-east-1) |

Credenciais AWS para LocalStack: `access-key=test`, `secret-key=test`.

## Estrutura do projeto

- **`VagaLiberadaNotificacaoApplication`** – ponto de entrada Spring Boot
- **`NotificacaoListener`** – listener SQS da fila `notificacao-agenda-queue`; deserializa o JSON e chama o serviço de envio
- **`WhatsAppService`** – simula envio por WhatsApp e grava em `NotificacaoHistorico`
- **`NotificacaoSqsDto`** – DTO da mensagem (pacienteId, consultaId, mensagem)
- **`NotificacaoHistorico`** – entidade JPA para o histórico de envios
- **`NotificacaoHistoricoRepository`** – repositório JPA para persistência

## Formato da mensagem na fila

O JSON consumido da fila deve seguir o formato do `NotificacaoSqsDto`:

```json
{
  "pacienteId": 1,
  "consultaId": 10,
  "mensagem": "Uma vaga foi liberada para sua consulta."
}
```

## Testes

```bash
./mvnw test
```

## Licença

Uso acadêmico – Tech Challenge 5 FIAP.
