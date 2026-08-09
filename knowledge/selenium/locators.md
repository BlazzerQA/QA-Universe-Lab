# Локаторы в Selenium

Локаторы позволяют найти элементы на веб-странице.

## Типы локаторов

1. **By.id** — по идентификатору элемента.
2. **By.name** — по атрибуту `name`.
3. **By.className** — по CSS-классу.
4. **By.tagName** — по имени тега.
5. **By.linkText** — по полному тексту ссылки.
6. **By.partialLinkText** — по частичному тексту ссылки.
7. **By.xpath** — по XPath-выражению.
8. **By.cssSelector** — по CSS-селектору.

## Рекомендации

- Отдавайте предпочтение `id` и `name`.
- Избегайте абсолютного XPath.
- Используйте устойчивые атрибуты, например `data-testid`.

## Пример

```java
WebElement button = driver.findElement(By.id("submit"));
WebElement link = driver.findElement(By.cssSelector("a[href='/login']"));
```
