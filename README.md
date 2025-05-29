# gerenciador-tarefas-projeto-final

## OBS: 
- Os Endpoints disponíveis estão no controller.
- Devi ao fato de só o Adm poder criar usuários, foi disponibilizado no Application uma impressão no console onde gera uma senha de acesso do admin para que ele possa criar outros usuários. Por isso, não foi colocado na tela a parte de registro;

## 📌 Objetivo

Desenvolver uma plataforma completa para facilitar o gerenciamento de tarefas em equipes, com:

- Registro e login de usuários com segurança;
- Controle de permissões (usuário comum e administrador);
- Interface intuitiva para criação, edição, filtragem e visualização de tarefas;

  ## 🚀 Tecnologias Utilizadas

### Backend
- **Java + Spring Boot**;
- **JPA + Hibernate** – ORM para persistência de dados;
- **PostgreSQL** – Banco de dados relacional;
- **JWT + BCrypt** – Autenticação segura com hash de senhas

  ### Frontend
- **React** – Biblioteca para construção de interfaces;
- **TypeScript** – Tipagem estática;
- **Material UI (MUI)** – Componentes visuais modernos.

  ##  Funcionalidades do Backend

-  **Autenticação JWT**
  - Registro, login e logout;
  - Hash de senha com BCrypt;
  - Tokens de acesso e refresh.

- **CRUD de Tarefas**
  - Campos: título, descrição, status, responsável, data de entrega;
  - Usuários comuns visualizam apenas suas tarefas;
  - Administradores têm acesso a todas as tarefas.

-  **CRUD de Usuários (admin only)**
  - Cadastro, edição, exclusão e listagem de usuários.

-  **Filtros avançados**
  - Filtragem de tarefas por status, data e usuário responsável.
