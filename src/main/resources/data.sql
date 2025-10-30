-- Inserir perfis
INSERT INTO perfis (nome) VALUES ('ROLE_ADMIN');
INSERT INTO perfis (nome) VALUES ('ROLE_USER');
INSERT INTO perfis (nome) VALUES ('ROLE_GERENTE');

-- Inserir usuários (senha: 123456 criptografada com BCrypt)
-- admin@cinema.com - senha: 123456
INSERT INTO usuarios (email, senha, ativo) VALUES ('admin@cinema.com', '$2a$10$XPTnPdVzXy.kJrRzEZQbPuOr4V9oZXr5S1qJQZgN7U/PZQ8W6ZYAW', 1);

-- user@cinema.com - senha: 123456
INSERT INTO usuarios (email, senha, ativo) VALUES ('user@cinema.com', '$2a$10$XPTnPdVzXy.kJrRzEZQbPuOr4V9oZXr5S1qJQZgN7U/PZQ8W6ZYAW', 1);

-- gerente@cinema.com - senha: 123456
INSERT INTO usuarios (email, senha, ativo) VALUES ('gerente@cinema.com', '$2a$10$XPTnPdVzXy.kJrRzEZQbPuOr4V9oZXr5S1qJQZgN7U/PZQ8W6ZYAW', 1);

-- Associar perfis aos usuários
-- admin tem perfil ADMIN
INSERT INTO usuarios_perfis (usuario_id, perfil_id) VALUES (1, 1);

-- user tem perfil USER
INSERT INTO usuarios_perfis (usuario_id, perfil_id) VALUES (2, 2);

-- gerente tem perfil GERENTE
INSERT INTO usuarios_perfis (usuario_id, perfil_id) VALUES (3, 3);

-- Inserir alguns filmes de exemplo para testes
INSERT INTO filmes (titulo, duracao) VALUES ('Matrix', '02:16:00');
INSERT INTO filmes (titulo, duracao) VALUES ('O Senhor dos Anéis', '03:48:00');
INSERT INTO filmes (titulo, duracao) VALUES ('Interestelar', '02:49:00');
INSERT INTO filmes (titulo, duracao) VALUES ('A Origem', '02:28:00');
INSERT INTO filmes (titulo, duracao) VALUES ('Vingadores Ultimato', '03:01:00');
