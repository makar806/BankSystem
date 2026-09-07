# BankSystem

Учебное банковское REST API на Java и Spring Boot. Проект моделирует работу пользователей, счетов и переводов, а также получает актуальные курсы валют через RabbitMQ.

## Возможности

- Создание и получение пользователей; фильтрация по полу и цвету волос
- Добавление и удаление друзей
- Создание счетов, просмотр баланса, пополнение и списание средств
- Переводы между счетами с комиссией: 0% между своими счетами, 3% между друзьями и 10% в остальных случаях
- История операций с фильтрацией по типу и счёту
- Просмотр баланса в RUB, USD или EUR по последнему полученному курсу
- Swagger UI для исследования API

## Архитектура

Проект разбит на Gradle-модули:

| Модуль | Назначение |
| --- | --- |
| `bank-model` | Доменные сущности JPA: пользователь, счёт и операция |
| `bank-data-access` | Слой доступа к данным на Hibernate |
| `bank-service` | Бизнес-логика переводов, операций и конвертации валют |
| `bank-app` | Основное Spring Boot-приложение с REST API и DTO |
| `rates-service` | Сервис, публикующий курсы USD и EUR в RabbitMQ |

`rates-service` регулярно публикует курсы валют в RabbitMQ. `bank-app` получает обновления, хранит последние значения в кэше и использует их при конвертации баланса.

## Стек

- Java 21
- Spring Boot 3
- Spring Web, Spring Data JPA, Spring AMQP
- Hibernate
- PostgreSQL 16
- RabbitMQ 3
- Gradle (multi-module)
- springdoc-openapi / Swagger UI
- Docker Compose

## Быстрый старт

### Требования

- JDK 21
- Docker и Docker Compose

### Запуск

```bash
# 1. Поднять PostgreSQL и RabbitMQ
docker compose up -d

# 2. Запустить сервис курсов валют
./gradlew :rates-service:bootRun

# 3. В отдельном терминале запустить API
./gradlew :bank-app:bootRun
```

После запуска:

- API: [http://localhost:8080](http://localhost:8080)
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- RabbitMQ Management: [http://localhost:15672](http://localhost:15672) - логин/пароль: `guest` / `guest`
- PostgreSQL: `localhost:5434`, база `bank_db`, пользователь `bank_user`

## Основные эндпоинты

| Группа | Эндпоинты |
| --- | --- |
| Пользователи | `GET /users`, `POST /users`, `GET /users/{id}` |
| Друзья | `GET /users/{id}/friends`, `POST /users/{id}/friends/{friendId}`, `DELETE /users/{id}/friends/{friendId}` |
| Счета | `GET /accounts`, `POST /accounts?userId={userId}`, `GET /accounts/{id}/balance` |
| Операции со счётом | `POST /accounts/{id}/deposit`, `POST /accounts/{id}/withdraw` |
| Переводы | `POST /transfer` |
| История | `GET /operations?type={type}&id={accountId}` |

Полный контракт запросов и ответов доступен в Swagger UI после запуска приложения.

## Примечание

Проект создан как учебный и демонстрирует работу со слоистой архитектурой, транзакциями, JPA/Hibernate, PostgreSQL и асинхронным обменом сообщениями через RabbitMQ.
