# API REST Cinema

## Requisitos Implementados

### ✅ 1. Mapeamento Objeto-Relacional (JPA/Hibernate)

**Entidades implementadas:**
- `Filme` - Filmes do cinema
- `Sessao` - Sessões de exibição
- `Sala` - Salas de cinema
- `Ingresso` - Ingressos vendidos
- `Usuario` - Usuários do sistema
- `Perfil` - Perfis de autorização (ROLES)

**Relacionamentos:**
- `@ManyToOne`: Sessao → Filme, Sessao → Sala, Ingresso → Sessao
- `@OneToMany`: Sala → Sessoes, Sessao → Ingressos
- `@ManyToMany`: Usuario ↔ Perfis

**Arquivos:** `src/main/java/br/edu/ifsul/cstsi/tads_rivero/*/`

---

### ✅ 2. Spring Rest, Repositories e Documentação da API

**Repositories:**
- `FilmeRepository extends JpaRepository<Filme, Long>`
- `SessaoRepository`, `SalaRepository`, `IngressoRepository`, `UsuarioRepository`
- Método customizado: `findByTituloContainingIgnoreCase(String titulo)`

**Documentação:**
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- Configuração em: `SpringDocConfiguration.java`

---

### ✅ 3. Controllers e Services

**FilmeController** (`/api/v1/filmes`):
- `GET /api/v1/filmes` - Listar todos (público)
- `GET /api/v1/filmes/{id}` - Buscar por ID (público)
- `GET /api/v1/filmes/titulo/{titulo}` - Buscar por título (público)
- `POST /api/v1/filmes` - Criar filme (requer ADMIN ou GERENTE)
- `PUT /api/v1/filmes/{id}` - Atualizar filme (requer ADMIN ou GERENTE)
- `DELETE /api/v1/filmes/{id}` - Deletar filme (requer ADMIN)

**FilmeService**:
- Lógica de negócio separada do controller
- Uso de `@Transactional` para operações de escrita

---

### ✅ 4. Autenticação JWT e Autorização Global Method

**Autenticação:**
- Endpoint de login: `POST /login`
- Token JWT com expiração de 2 horas
- `TokenService` para geração e validação de tokens
- `SecurityFilter` para interceptar requisições

**Autorização com @PreAuthorize:**
```java
@PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")  // POST, PUT
@PreAuthorize("hasRole('ADMIN')")  // DELETE
```

**Configuração:**
- `@EnableMethodSecurity(prePostEnabled = true)` em `SecurityConfig`
- Spring Security com JWT
- Perfis: ROLE_ADMIN, ROLE_USER, ROLE_GERENTE

---

### ✅ 5. Validação com Spring Validation

**DTOs com validação:**

**FilmeDTOPost / FilmeDTOPut:**
```java
@NotBlank(message = "O título não pode ser nulo ou vazio")
@Size(min = 2, max = 200, message = "Tamanho mínimo de 2 e máximo de 200")
String titulo;

@NotNull(message = "A duração não pode ser nula")
LocalTime duracao;
```

**AutenticacaoDTO:**
```java
@NotBlank(message = "O email não pode ser nulo ou vazio")
@Email(message = "Email deve ser válido")
String email;

@NotBlank(message = "A senha não pode ser nula ou vazia")
String senha;
```

**Uso:** `@Valid` nos controllers para ativar a validação

---

## Como Executar o Projeto

### Passo 1: Iniciar o Banco de Dados MariaDB

```bash
docker run -d --name my-mariadb \
  -e MYSQL_ROOT_PASSWORD=mariadb \
  -e MYSQL_DATABASE=tads_rivero \
  -e MYSQL_USER=mariadb \
  -e MYSQL_PASSWORD=mariadb \
  -p 3306:3306 \
  mariadb:latest
```

### Passo 2: Compilar o Projeto

```bash
./mvnw clean package -DskipTests
```

### Passo 3: Executar a Aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: http://localhost:8080

---

## Demonstração Prática

### 1. Acessar Documentação Swagger

```
URL: http://localhost:8080/swagger-ui/index.html
```

### 2. Testar Endpoint Público (sem autenticação)

**Listar todos os filmes:**
```bash
curl http://localhost:8080/api/v1/filmes
```

**Resposta esperada:**
```json
[
  {"id":1,"titulo":"Matrix","duracao":"02:16:00"},
  {"id":2,"titulo":"O Senhor dos Anéis","duracao":"03:48:00"},
  {"id":3,"titulo":"Interestelar","duracao":"02:49:00"},
  {"id":4,"titulo":"A Origem","duracao":"02:28:00"},
  {"id":5,"titulo":"Vingadores Ultimato","duracao":"03:01:00"}
]
```

### 3. Testar Endpoint Protegido (sem autenticação - deve falhar)

```bash
curl -X POST http://localhost:8080/api/v1/filmes \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Novo Filme","duracao":"01:30:00"}'
```

**Resultado:** Acesso negado (403 Forbidden) - Autenticação obrigatória

### 4. Fazer Login e Obter Token JWT

**Usuários disponíveis:**
- admin@cinema.com - ROLE_ADMIN
- gerente@cinema.com - ROLE_GERENTE
- user@cinema.com - ROLE_USER

**Senha para todos:** 123456

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@cinema.com","senha":"123456"}'
```

**Resposta esperada:**
```json
{"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."}
```

### 5. Usar o Token para Acessar Endpoint Protegido

```bash
TOKEN="SEU_TOKEN_AQUI"

curl -X POST http://localhost:8080/api/v1/filmes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"titulo":"Novo Filme","duracao":"01:30:00"}'
```

**Resposta esperada:** Status 201 Created com o filme criado

### 6. Testar Autorização por Perfil

**Tentar deletar como GERENTE (deve falhar):**
```bash
# Login como gerente
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"gerente@cinema.com","senha":"123456"}'

