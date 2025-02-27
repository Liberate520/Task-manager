Task Management System
Описание

Это приложение для управления задачами с авторизацией через Keycloak. Приложение предоставляет интерфейс для управления задачами и пользователями, а также доступ к Swagger UI для документации API.
Структура

    Users: Пользователи могут быть добавлены при успешной авторизации.
    Tasks: Задачи могут быть созданы, изменены и просмотрены.
    Comments: Пользователи могут добавлять комментарии к задачам.

Технологии

    Spring Boot
    Keycloak (для аутентификации и авторизации)
    PostgreSQL (для хранения данных)
    Swagger UI (для отображения документации API)

Как запустить проект

Предварительные требования

Убедитесь, что у вас установлен Docker и Docker Compose.
Шаги для запуска

    запустите файл docker/start.sh

Этот скрипт поднимет все необходимые контейнеры, включая Keycloak, PostgreSql и Redis

    запустите файл main метод в TaskManagementSystemApplication


Приложение будет доступно по адресу:

    Backend: http://localhost:8081
    Keycloak: http://localhost:8080
    Swagger UI: http://localhost:8081/swagger-ui.html

Для доступа к Keycloak используйте:

    Админ:
        Логин: admin@example.com
        Пароль: admin
        Роль: ADMIN
    Пользователь:
        Логин: user@example.com
        Пароль: user
        Роль: USER

Создание пользователей

Создание аккаунтов необходимо выполнять в Keycloak. При обращении на любой эндпоинт приложения и успешной авторизации, данные нового пользователя автоматически попадут на сервер и будут сохранены в базе данных. После этого пользователя можно будет увидеть в общем списке пользователей.
API

Документация для API доступна через Swagger UI по адресу:
Swagger UI
Пример запросов

    GET /tasks: Получение всех задач.
    POST /tasks: Создание новой задачи.
    GET /users: Получение списка всех пользователей.

Локальная настройка Keycloak

    Перейдите в административный интерфейс Keycloak: http://localhost:8080/admin/.
    Логин в Keycloak: admin (пароль admin).
    Создайте или отредактируйте пользователей в разделе Users.
    Убедитесь, что для пользователя admin@example.com назначена роль ADMIN, а для пользователя user@example.com — роль USER.

Примечания

    База данных и приложения настроены для работы с PostgreSQL.
    Если необходимо внести изменения в настройки Keycloak или структуру базы данных, воспользуйтесь административной панелью Keycloak.