# BookStore

Fullstack веб-приложение интернет-магазина книг с REST API, корзиной, заказами, поиском/фильтрацией и интеграцией с внешним API OpenLibrary.

## Описание

Интернет-магазин книг, в котором пользователь может просматривать каталог, искать и фильтровать книги, регистрироваться и авторизовываться, добавлять товары в корзину, оформлять заказы и просматривать историю покупок.

## Что реализовано

- Каталог книг с поиском по названию/автору и фильтрацией по жанру и диапазону цен (через `Specification` из Spring Data JPA — динамическое построение запроса только по указанным параметрам)
- Импорт книг из внешнего API OpenLibrary через `WebClient` с преобразованием в собственные DTO
- Корзина: добавление/удаление книг, изменение количества, автоматический расчёт стоимости
- Оформление заказов и просмотр истории покупок
- Регистрация и авторизация на Spring Security — хеширование паролей (BCrypt), сессионное управление, защищённый доступ к пользовательским данным
- Личный кабинет пользователя
- Frontend на чистом HTML/CSS/JS, взаимодействие с backend через Fetch API

## Технологии

**Backend:** Java, Spring Boot, Spring Web, Spring Data JPA, Hibernate, Spring Security, PostgreSQL, WebClient, BCrypt

**Frontend:** HTML5, CSS3, JavaScript, Fetch API

**External API:** OpenLibrary API

**Инфраструктура:** Docker (PostgreSQL)

## Структура проекта

```text
bookstore-project/
├── backend/      # Spring Boot приложение
├── frontend/     # HTML/CSS/JS
└── docker-compose.yml
```

## Запуск проекта

### Требования
- Java 21+ (или ваша версия — уточните)
- Docker и Docker Compose

### Шаги

1. Склонировать репозиторий:
```bash
git clone https://github.com/lukinadiana-creator/bookstore-project.git
cd bookstore-project
```

2. Поднять базу данных:
```bash
docker-compose up -d
```

3. Запустить backend:
```bash
cd backend
./mvnw spring-boot:run
```

4. Открыть `frontend/index.html` в браузере (например, через расширение Live Server в VS Code)