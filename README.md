# 📋 Gerenciador de Tarefas – Projeto Final

Aplicação Full Stack para gerenciamento de tarefas entre membros de uma equipe, com autenticação via JWT, controle de permissões e uma interface intuitiva.

---

## 🧭 Como rodar o projeto

1. Acesse o diretório onde está o `docker-compose.yml` e execute:

```bash
docker-compose down -v
docker-compose up --build   # ou use: docker-compose up -d
Verifique os containers em execução:

bash
Copiar
Editar
docker ps
Acesse o banco de dados PostgreSQL:

bash
Copiar
Editar
docker exec -it postgres_db psql -U postgres -d springdb
🔐 Usuário Admin (gerado automaticamente)
Email: admin@email.com

Senha: 123456

Este usuário tem permissão para cadastrar novos usuários (comuns ou administradores).

⚠️ Ao subir o container, é feito um insert automático no banco com um usuário admin.
Também é impresso no console o hash da senha gerada, caso precise alterar.

🎯 Objetivo
Desenvolver uma plataforma para facilitar o gerenciamento de tarefas com:

Registro e login com autenticação segura;

Permissões diferenciadas (Usuário e Administrador);

Interface amigável para controle de tarefas.

🚀 Tecnologias Utilizadas
🔧 Backend
Java + Spring Boot

JPA + Hibernate – ORM para persistência

PostgreSQL – Banco de dados relacional

JWT + BCrypt – Autenticação e segurança

🎨 Frontend
React

TypeScript

Material UI (MUI) – Componentes modernos e responsivos

⚙️ Funcionalidades do Backend
🔐 Autenticação JWT
Registro, login e logout

Hash de senhas com BCrypt

Tokens de acesso e refresh

🗂️ CRUD de Tarefas
Campos: título, descrição, status, responsável, data de entrega

Usuários comuns veem apenas suas tarefas

Admins veem todas as tarefas

👥 CRUD de Usuários (Admin apenas)
Cadastro, edição, exclusão e listagem

🔍 Filtros avançados
Filtrar tarefas por: status, data e responsável

📎 Observações importantes
Os endpoints estão documentados nos controllers;

Como apenas admins podem cadastrar usuários, não foi implementada uma tela de registro no frontend;

A criação do usuário admin no banco é feita automaticamente via script no container Docker.
