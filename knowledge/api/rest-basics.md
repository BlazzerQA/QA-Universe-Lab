# Основы REST API

REST (Representational State Transfer) — архитектурный стиль для проектирования API.

## HTTP-методы

| Метод | Действие |
|-------|----------|
| GET    | Получение ресурса |
| POST   | Создание ресурса |
| PUT    | Обновление ресурса |
| PATCH  | Частичное обновление |
| DELETE | Удаление ресурса |

## Коды ответа

- `200 OK` — успешный запрос
- `201 Created` — ресурс создан
- `400 Bad Request` — некорректный запрос
- `401 Unauthorized` — требуется авторизация
- `403 Forbidden` — доступ запрещён
- `404 Not Found` — ресурс не найден
- `500 Internal Server Error` — ошибка сервера

## Пример запроса

```http
GET /api/users HTTP/1.1
Host: example.com
```
