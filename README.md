# ScreenMatch API 🎬

![Java CI with Maven](https://img.shields.io/github/actions/workflow/status/joaogabriel43/ScreenMatch/maven.yml?branch=main&logo=github&style=for-the-badge)
![Codecov](https://img.shields.io/codecov/c/github/joaogabriel43/ScreenMatch?style=for-the-badge&logo=codecov&token=SEU_TOKEN_CODECOV)
![License: MIT](https://img.shields.io/github/license/joaogabriel43/ScreenMatch?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.1-6DB33F?style=for-the-badge&logo=spring)

> API REST para gerenciamento de séries e episódios, construída com Java e Spring Boot. O projeto consome a API do OMDb para dados e utiliza o ChatGPT para traduzir sinopses.

---

### Tabela de Conteúdos
* [Status do Projeto](#status-do-projeto)
* [Features](#features)
* [Arquitetura do Projeto](#arquitetura-do-projeto)
* [Documentação da API](#documentação-da-api)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Configuração do Ambiente](#configuração-do-ambiente)
* [Como Executar](#como-executar)
* [Roadmap](#roadmap)
* [Autor](#autor)

---

### Status do Projeto
<p align="center">
  <img src="https://img.shields.io/badge/Status-Concluído-brightgreen?style=for-the-badge" alt="Status do Projeto: Concluído"/>
</p>

---

### Features

- ✅ **API REST Completa:** Endpoints para todas as operações de CRUD (Create, Read, Update, Delete) de séries e episódios.
- ✅ **Interface de Linha de Comando (CLI):** Um menu interativo no console para interagir com a base de dados.
- ✅ **Integração com OMDb API:** Busca de dados de séries e temporadas de uma fonte externa.
- ✅ **Integração com OpenAI (ChatGPT):** Tradução automática de sinopses de séries para o português.
- ✅ **Consultas Avançadas:** Endpoints para buscar top 5 séries por avaliação, lançamentos recentes, e mais.
- ✅ **Persistência de Dados:** Uso de Spring Data JPA para salvar e gerenciar os dados em um banco PostgreSQL.

---

### Arquitetura do Projeto

A aplicação foi desenvolvida seguindo os princípios de uma arquitetura limpa e em camadas para garantir a separação de responsabilidades (SoC), alta coesão e baixo acoplamento.

```
[Cliente (CLI / HTTP Request)]
        ↓
[Controller Layer] (APIs REST, DTOs)
        ↓
[Service Layer] (Lógica de Negócio, Orquestração)
        ↓
[Repository Layer] (Abstração de Acesso a Dados - Spring Data JPA)
        ↓
[Database] (PostgreSQL)
```
- **Controller:** Responsável por expor os endpoints da API REST, receber requisições, validar dados de entrada e retornar respostas usando DTOs (Data Transfer Objects).
- **Service:** Contém a lógica de negócio principal da aplicação. Orquestra as operações, consome as APIs externas e interage com a camada de repositório.
- **Repository:** Interface que define os métodos de acesso ao banco de dados, utilizando o poder do Spring Data JPA.
- **Model:** Entidades JPA que representam as tabelas do banco de dados (`Serie`, `Episodio`).

---

### Documentação da API

Abaixo estão os principais endpoints disponíveis na API:

| Método HTTP | Endpoint | Descrição |
|---|---|---|
| `GET` | `/series` | Retorna a lista de todas as séries cadastradas. |
| `GET` | `/series/{id}` | Busca uma série específica pelo seu ID. |
| `GET` | `/series/top5` | Retorna as 5 séries com melhor avaliação. |
| `GET` | `/series/lancamentos` | Retorna os 5 últimos lançamentos. |
| `GET` | `/series/categoria/{nomeGenero}` | Busca séries por uma determinada categoria/gênero. |
| `GET` | `/series/{id}/temporadas/todas`| Retorna todas as temporadas de uma série. |
| `GET` | `/series/{id}/temporadas/{numero}`| Retorna os episódios de uma temporada específica. |

---

### Tecnologias Utilizadas

| Tecnologia | Descrição |
|---|---|
| **Java 17** | Versão LTS da linguagem. |
| **Spring Boot** | Framework principal para auto-configuração e injeção de dependências. |
| **Spring Data JPA** | Abstração da camada de persistência de dados. |
| **PostgreSQL**| Banco de dados relacional utilizado para persistência. |
| **Maven** | Gerenciamento de dependências e build do projeto. |
| **Jackson** | Biblioteca para serialização/desserialização de JSON. |
| **OpenAI API** | Utilizada para a funcionalidade de tradução de sinopses. |

---

### Configuração do Ambiente

Para executar o projeto, é necessário configurar as seguintes variáveis de ambiente. Nunca salve credenciais diretamente no código ou no arquivo `application.properties`.

1.  **Banco de Dados:**
    - `DB_URL`: A URL de conexão JDBC para o seu banco de dados.
    - `DB_USERNAME`: O nome de usuário do banco.
    - `DB_PASSWORD`: A senha do usuário do banco.

2.  **OpenAI API:**
    - `OPENAI_API_KEY`: Sua chave de API da OpenAI para a funcionalidade de tradução.

**Exemplo de configuração no `application.properties`:**
```properties
# -- Database Configuration --
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# -- OpenAI API Configuration --
openai.api.key=${OPENAI_API_KEY}
```

---

### Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/joaogabriel43/ScreenMatch.git](https://github.com/joaogabriel43/ScreenMatch.git)
    cd ScreenMatch
    ```
2.  **Configure as variáveis de ambiente** listadas na seção anterior.

3.  **Execute o projeto** via Maven Wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```
4.  A aplicação será iniciada na porta `8080`. Você pode interagir com a API usando ferramentas como Postman ou Insomnia, ou observar a execução do menu CLI no seu console.

---

### Roadmap

- [ ] **Implementar Testes:** Adicionar testes unitários para a camada de Serviço (com Mockito) e testes de integração para a camada de Controller (com `@SpringBootTest`).
- [ ] **Adicionar Endpoints de Escrita:** Implementar os métodos `POST`, `PUT` e `DELETE` para um gerenciamento completo das séries via API.
- [ ] **Segurança com Spring Security:** Adicionar autenticação e autorização aos endpoints.
- [ ] **Containerização com Docker:** Criar um `Dockerfile` e `docker-compose.yml` para facilitar o setup do ambiente de desenvolvimento.

---

### Autor

- **João Gabriel** - [GitHub](https://github.com/joaogabriel43)
