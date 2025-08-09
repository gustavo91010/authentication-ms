# Authentication-MS  
Sistema de autenticação centralizado para múltiplas aplicações, desenvolvido com **Spring Boot** e suporte a autenticação via **JWT** e **OAuth2** (Google e GitHub).  

## 📌 Descrição  
O **Authentication-MS** é um microsserviço que gerencia autenticação e registro de usuários para diferentes aplicações clientes.  
- Cada aplicação é registrada no sistema com suas próprias chaves (`clientId` e `secretId`).  
- Um mesmo e-mail pode ser usado em aplicações diferentes sem conflitos.  
- Registro e integração de novos usuários via **SQS** (AWS) com Lambda para comunicação automática entre sistemas.  
- Autenticação JWT e login social via OAuth2.  
- Documentação automática via **Swagger**.  
- Monitoramento e métricas via **Spring Boot Actuator**.  

## 🚀 Principais Funcionalidades  
- Registro de aplicações e usuários vinculados.  
- Autenticação via **JWT**.  
- Login via **Google** e **GitHub**.  
- Integração com **AWS SQS** para disparo de eventos de registro.  
- Documentação com **Swagger UI**.  
- Endpoints de monitoramento e saúde com **Actuator**.  

## 🛠 Tecnologias Utilizadas  
- **Java 8**  
- **Spring Boot 2.5.6** (Web, Security, Data JPA, Validation)  
- **PostgreSQL**  
- **JWT** (`io.jsonwebtoken`)  
- **Swagger** (`springfox` e `springdoc-openapi`)  
- **Flyway** para migração de banco  
- **AWS SQS** (`spring-cloud-aws`)  
- **Spring Boot Actuator**  
- **Thymeleaf** (para páginas e-mails ou autenticação em dois fatores)  
- **OAuth2** (Google, GitHub)  

## 📦 Estrutura do Projeto  
src/main/java/com/ajudaqui/authenticationms
│
├── config/ # Configurações de segurança, Swagger, AWS e WebMvc

├── controller/ # Endpoints REST para autenticação, aplicações e usuários

├── dto/ # Objetos de transferência de dados
├── entity/ # Entidades JPA (Applications, Users, Roles, etc.)
├── exception/ # Exceções personalizadas e handlers globais
├── repository/ # Interfaces de acesso ao banco
├── request/ # Modelos para requisições recebidas
├── response/ # Modelos para respostas enviadas
├── service/ # Regras de negócio e integrações
└── utils/ # Utilitários e enums

## ⚙ Configuração e Execução  

### Pré-requisitos  
- **Java 8+**  
- **Maven**  
- **Docker** (para PostgreSQL)  
- Credenciais AWS configuradas para acesso ao SQS  

### Rodando localmente  
```bash
# Clonar o repositório
git clone https://github.com/seu-repositorio/authentication-ms.git
cd authentication-ms

# Subir banco com Docker
docker-compose up -d

# Rodar aplicação
./mvnw spring-boot:run
| Método | Endpoint           | Descrição                  |
| ------ | ------------------ | -------------------------- |
| POST   | `/auth/login`      | Autenticação JWT           |
| GET    | `/auth/google`     | Login via Google           |
| GET    | `/auth/github`     | Login via GitHub           |
| POST   | `/applications`    | Registro de aplicação      |
| POST   | `/users`           | Registro de usuário        |
| GET    | `/actuator/health` | Verificar saúde do serviço |
🖥 Swagger UI

A documentação da API pode ser acessada em:

http://localhost:8080/swagger-ui.html

📊 Actuator

Endpoints de monitoramento:

http://localhost:8080/actuator

📩 Fluxo de Registro via SQS

    O cliente registra uma aplicação informando URL de registro.

    Ao criar um novo usuário, os dados são enviados ao SQS.

    Uma AWS Lambda consome a mensagem e chama o endpoint da aplicação registrada para finalizar o cadastro.
