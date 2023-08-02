# Планирование автоматизации

## 1. Перечень автоматизируемых сценариев

- #### UI тесты

    - Позитивные сценарии:

        - Оплата тура с использованием обычной дебетовой карты (валидные данные карты и правильный CVC-код).
        - Оплата тура с использованием уникальной технологии (валидные данные карты и правильный CVC-код).
    ####
    - Негативные сценарии:

        - Оплата тура использованием обычной дебетовой карты с невалидными данными карты.
        - Оплата тура использованием обычной дебетовой карты с отклоненной картой.
        - Оплата тура использованием обычной дебетовой карты с неправильным CVC-кодом.
        - Оплата тура с использованием обычной дебетовой карты с невалидными данными карты.
        - Оплата тура с использованием обычной дебетовой карты с отклоненной картой.
        - Оплата тура с использованием обычной дебетовой карты с неправильным CVC-кодом.

## 2. Перечень используемых инструментов с обоснованием выбора

* Язык программирования: Java - широко используемый язык программирования с большим сообществом разработчиков и обширной документацией.
* IntelliJ IDEA. Платформа для удобного написания кода, в том числе для тестов.
* JUnit. Библиотека для написания и запуска авто-тестов.
* Gradle. Система управления зависимости, которая позволит удобно ставить необходимые фреймворки без проблем с постоянной настройкой зависимостей.
* Selenide. Фреймворк для автоматизированного тестирования веб-приложений.
* Allure. Фреймворк, предназначенный для создания подробных отчетов о выполнении тестов.
* Git и Github. Система контроля версий, для хранения кодов автотестов и настроек окружения.

## 3. Перечень и описание возможных рисков при автоматизации

* Изменение структуры API банка или приложения может привести к несовместимости автотестов.
* Отсутствие доступа к банковским сервисам может привести к невозможности проведения автотестов.
* Неправильная конфигурация тестового окружения может привести к некорректным результатам автотестов.

## 4. Интервальная оценка с учётом рисков (в часах)

* Позитивные сценарии: 10 часов.
* Негативные сценарии: 10 часов.
* Настройка окружения и подготовка тестовых данных: 5 часов.
* Разработка автотестов: 15 часов.
* Отладка и исправление ошибок: 5 часов.

## 5. План сдачи работ: когда будут проведены автотесты, результаты их проведения и отчёт по автоматизации

* Проведение автотестов: до 6 августа.
* Результаты проведения автотестов: 8 августа.
* Отчет по автоматизации: 10 августа.