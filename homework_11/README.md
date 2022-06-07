Домашнее задание
Использовать WebFlux

Цель:
Цель: разрабатывать Responsive и Resilent приложения на реактивном стеке Spring c помощью Spring Web Flux и Reactive Spring Data Repositories
Результат: приложение на реактивном стеке Spring

Описание/Пошаговая инструкция выполнения домашнего задания:
За основу для выполнения работы можно взять ДЗ с Ajax + REST (классическое веб-приложение для этого луче не использовать).
Но можно выбрать другую доменную модель (не библиотеку).
Необходимо использовать Reactive Spring Data Repositories.
В качестве БД лучше использовать MongoDB или Redis. Использовать PostgreSQL и экспериментальную R2DBC не рекомендуется.
RxJava vs Project Reactor - на Ваш вкус.
Вместо классического Spring MVC и embedded Web-сервера использовать WebFlux.
Опционально: реализовать на Functional Endpoints Данное задание НЕ засчитывает предыдущие! Рекомендации: Старайтесь избавиться от лишних архитектурных слоёв. Самый простой вариант - весь flow в контроллере.
Критерии оценки:
Факт сдачи:

0 - задание не сдано
1 - задание сдано Степень выполнения (количество работающего функционала, что примет заказчик, что будет проверять тестировщик):
0 - ничего не работает или отсутствует основной функционал
1 - не работает или отсутствует большая часть критического функционала
2 - основной функционал есть, возможны небольшие косяки
3 - основной функционал есть, всё хорошо работает
4 - основной функционал есть, всё хорошо работает, тесты и/или задание перевыполнено Способ выполнения (качество выполнения, стиль кода, как ревью перед мержем):
0 - нужно править, мержить нельзя (нарушение соглашений, публичные поля)
1 - лучше исправить в рамках этого ДЗ для повышения оценки
2 - можно мержить, но в следующих ДЗ нужно поправить.
3 - можно мержить, мелкие недочёты
4 - отличная работа!
5 - экстра балл за особо красивый кусочек кода/решение целиком (ставится только после отличной работы, отдельно не ставится) Статус "Принято" ставится от 6 и выше баллов. Ниже 6, задание не принимается. Идеальное, но не работающее, решение = 1 + 0 + 4 (+4 а не 5) = 5 - не принимается. Если всё работает, но стилю не соответствует (публичные поля, классы капсом) = 1 + 4 + 0 = 5 - не принимается
Рекомендуем сдать до: 27.05.2022

За основу для выполнения работы можно взять ДЗ с Ajax + REST
(классическое веб-приложение для этого луче не использовать).
• Но можно выбрать другую доменную модель (не библиотеку).
• Необходимо использовать Reactive Spring Data Repositories.
• В качестве БД лучше использовать MongoDB или Redis. Использовать
PostgreSQL и экспериментальную R2DBC не рекомендуется.
• RxJava vs Project Reactor - на Ваш вкус.
• Вместо классического Spring MVC и embedded Web-сервера использовать
WebFlux.
• Опционально: реализовать на Functional Endpoints

Данное задание НЕ засчитывает предыдущие!
Рекомендации:
• Старайтесь избавиться от лишних архитектурных слоёв
• Самый простой вариант - весь flow в контроллере.