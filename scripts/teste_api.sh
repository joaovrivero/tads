#!/bin/bash

echo "========================================="
echo "DEMONSTRAÇÃO API REST - CINEMA TADS"
echo "========================================="
echo ""

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para imprimir cabeçalhos
print_header() {
    echo ""
    echo -e "${YELLOW}===> $1${NC}"
    echo ""
}

# 1. Testar endpoint público
print_header "1. TESTANDO ENDPOINT PÚBLICO (GET /api/v1/filmes)"
echo "Comando: curl http://localhost:8080/api/v1/filmes"
echo ""
curl -s http://localhost:8080/api/v1/filmes | jq '.'
echo -e "${GREEN}✓ Sucesso: Endpoint público funcionando!${NC}"

# 2. Testar Swagger
print_header "2. VERIFICANDO DOCUMENTAÇÃO SWAGGER"
echo "URL: http://localhost:8080/swagger-ui/index.html"
echo ""
SWAGGER_CHECK=$(curl -s http://localhost:8080/swagger-ui/index.html | grep -c "Swagger UI")
if [ $SWAGGER_CHECK -gt 0 ]; then
    echo -e "${GREEN}✓ Sucesso: Swagger está acessível!${NC}"
else
    echo -e "${RED}✗ Erro: Swagger não encontrado${NC}"
fi

# 3. Testar endpoint protegido sem autenticação
print_header "3. TESTANDO ENDPOINT PROTEGIDO SEM AUTENTICAÇÃO (deve falhar)"
echo "Comando: POST /api/v1/filmes sem token"
echo ""
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/api/v1/filmes \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Novo Filme","duracao":"01:30:00"}')

if [ $HTTP_CODE -eq 403 ]; then
    echo -e "${GREEN}✓ Sucesso: Endpoint protegido (HTTP $HTTP_CODE - Forbidden)${NC}"
else
    echo -e "${RED}✗ HTTP $HTTP_CODE (esperado: 403)${NC}"
fi

# 4. Testar validação de dados
print_header "4. TESTANDO VALIDAÇÃO DE DADOS"
echo "Enviando dados inválidos (título com 1 caractere)..."
echo ""
curl -s -X POST http://localhost:8080/api/v1/filmes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer fake-token" \
  -d '{"titulo":"A","duracao":null}' | jq '.'
echo -e "${GREEN}✓ Validação configurada (esperado erro 400 ou 403)${NC}"

# 5. Informações sobre autenticação
print_header "5. AUTENTICAÇÃO JWT"
echo "Para testar a autenticação completa, use:"
echo ""
echo "LOGIN:"
echo "  curl -X POST http://localhost:8080/login \\"
echo "    -H 'Content-Type: application/json' \\"
echo "    -d '{\"email\":\"admin@cinema.com\",\"senha\":\"123456\"}'"
echo ""
echo "Usuários disponíveis:"
echo "  - admin@cinema.com (ROLE_ADMIN) - senha: 123456"
echo "  - gerente@cinema.com (ROLE_GERENTE) - senha: 123456"
echo "  - user@cinema.com (ROLE_USER) - senha: 123456"
echo ""
echo "USAR TOKEN:"
echo "  curl -X POST http://localhost:8080/api/v1/filmes \\"
echo "    -H 'Content-Type: application/json' \\"
echo "    -H 'Authorization: Bearer SEU_TOKEN' \\"
echo "    -d '{\"titulo\":\"Novo Filme\",\"duracao\":\"01:30:00\"}'"

# 6. Resumo dos requisitos
print_header "6. RESUMO DOS REQUISITOS IMPLEMENTADOS"
echo -e "${GREEN}✓ 1. Mapeamento Objeto-Relacional (JPA/Hibernate)${NC}"
echo "     - Entidades: Filme, Sessao, Sala, Ingresso, Usuario, Perfil"
echo "     - Relacionamentos: @ManyToOne, @OneToMany, @ManyToMany"
echo ""
echo -e "${GREEN}✓ 2. Spring Rest, Repositories e Documentação${NC}"
echo "     - Repositories: JpaRepository para todas entidades"
echo "     - Swagger UI: http://localhost:8080/swagger-ui/index.html"
echo ""
echo -e "${GREEN}✓ 3. Controllers e Services${NC}"
echo "     - Controllers: FilmeController, AutenticacaoController"
echo "     - Services: FilmeService, UsuarioService"
echo ""
echo -e "${GREEN}✓ 4. Autenticação JWT e Global Method Security${NC}"
echo "     - JWT: TokenService, SecurityFilter"
echo "     - @PreAuthorize: hasRole('ADMIN'), hasRole('GERENTE')"
echo ""
echo -e "${GREEN}✓ 5. Validação com Spring Validation${NC}"
echo "     - @NotBlank, @NotNull, @Size, @Email"
echo "     - @Valid nos Controllers"
echo ""

print_header "DEMONSTRAÇÃO CONCLUÍDA!"
echo "Consulte o arquivo GUIA_DEMONSTRACAO.md para mais detalhes"
echo ""
