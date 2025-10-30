 Como Executar

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
