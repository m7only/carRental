# Rental Car

Пет проет по созданию сервиса проката автомобилей. Разрабатывается для практики и изучения новых технологий.

### Технологии

* Spring Boot, Spring Web, Spring Data, Spring Security
* PostgresSQL, Hibernate, Liquibase
* Thymeleaf, HTML, Bootstrap
* JavaDoc

### Что реализовано
#### Технически:
* CRUD сущностей заказ, авто, фотографии
* CRUD пользователей с регистрацией, аутентификацией, авторизацией и ролями
* REST отправка фото
* Связи между сущностями
* Миграция Liquibase

#### Функционально:

В текущем состоянии проект рабочий и запускается. Пользователь может зарегистрироваться, в личном кабинете внести и
изменить персональные данные, загрузить свои документы для валидации менеджером. После валидации пользователь
может оформить заказ на автомобили, которые свободны в желаемое время время. Может отменить заказ.

Реализована админ панель, доступ в которую имеют пользователи только с определенными ролями. В админ панели реализованы
CRUD для пользователей, заказов и автомобилей. Так же на разные операции требуются соответствующие роли у пользователя.

### Запуск

Пользователь с админ-правами:
* логин q
* пароль q

Запуск с помощью docker: `docker-compose -p car-rent up`. Файл docker-compose.yml:
```
version: '4'
services:
  client-backend:
    image: m7only/rent:0.0.1
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    depends_on:
      - service-db
    environment:
      - SERVER_PORT= 8080
      - SPRING_DATASOURCE_URL=jdbc:postgresql://service-db/rent

  service-db:
    image: postgres:14.7-alpine
    environment:
      POSTGRES_DB: rent
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: root
    ports:
      - "15432:5432"
    volumes:
      - db-data:/var/lib/postgresql/data
    restart: unless-stopped

volumes:
  db-data:

```

### Дальнейшие планы
#### Технически:
* Тесты JUnit, Mockito
* Более глубокая валидация
* Контроль исключений, логирование

#### Функционально
* Расширение функионала сервиса: сортировки, фильтры
* Измение дизайн части фронта


2023