# Task Manager API

API REST para gerenciamento de tarefas por usuário, desenvolvida com **Spring Boot**, autenticação **JWT** e persistência em **SQLite**.

Cada usuário autenticado possui a sua própria lista de tarefas: só é possível consultar, atualizar ou remover as tarefas que pertencem a ele.

---

## Funcionalidades

- Cadastro e login de usuários com senha criptografada (BCrypt).
- Geração e validação de token JWT.
- CRUD de usuários.
- CRUD de tarefas vinculadas ao usuário autenticado.
- Controle de acesso por proprietário: um usuário não acessa tarefas de outro.
- Validação de entrada com Bean Validation.
- Tratamento centralizado de exceções com respostas de erro padronizadas.
- Documentação da API com Swagger/OpenAPI.

## Tecnologias

| Tecnologia | Versão / Uso |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Web MVC | API REST |
| Spring Data JPA / Hibernate | Persistência |
| Spring Security | Autenticação e autorização |
| JJWT | Tokens JWT (`jjwt-api`, `jjwt-impl`, `jjwt-jackson` 0.11.5) |
| SQLite (`sqlite-jdbc`) | Banco de dados |
| ModelMapper | Conversão entre entidades e DTOs |
| Bean Validation | Validação de dados |
| Springdoc OpenAPI | Swagger UI |
| Maven | Build e dependências |

## Arquitetura

O projeto segue uma organização em camadas:

```text
Controller  ->  Service  ->  Repository  ->  Banco de dados
     |             |
     v             v
   DTOs        Entidades / ModelMapper
```

- **Controller**: expõe os endpoints REST e recebe/retorna DTOs.
- **Service**: concentra as regras de negócio e a verificação de propriedade das tarefas.
- **Repository**: interfaces Spring Data JPA para acesso ao banco.
- **Security**: filtro JWT, geração/validação de token e carregamento de usuário.
- **Exception**: exceções de domínio e um `@ControllerAdvice` global.

## Pré-requisitos

- **Java 21** ou superior.
- **Maven** (ou usar o wrapper `mvnw` incluso no projeto).
- Nenhuma instalação de banco é necessária: o SQLite cria o arquivo `taskmanager.db` automaticamente.

## Como executar

### Com o Maven Wrapper

No Windows:

