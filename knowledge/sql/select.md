# SQL: SELECT

`SELECT` — основная команда для получения данных из базы.

## Синтаксис

```sql
SELECT column1, column2
FROM table_name
WHERE condition
ORDER BY column1 ASC;
```

## Примеры

### Выбор всех записей

```sql
SELECT * FROM users;
```

### Фильтрация

```sql
SELECT * FROM users WHERE age > 18;
```

### Сортировка

```sql
SELECT * FROM products ORDER BY price DESC;
```

### Группировка

```sql
SELECT status, COUNT(*)
FROM orders
GROUP BY status;
```

## Полезные операторы

- `LIKE` — поиск по шаблону
- `IN` — проверка вхождения в список
- `BETWEEN` — диапазон значений
- `JOIN` — объединение таблиц
