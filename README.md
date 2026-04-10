# Simple CRM

Упрощенная CRM-система для управления информацией о продавцах и их транзакциях.

---

## Функциональность

### Продавцы

- Получение продавца по id
- Получение всех продавцов
- Создание продавца
- Обновление продавца
- Удаление продавца

### Транзакции

- Получение транзакции по id
- Получение всех транзакции, в том числе для конкретного проавца по id
- Создание транзакции
- Обновление транзакции
- Удаление транзакции

### Аналитика

#### Топ-продавец

Определяется как продавец с максимальной суммой транзакций за период:
- текущий день
- текущий месяц
- текущий квартал
- текущий год

#### Продавцы с оборотом ниже заданного
- Выбираются продавцы, у которых сумма транзакций меньше заданной
- Используется диапазон дат

---

## Инструкции по сборке и запуску

API и база данных упакованы в Docker контейнеры, для запуска используется docker compose<br>
`docker-compose up --build`

## Примеры использования API

### Продавцы

#### Создать продавца
`POST /api/sellers
Content-Type: application/json
{
  "name": "John Doe",
  "contactInfo": "john@mail.com"
}`

#### Получить всех продавцов
`GET /api/sellers`

#### Получить продавца по ID
`GET /api/sellers/{id}`

#### Обновить продавца
`PUT /api/sellers/{id}
Content-Type: application/json
{
  "name": "Updated Name"
}`

#### Удалить продавца
`DELETE /api/sellers/{id}`

### Транзакции

#### Создать транзакцию
`POST /api/transactions
Content-Type: application/json
{
  "sellerId": 1,
  "amount": 500,
  "paymentType": "CARD",
  "transactionDate": "2026-03-01T10:00:00"
}`

#### Получить все транзакции
`GET /api/transactions`

#### Получить все транзакции для конкретного продавца
`GET /api/transactions?sellerId=1`

#### Получить транзакцию по ID
`GET /api/transactions/{id}`

#### Обновить транзакцию
`PUT /api/transactions/{id}
Content-Type: application/json
{
  "paymentType": "Updated Payment Type"
}`

#### Удалить транзакцию
`DELETE /api/transactions/{id}`

---

## Описание необходимых зависимостей

- `implementation("org.springframework.boot:spring-boot-starter")`
  <br>база Spring Boot

- `implementation("org.springframework.boot:spring-boot-starter-web")`
  <br>веб-функциональность Spring Boot

- `implementation("org.springframework.boot:spring-boot-starter-validation")`
  <br>валидация полей для dto

- `implementation("org.springframework.boot:spring-boot-starter-data-jpa")`
  <br>работа с базой данных

- `implementation("org.hibernate.orm:hibernate-envers")`
  <br>аудит изменения сущностей

- `implementation("org.projectlombok:lombok")`
    - `annotationProcessor("org.projectlombok:lombok")`
      <br>генерация сеттеров, геттеров и конструкторов

- `runtimeOnly("org.postgresql:postgresql")`
  <br>драйвер PostgreSQL

- `runtimeOnly("com.h2database:h2")`
  <br>inmemory база данных для тестирования