```bash
mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

### Com Maven instalado

```bash
mvn spring-boot:run
```

A aplicação sobe em: **http://localhost:8080**

### Gerar o artefato

```bash
mvn clean package
java -jar target/taskmanagerapi-0.0.1-SNAPSHOT.jar
```

## Configuração

As configurações ficam em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlite:taskmanager.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
server.port=8080

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

| Propriedade | Descrição |
|---|---|
| `spring.datasource.url` | Caminho do arquivo do banco SQLite |
| `spring.jpa.hibernate.ddl-auto=update` | Cria/atualiza as tabelas automaticamente |
| `server.port` | Porta do servidor HTTP |
| `jwt.secret` | Chave usada para assinar os tokens (mínimo de 32 caracteres) |
| `jwt.expiration` | Validade do token em milissegundos (padrão: 24 horas) |

> **Importante:** em produção, defina `jwt.secret` por variável de ambiente ou arquivo externo. A chave presente no repositório serve apenas para desenvolvimento.

## Autenticação

A API usa **JWT Bearer Token**. O fluxo é:

1. Cadastre um usuário em `POST /api/auth/register` (ou faça login se já existir).
2. Envie as credenciais em `POST /api/auth/login` e receba um token.
3. Envie o token no cabeçalho `Authorization` das próximas requisições:

```http
Authorization: Bearer <seu_token>
```

Rotas públicas:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `/swagger-ui/**`, `/swagger-ui.html`, `/v3/api-docs/**`

Todas as demais rotas exigem autenticação. Os endpoints de tarefas exigem ainda o papel `ROLE_USER` e que a tarefa pertença ao usuário autenticado.

## Endpoints

### Autenticação

| Método | Rota | Descrição | Autenticação |
|---|---|---|---|
| `POST` | `/api/auth/register` | Cadastra um novo usuário | Não |
| `POST` | `/api/auth/login` | Autentica e retorna o token JWT | Não |

### Usuários

| Método | Rota | Descrição | Autenticação |
|---|---|---|---|
| `POST` | `/api/v1/usuarios` | Cadastra um usuário | Sim |
| `GET` | `/api/v1/usuarios` | Lista todos os usuários | Sim |
| `GET` | `/api/v1/usuarios/{id}` | Busca um usuário por ID | Sim |
| `PUT` | `/api/v1/usuarios/{id}` | Atualiza um usuário | Sim |
| `DELETE` | `/api/v1/usuarios/{id}` | Remove um usuário | Sim |

> **Atenção:** os endpoints de usuários exigem autenticação, mas **não verificam se o usuário autenticado é o dono do registro**. Qualquer usuário logado pode listar, atualizar ou remover outros usuários.
>
> Além disso, `POST /api/v1/usuarios` **não criptografa a senha**. Para cadastro seguro, use sempre `POST /api/auth/register`, que aplica BCrypt.

### Tarefas

| Método | Rota | Descrição | Autenticação |
|---|---|---|---|
| `POST` | `/api/v1/tarefas` | Cria uma tarefa para o usuário autenticado | Sim + `ROLE_USER` |
| `GET` | `/api/v1/tarefas` | Lista as tarefas do usuário autenticado | Sim + `ROLE_USER` |
| `GET` | `/api/v1/tarefas/{id}` | Busca uma tarefa do usuário | Sim + proprietário |
| `PUT` | `/api/v1/tarefas/{id}` | Atualiza uma tarefa do usuário | Sim + proprietário |
| `DELETE` | `/api/v1/tarefas/{id}` | Remove uma tarefa do usuário | Sim + proprietário |

### Modelos de dados

**Usuário — requisição**

```json
{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "senha": "senha123"
}
```

**Usuário — resposta**

```json
{
  "id": 1,
  "nome": "Maria Silva",
  "email": "maria@email.com"
}
```

**Tarefa — requisição**

```json
{
  "titulo": "Estudar Spring Boot",
  "descricao": "Ler o capítulo sobre JWT",
  "concluida": false
}
```

**Tarefa — resposta**

```json
{
  "id": 1,
  "titulo": "Estudar Spring Boot",
  "descricao": "Ler o capítulo sobre JWT",
  "concluida": false
}
```

**Login — resposta**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Cadastro via `/api/auth/register` — resposta (`201`)**

```text
Usuário cadastrado com sucesso
```

Regras de validação principais:

- `nome`: obrigatório, até 100 caracteres.
- `email`: obrigatório e válido.
- `senha`: obrigatória; em `/api/v1/usuarios`, mínimo de 6 caracteres.
- `titulo` da tarefa: obrigatório, até 100 caracteres.
- `descricao` da tarefa: até 500 caracteres.

> Ao criar uma tarefa (`POST`), o campo `concluida` é ignorado e a tarefa sempre nasce como `false`. Para marcar como concluída, use `PUT /api/v1/tarefas/{id}`.

## Exemplos de uso

### 1. Cadastrar usuário

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Silva",
    "email": "maria@email.com",
    "senha": "senha123"
  }'
```

### 2. Fazer login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria@email.com",
    "senha": "senha123"
  }'
```

Resposta:

```json
{ "token": "eyJhbGciOiJIUzI1NiIs..." }
```

### 3. Criar uma tarefa

```bash
curl -X POST http://localhost:8080/api/v1/tarefas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{
    "titulo": "Estudar Spring Boot",
    "descricao": "Ler o capítulo sobre JWT",
    "concluida": false
  }'
```

### 4. Listar tarefas do usuário

```bash
curl http://localhost:8080/api/v1/tarefas \
  -H "Authorization: Bearer SEU_TOKEN"
```

### 5. Concluir uma tarefa

```bash
curl -X PUT http://localhost:8080/api/v1/tarefas/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{
    "titulo": "Estudar Spring Boot",
    "descricao": "Ler o capítulo sobre JWT",
    "concluida": true
  }'
```

## Tratamento de erros

Os erros lançados pelo `GlobalExceptionHandler` seguem o formato:

```json
{
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Tarefa não encontrada."
}
```

| Código | Situação |
|---|---|
| `400 Bad Request` | Dados inválidos na requisição |
| `401 Unauthorized` | Token ausente/inválido ou credenciais incorretas |
| `404 Not Found` | Usuário ou tarefa não encontrado; tentativa de acesso a tarefa de outro usuário |
| `409 Conflict` | E-mail já cadastrado no cadastro via `/api/auth/register` |
| `500 Internal Server Error` | Erro inesperado no servidor |

Observações sobre o comportamento atual:

- O acesso a uma tarefa de outro usuário é tratado pelo `GlobalExceptionHandler` e retornado como **404**, para não revelar a existência do recurso (apesar de o Swagger documentar 403).
- O `409` do cadastro em `/api/auth/register` retorna uma mensagem em texto simples (`E-mail já cadastrado`), não o JSON padronizado.
- Requisições sem token ou com token inválido são barradas pelo `JwtAuthenticationEntryPoint`, que usa o corpo de erro padrão do Spring Boot.
- Em `/api/v1/usuarios`, um e-mail duplicado não é convertido em `409` e pode resultar em `500`.

## Documentação interativa

Com a aplicação em execução, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui.html (redireciona para `/swagger-ui/index.html`)
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

Para testar endpoints protegidos no Swagger, clique em **Authorize** e informe o token JWT obtido no login.

## Segurança

A API utiliza Spring Security para gerenciar a autenticação e autorização das requisições.

Os principais mecanismos implementados são:

- **Autenticação JWT:** utilização de tokens Bearer para identificar usuários autenticados.
- **Criptografia de senhas:** utilização do BCrypt no fluxo de cadastro por `/api/auth/register`.
- **Controle de acesso:** proteção dos endpoints de tarefas, permitindo operações apenas sobre tarefas pertencentes ao usuário autenticado.
- **Validação de tokens:** verificação dos tokens JWT recebidos nas requisições protegidas.

**Observação:** algumas limitações de autorização e configuração de segurança permanecem identificadas na seção [Limitações conhecidas](#limitações-conhecidas).

## Limitações conhecidas

O projeto apresenta algumas limitações que foram identificadas durante o desenvolvimento:

**Segurança e autorização**

- Os endpoints de `/api/v1/usuarios` não restringem as operações ao próprio usuário autenticado.
- `POST /api/v1/usuarios` não aplica hash BCrypt às senhas. Atualmente, o cadastro seguro deve ser realizado por `/api/auth/register`.
- A chave JWT está definida diretamente no `application.properties` para fins de desenvolvimento e deverá ser externalizada por variável de ambiente em uma configuração de produção.

**Funcionalidades e configuração**

- Não há implementação de refresh tokens ou revogação de tokens JWT.
- Não há configuração explícita de CORS.
- O banco SQLite local (`taskmanager.db`) é criado automaticamente e não deve ser versionado.

**Documentação e tratamento de erros**

- O Swagger documenta `403 Forbidden` para tentativas de acesso a tarefas de outros usuários, mas a API retorna `404 Not Found` nesses casos.
- O cadastro via `/api/auth/register` retorna uma mensagem em texto simples para e-mails duplicados, em vez da resposta JSON padronizada.
- Requisições sem token ou com token inválido utilizam o tratamento padrão do Spring Boot por meio do `JwtAuthenticationEntryPoint`.
- No endpoint `/api/v1/usuarios`, e-mails duplicados podem resultar em `500 Internal Server Error`, em vez de `409 Conflict`.

## Estrutura do projeto

```text
src/main/java/com/mariana/taskmanagerapi/
├── config/        # Segurança, OpenAPI e ModelMapper
├── controller/    # AuthController, UsuarioController, TarefaController
├── dto/           # Objetos de requisição e resposta
├── entity/        # Usuario e Tarefa
├── exception/     # Exceções de domínio e tratamento global
├── repository/    # Interfaces Spring Data JPA
├── security/      # Filtro JWT, serviço de token e UserDetailsService
├── service/       # Regras de negócio
└── TaskmanagerApiApplication.java
```

## Testes

O projeto inclui um teste de carregamento do contexto (`TaskmanagerApiApplicationTests`). Execute com:

```bash
mvn test
```

---

Desenvolvido por **Mariana** como projeto de estudo de APIs REST com Spring Boot, Spring Security e JWT.
