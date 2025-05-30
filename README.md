# Desafio Logística
Este projeto simula um sistema de logística que integra dados de usuários, pedidos e produtos a partir de um arquivo legado desnormalizado e expõe esses dados de forma eficiente via API REST. A aplicação foi desenvolvida para receber, processar e disponibilizar informações consolidadas, permitindo que sistemas clientes façam consultas dinâmicas por ID de pedido e intervalo de datas de compra.
## Linguagem Utilizada:
A escolha do Java se deu por ser uma linguagem robusta, amplamente utilizada em ambientes corporativos e com excelente suporte para desenvolvimento backend de APIs REST. Java oferece:
- Forte tipagem e segurança em tempo de compilação.
- Ecossistema rico com bibliotecas maduras.
- Suporte consolidado para frameworks como Spring Boot.
- Alta escalabilidade e performance em aplicações de missão crítica.

## Arquitetura:
O projeto foi estruturado seguindo o padrão MVC, por ser uma arquitetura amplamente reconhecida e adotada para aplicações web. Ela promove uma separação clara de responsabilidades:
- Model: entidades JPA (UserEntity, OrderEntity, ProductEntity)
- Controller: expõe endpoints REST
- Service: lógica de negócio (ingestão, normalização, filtros)
- Repository: Spring Data JPA para persistência
- DTOs: desacoplamento entre domínio e API (UserDto, OrderDto, ProductDto)

## Estrutura de Pacotes:
```bash
com.desafio.logistica
├─ controller
├─ dto
├─ exceptionhandler
├─ exeption
├─ model
├─ repository
├─ service
└─ utils
```
## Tecnologias Utilizadas:
- Java 17 - Linguagem de programação principal.
- Spring Boot 3.4.5 - Framework para acelerar o desenvolvimento da API REST.
- Spring Data JPA - Acesso e persistência de dados com base em repositórios.
- Hibernate - Implementação do JPA para ORM.
- MySQL 8 - Banco de dados relacional utilizado no armazenamento.
- Maven - Gerenciador de dependências e build.
- Jakarta Persistence API - API padrão para persistência de dados.

# Como Executar:
Siga os passos abaixo para executar a aplicação localmente:

Pré-requisitos
- Java 17+
- Maven 3.8+
- MySQL 8 ou superior (ou outro banco compatível configurado no application.yml)
- Docker (opcional, se preferir rodar via container)
1. Clone o repositório
```bash
git clone git@github.com:francastelobranco/desafio.git
```
2. No application.yml, os dados de acesso ao banco já estão definidos:
```bash
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/logistica?createDatabaseIfNotExist=true&serverTimezone=UTC
    username: root
    password: 123456
```
O parâmetro createDatabaseIfNotExist=true permite que o banco seja criado automaticamente caso ainda não exista.

3. Rodar a aplicação via Maven (modo desenvolvimento)
```bash
./mvnw spring-boot:run
```

## Executando com Docker Compose
O projeto já inclui um Dockerfile e um docker-compose.yml com:
- API Logística rodando em http://localhost:8080
- MySQL (porta 3307)
- phpMyAdmin acessível em http://localhost:5000

### Para rodar tudo com Docker:
```bash
docker-compose up --build
```
### Acessos:
- Swagger UI: http://localhost:8080/swagger-ui.html
- phpMyAdmin: http://localhost:5000
- Servidor: mysql
- Usuário: root
- Senha: 123456

## Testes
Para executar os testes unitários:
```bash
mvn clean test
```
Para executar os testes com coverage e gerar relatório HTML:
```bash
mvn clean test jacoco:report
```
Relatório gerado em `target/site/jacoco/index.html`.


## API
- **URL:** `/api/v1/user/upload`
- **Método:** `POST`
- **Descrição:** Recebe um `.txt` com dados desnormalizados e persiste usuários, pedidos e produtos.

#### Resposta:

| Status | Endpoint              | Descrição                                      | 
|--------|-----------------------|------------------------------------------------| 
| 204    | NO CONTENT            | Arquivo processado com sucesso e dados salvos. |
| 400    | BAD REQUEST           | Formato inválido ou erro ao ler o arquivo.     |
| 413    | PAYLOAD TOO LARGE     | Tamanho do arquivo excede o limite.            |

##### Buscar Usuários com Filtros:
- **URL:** `/api/v1/user/find`
- **Método:** `GET`
- **Descrição:** Retorna a lista de usuários com seus pedidos e produtos, permitindo filtragem opcional por `orderId`, `startDate` e `endDate`.
#### Parâmetros de Query (opcionais):
- `orderId`: filtra pelo ID do pedido (Integer).
- `startDate`: filtra pedidos com data maior ou igual a essa (`yyyy-MM-dd`).
- `endDate`: filtra pedidos com data menor ou igual a essa (`yyyy-MM-dd`).

#### Exemplo de requisição:
```bash
# GET /user/find?orderId=123&startDate=2021-01-01&endDate=2021-12-31
```

| Status | Endpoint              | Descrição                                      | 
|--------|-----------------------|------------------------------------------------| 
| 200    | OK                    | Retorna lista de `UserDto`                     |
| 400    | BAD REQUEST           | Intervalo de datas inválido: `startDate` não pode ser posterior a `endDate`.|
| 404    | NOT FOUND             | Pedido não encontrado para o filtro informado. |
| 500    | INTERNAL SERVER ERROR | Erro interno.                                  |

> ⚠️ Observação: Caso ambos os parâmetros `startDate` e `endDate` sejam informados, `startDate` não pode ser posterior a `endDate`.

#### Resposta de exemplo (JSON)
```json
[
  {
    "userId": 11,
    "name": "Savannah Hamill",
    "orders": [
      {
        "orderId": 123,
        "total": 510.23,
        "date": "2021-06-09",
        "products": [
          {
            "productId": 2,
            "value": 798.03
          }
        ]
      }
    ]
  }
]
```
#### Formato do Arquivo de Dados:
Cada linha deve obedecer ao layout de colunas fixas:

| Campo      | Descrição                                                                 |
|------------|---------------------------------------------------------------------------|
| `userId`   | ID do usuário (10 caracteres, preenchido com zeros à esquerda).           |
| `userName` | Nome do usuário (45 caracteres, preenchido com espaços).                  |
| `orderId`  | ID do pedido (10 caracteres).                                             |
| `productId`| ID do produto (10 caracteres).                                            |
| `value`    | Valor do produto (12 caracteres, ponto decimal implícito).                |
| `date`     | Data da compra (8 caracteres no formato `yyyyMMdd`).                      |

#### Exemplo de linha:
```bash
0000000015                                   Bonny Koss00000001530000000004        80.820210701
```