# Tentar deletar (só ADMIN pode)
curl -X DELETE http://localhost:8080/api/v1/filmes/1 \
  -H "Authorization: Bearer $TOKEN_GERENTE"
```

**Resultado:** Acesso negado (403) - Apenas ADMIN pode deletar

### 7. Testar Validação de Dados

**Enviar dados inválidos:**
```bash
curl -X POST http://localhost:8080/api/v1/filmes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"titulo":"A","duracao":null}'
```

**Resposta esperada:** Erro 400 com mensagens de validação:
```json
{
  "titulo": "Tamanho mínimo de 2 e máximo de 200",
  "duracao": "A duração não pode ser nula"
}
```

---

## Estrutura do Projeto

```
src/main/java/br/edu/ifsul/cstsi/tads_rivero/
├── filme/
│   ├── Filme.java                 # Entidade JPA
│   ├── FilmeController.java       # REST Controller
│   ├── FilmeService.java          # Lógica de negócio
│   ├── FilmeRepository.java       # Repository JPA
│   ├── FilmeDTOPost.java          # DTO com validações
│   ├── FilmeDTOPut.java           # DTO com validações
│   └── FilmeDTOResponse.java      # DTO de resposta
├── usuario/
│   ├── Usuario.java               # Entidade JPA + UserDetails
│   ├── Perfil.java                # Entidade JPA + GrantedAuthority
│   ├── UsuarioService.java        # UserDetailsService
│   ├── UsuarioRepository.java     # Repository
│   ├── AutenticacaoController.java # Endpoint /login
│   ├── AutenticacaoDTO.java       # DTO com validações
│   └── TokenJwtDTO.java           # DTO de resposta
├── infra/
│   ├── security/
│   │   ├── SecurityConfig.java    # Configuração Spring Security
│   │   ├── SecurityFilter.java    # Filtro JWT
│   │   └── TokenService.java      # Geração/validação JWT
│   ├── docs/
│   │   └── SpringDocConfiguration.java # Configuração Swagger
│   └── exception/
│       └── TratadorDeErros.java   # Exception handlers
└── (outras entidades: sala, sessao, ingresso)
```

---

## Evidências dos Requisitos

### 1. Mapeamento ORM
- **Arquivos**: Todas as classes de entidade (`*.java` nos pacotes)
- **Evidência**: Annotations `@Entity`, `@Table`, `@ManyToOne`, `@OneToMany`, `@ManyToMany`

### 2. Spring Rest, Repositories e Documentação
- **Repositories**: `*Repository.java` extends `JpaRepository`
- **Swagger**: Acessível em `/swagger-ui/index.html`
- **Evidência**: `SpringDocConfiguration.java`, dependência `springdoc-openapi`

### 3. Controllers e Services
- **Controllers**: `*Controller.java` com `@RestController`
- **Services**: `*Service.java` com `@Service` e `@Transactional`
- **Evidência**: Separação de responsabilidades, DTOs

### 4. JWT e Global Method Security
- **JWT**: `TokenService.java`, `SecurityFilter.java`
- **Global Method**: `@EnableMethodSecurity` + `@PreAuthorize` nos endpoints
- **Evidência**: `SecurityConfig.java:20`, `FilmeController.java:58,79,97`

### 5. Spring Validation
- **DTOs**: `@NotBlank`, `@NotNull`, `@Size`, `@Email`
- **Controllers**: `@Valid` nos parâmetros
- **Evidência**: `FilmeDTOPost.java`, `AutenticacaoDTO.java`

---

## Testes Automatizados

O projeto possui testes de integração em:
```
src/test/java/br/edu/ifsul/cstsi/tads_rivero/filme/
```

Para executar os testes:
```bash
./mvnw test
```

---

## Tecnologias Utilizadas

- **Spring Boot 3.2.4**
- **Spring Data JPA** - ORM
- **Spring Security** - Autenticação e Autorização
- **JWT (auth0/java-jwt)** - Tokens de autenticação
- **Spring Validation** - Validação de dados
- **SpringDoc OpenAPI** - Documentação Swagger
- **MariaDB** - Banco de dados
- **Lombok** - Redução de boilerplate
- **H2** - Banco para testes

---

## Dados de Teste

O arquivo `data.sql` popula o banco automaticamente com:

**Perfis:**
- ROLE_ADMIN
- ROLE_USER
- ROLE_GERENTE

**Usuários:**
- admin@cinema.com (ROLE_ADMIN)
- user@cinema.com (ROLE_USER)
- gerente@cinema.com (ROLE_GERENTE)

**Filmes:**
- Matrix
- O Senhor dos Anéis
- Interestelar
- A Origem
- Vingadores Ultimato

---

## Conclusão

Este projeto implementa **TODOS os 5 requisitos obrigatórios** do trabalho:

1. ✅ Mapeamento objeto-relacional com JPA/Hibernate
2. ✅ Spring Rest com Repositories e documentação Swagger
3. ✅ Controllers e Services com separação de responsabilidades
4. ✅ Autenticação JWT com autorização Global Method Security
5. ✅ Validação de dados com Spring Validation



**Para rodar a aplicação:**
```bash
docker-compose up -d
./mvnw spring-boot:run
```

**Para executar os testes:**
```bash
./mvnw test
```

**Para testar a API via Swagger:**
1. Execute a aplicação
2. Acesse: http://localhost:8080/swagger-ui.html

**Credenciais para teste:**
- Admin: admin@cinema.com / 123456
- Gerente: gerente@cinema.com / 123456
- User: user@cinema.com / 123456
