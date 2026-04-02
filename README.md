<p align="center">
  <img src="https://readme-typing-svg.herokuapp.com?font=JetBrains+Mono&size=24&duration=3000&pause=600&center=true&vCenter=true&width=600&lines=QA-Universe+Lab;Java+%7C+Spring+Boot+%7C+Selenide;API+%7C+UI+%7C+Mockito+Testing" alt="typing" />
</p>

<h1 align="center">🧪 QA-Universe Lab</h1>

<p align="center">
  <b>Interactive Java & Spring Boot Interview Sandbox</b><br>
  A hands-on project to master Middle-level AQA topics: Java Collections, Mockito, REST API, and UI testing.<br>
  Built with <b>TDD</b> and <b>Clean Code</b> principles.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white">
  <img src="https://img.shields.io/badge/Selenide-000000?style=for-the-badge&logo=selenium&logoColor=white">
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
  <img src="https://img.shields.io/badge/Mockito-3E9B6F?style=for-the-badge&logo=mockito&logoColor=white">
  <img src="https://img.shields.io/badge/RestAssured-000000?style=for-the-badge&logo=rest-assured&logoColor=white">
  <img src="https://img.shields.io/badge/Allure-FF4F58?style=for-the-badge&logo=allure&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
</p>

---

## 🎯 О проекте

**QA-Universe Lab** — это практическая песочница для инженеров по автоматизации тестирования. Проект помогает освоить ключевые темы уровня Middle AQA через реальные примеры и тесты.

### 📚 Что внутри

| Модуль | Темы |
|--------|------|
| **Collections** | HashMap, Stream API, сортировка, поиск дубликатов |
| **Mockito** | Mocks vs Stubs, верификация, ArgumentCaptor, Spy |
| **REST API** | RestAssured, TestContainers, JSON Schema Validation |
| **UI (Selenide)** | Page Object Pattern, WebDriver management, Browser testing |

---

## 🚀 Быстрый старт

```bash
# Клонирование
git clone https://github.com/BlazzerQA/QA-Universe-Lab.git
cd QA-Universe-Lab

# Запуск всех тестов (API + UI)
mvn clean test

# Открыть Allure отчет
mvn allure:serve