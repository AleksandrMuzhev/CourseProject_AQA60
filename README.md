# Курсовой проект по модулю «Автоматизация тестирования» для профессии «Инженер по тестированию»
[Задание](https://github.com/netology-code/aqa-qamid-diplom)
## Документы
* [План автоматизации](https://github.com/AleksandrMuzhev/CourseProject_AQA60/blob/main/documents/Plan.md)
* [Отчёт по итогам тестирования](https://github.com/AleksandrMuzhev/CourseProject_AQA60/blob/main/documents/Report.md)
* [Отчёт об автоматизации](https://github.com/AleksandrMuzhev/CourseProject_AQA60/blob/main/documents/Summary.md)


## Описание приложения

Приложение — это веб-сервис, который предлагает купить тур по определённой цене двумя способами:
1. Обычная оплата по дебетовой карте.
2. Уникальная технология: выдача кредита по данным банковской карты.

Само приложение не обрабатывает данные по картам, а пересылает их банковским сервисам:

* сервису платежей, далее Payment Gate;
* кредитному сервису, далее Credit Gate.

Приложение в собственной СУБД должно сохранять информацию о том, успешно ли был совершён платёж и каким способом. Данные карт при этом сохранять не допускается.

## Инуструкция по запуску тестов

1. Склонировать проект `git clone git@github.com:AleksandrMuzhev/CourseProject_AQA60.git`

3. Открыть проект в IntelliJ IDEA

5. Запустить контейнеры командой `docker-compose up --build`

7. Для запуска сервиса с указанием пути к базе данных использовать следующие команды:

* для mysql `java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar aqa-shop.jar`

* для postgresql `java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar aqa-shop.jar`

5. SUT открывается по адресу `http://localhost:8080/`

6. Запуск тестов стоит выполнить командой `./gradlew clean test`

7. Для формирования отчета (Allure), после выполнения тестов выполнить команду `./gradlew allureReport`

8. После завершения тестирования завершить работу приложения (CTRL+C) и остановить контейнеры командой `docker-compose down`