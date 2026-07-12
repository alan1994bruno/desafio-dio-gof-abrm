# API de Gerenciamento de Clientes - Spring Boot & Design Patterns

Este projeto é uma API RESTful desenvolvida em Java com Spring Boot, focada no gerenciamento de clientes e na integração com a API pública ViaCEP para autocompletar endereços. O foco principal desta aplicação é a implementação prática de **Design Patterns** (Padrões de Projeto) do GoF (Gang of Four) aplicados ao ecossistema do Spring.

## 🛠️ Tecnologias e Bibliotecas Utilizadas

- **Java**
- **Spring Boot** (Web, Data JPA)
- **Spring Cloud OpenFeign:** Utilizado para criar o cliente HTTP declarativo que consome a API do ViaCEP.
- **Lombok:** Redução de código boilerplate (Getters, Setters, etc.) nas entidades.
- **Banco de Dados & Infraestrutura:** PostgreSQL e Adminer via Docker.
- **Swagger (OpenAPI):** Documentação interativa da API.

## 🧠 Design Patterns Aplicados

O desenvolvimento do serviço de clientes (`ClienteService`) baseou-se diretamente nos seguintes padrões:

- **Singleton:** Garantido nativamente pelo framework Spring Boot. Os componentes (`@Service`, `@RestController`, `@Repository`) são instanciados uma única vez e injetados via `@Autowired`.
- **Strategy:** Implementado através da interface `IClienteService`. Isso permite que múltiplas implementações de serviços de cliente possam coexistir, abstraindo o contrato da regra de negócio.
- **Facade:** Visto no método `salvarClienteComCep`. Ele constrói uma "fachada" que oculta a complexidade da integração entre o repositório de banco de dados e a chamada externa (ViaCEP), provendo uma interface de uso simples para o controller.

## ⚙️ Funcionalidades

- Listagem de todos os clientes.
- Busca de cliente específico pelo ID.
- Cadastro e atualização de clientes, exigindo apenas o Nome e o CEP.
- Integração automática para preenchimento de logradouro, bairro, localidade e UF baseando-se no CEP informado via Feign Client.
- Exclusão de clientes por ID.
- Tratamento de exceções customizado (`ResourceNotFoundException`) para requisições de IDs inexistentes (retornando HTTP 404).

## 🐳 Ambiente de Desenvolvimento (Docker)

O projeto conta com um arquivo `docker-compose.yml` pré-configurado para subir rapidamente uma instância do banco de dados **PostgreSQL** e o **Adminer** (interface gráfica para gerenciar o banco).

Crie o arquivo `docker-compose.yml` na raiz do projeto com o seguinte conteúdo:

```yaml
version: "3.8"

services:
  db:
    image: postgres:15
    container_name: postgres_db
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: password
      POSTGRES_DB: gof
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  adminer:
    image: adminer
    container_name: adminer_ui
    ports:
      - "8081:8080"

volumes:
  postgres_data:
```

## 🚀 Como Executar

1. Clone o repositório.
2. Abra o terminal na raiz do projeto e suba os containers do banco de dados:

```bash
docker-compose up -d

```

3. Abra o projeto na sua IDE.
4. Execute a classe principal `GofApplication.java`.
5. A API estará disponível em `http://localhost:8080`.
6. Para acessar o banco de dados via interface gráfica (Adminer), acesse `http://localhost:8081` no seu navegador.

## 📄 Documentação da API (Swagger)

A API está totalmente documentada utilizando o Swagger (OpenAPI). Após iniciar a aplicação, você pode visualizar todos os endpoints, modelos de dados e testar as requisições diretamente pelo navegador acessando:

👉 **[http://localhost:8080/swagger-ui](https://www.google.com/search?q=http://localhost:8080/swagger-ui)**

## 📌 Endpoints da API

A URL base para os endpoints de clientes é `/clientes`.

| Método   | Rota             | Descrição                                           |
| -------- | ---------------- | --------------------------------------------------- |
| `GET`    | `/clientes`      | Retorna a lista de todos os clientes cadastrados.   |
| `GET`    | `/clientes/{id}` | Busca os detalhes de um cliente específico pelo ID. |
| `POST`   | `/clientes`      | Insere um novo cliente no sistema.                  |
| `PUT`    | `/clientes/{id}` | Atualiza os dados de um cliente existente.          |
| `DELETE` | `/clientes/{id}` | Remove um cliente da base de dados.                 |

### Formato do Payload

Para os métodos `POST` e `PUT`, a API espera receber um objeto JSON simplificado contendo apenas o nome e o CEP. O sistema se encarrega de preencher o restante das informações de endereço.

**Exemplo de Requisição (`POST /clientes`):**

```json
{
  "nome": "Bruno",
  "cep": "01001-000"
}
```

##### Nota: Pode enviar o cep com '-' ou sem '-'.

---

**Autor:** Álan Bruno
