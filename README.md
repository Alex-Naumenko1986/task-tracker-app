# Система управления задачами

### Стек проекта:

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* Maven
* Lombok
* Mapstruct
* Query DSL

### Инструкция для локального запуска проекта:

1. Для запуска проекта на компьютере должен быть установлен Docker: https://docs.docker.com/engine/install/
2. Клонируйте репозиторий: git clone https://github.com/Alex-Naumenko1986/task-tracker-app.git
3. Перейдите в папку с проектом
4. Выполните команду mvn clean install для сборки jar-файла
5. Запустите приложение: docker-compose up -d
6. Тестирование приложения реализовано при помощи коллекции Postman. После запуска приложения, импортируйте и запустите
   коллекцию в Postman. Коллекция находится в папке [/postman](/postman)
7. Документация доступна в папку [/documentation](/documentation). Ее можно открыть в Swagger Editor
8. Swagger UI доступен после запуска приложения по ссылке: http://localhost:8080/swagger-ui
