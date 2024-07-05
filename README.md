Описание проекта с REST API для изучения Spring Boot 3 и Spring Data JPA
Название проекта: Bookstore API
Описание:
Проект "Bookstore API" представляет собой RESTful веб-приложение, разработанное с использованием Spring Boot 3 и Spring Data JPA. Этот проект предназначен для демонстрации и изучения основ создания RESTful сервисов, работы с базами данных и применения лучших практик разработки с использованием Spring Boot и Spring Data JPA.

Цели проекта:
Изучить основные концепции и архитектуру Spring Boot 3.
Понять, как создавать и настраивать RESTful API.
Научиться работать с базами данных с помощью Spring Data JPA.
Реализовать CRUD операции (Create, Read, Update, Delete) для сущностей.
Освоить обработку ошибок и управление транзакциями.
Понять, как тестировать REST API с использованием JUnit и MockMVC.
Функциональные требования:
Управление книгами (Books):
Создание новой книги.
Получение списка всех книг.
Получение информации о книге по ID.
Обновление информации о книге.
Удаление книги.
Управление авторами (Authors):
Создание нового автора.
Получение списка всех авторов.
Получение информации об авторе по ID.
Обновление информации об авторе.
Удаление автора.
Технологический стек:
Backend: Java, Spring Boot 3, Spring Data JPA
База данных: H2 (встроенная база данных для разработки), PostgreSQL (для производства)
Сборка и управление зависимостями: Maven/Gradle
Структура проекта:
Controller: Обрабатывает HTTP-запросы и возвращает ответы.
Service: Содержит бизнес-логику приложения.
Repository: Взаимодействует с базой данных.
Entity: Определяет структуру таблиц базы данных.
DTO (Data Transfer Object): Используется для передачи данных между слоями приложения.
Exception Handling: Управление ошибками и их обработка.
Security: Конфигурация безопасности и аутентификация


Поля сущности Book
id (Long): Уникальный идентификатор книги.
title (String): Название книги.
isbn (String): Международный стандартный книжный номер (ISBN).
publicationDate (LocalDate): Дата публикации книги.
author (Author): Автор книги (связь с сущностью Author).
price (BigDecimal): Цена книги.
genre (String): Жанр книги.
description (String): Описание книги.

Поля сущности Author
id (Long): Уникальный идентификатор автора.
firstName (String): Имя автора.
lastName (String): Фамилия автора.
birthdate (LocalDate): Дата рождения автора.
biography (String): Биография автора.
books (List<Book>): Список книг, написанных автором (связь с сущностью Book).
