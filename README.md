# Product Catalog API

![Java 17+](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.1-brightgreen)

REST API для управління каталогом товарів з кешуванням та захистом.

## 🔧 Технології
- Spring Boot 3, H2 Database
- Кешування Caffeine (10 хв)
- Rate Limiting (100 запитів/хв)
- Аутентифікація (admin/admin123, user/user123)
- Swagger документація

## 🚀 Запуск
```bash
git clone https://github.com/AdamenkoVlad/product-catalog.git
cd product-catalog
mvn spring-boot:run

Доступ
Swagger: http://localhost:8080/swagger-ui.html

H2 Console: http://localhost:8080/h2-console

Логін: admin/admin123
