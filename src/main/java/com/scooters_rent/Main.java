package com.scooters_rent;

import com.scooters_rent.service.CrudAllService;
import com.scooters_rent.service.RentalBusinessService;
import com.scooters_rent.service.TransactionDemoService;
import com.scooters_rent.util.HibernateUtil;
import java.util.Scanner;

public class Main {

    private static final CrudAllService crudAllService = new CrudAllService();
    private static final RentalBusinessService businessService = new RentalBusinessService();
    private static final TransactionDemoService transactionService = new TransactionDemoService();

    public static void main(String[] args) {
        System.out.println("=== Система аренды самокатов (Hibernate) ===\n");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        transactionService.demoSeedData();

        while (running) {
            System.out.print("""
                    \n=== ГЛАВНОЕ МЕНЮ ===
                    [1] CRUD All (ручное управление таблицами)
                    [2] Business Query (аналитические запросы)
                    [3] Transaction Demo (batch + транзакции)
                    [0] Выход
                    > """);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> runCrudAllMenu(scanner);
                case "2" -> runBusinessMenu(scanner);
                case "3" -> runTransactionMenu(scanner);
                case "0" -> {
                    running = false;
                    System.out.println("До свидания!");
                    HibernateUtil.close();
                }
                default -> System.out.println("Неверный выбор.");
            }
        }

        scanner.close();
    }

    // =========================================================
    // 1. CRUD ALL
    // =========================================================

    private static void runCrudAllMenu(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \n--- CRUD All ---
                    [1]  Пользователи (User)
                    [2]  Самокаты (Scooter)
                    [3]  Поездки (Trip)
                    [4]  Маршруты (Route)
                    [5]  Тарифы (Tariff)
                    [6]  Зоны (Zone)
                    [7]  Модели (Model)
                    [8]  Задачи (Task)
                    [9]  Джусеры (Juicer)
                    [0]  Назад
                    > """);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> crudAllService.manageUsers(scanner);
                case "2" -> crudAllService.manageScooters(scanner);
                case "3" -> crudAllService.manageTrips(scanner);
                case "4" -> crudAllService.manageRoutes(scanner);
                case "5" -> crudAllService.manageTariffs(scanner);
                case "6" -> crudAllService.manageZones(scanner);
                case "7" -> crudAllService.manageModels(scanner);
                case "8" -> crudAllService.manageTasks(scanner);
                case "9" -> crudAllService.manageJuicers(scanner);
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    // =========================================================
    // 2. BUSINESS QUERY
    // =========================================================

    private static void runBusinessMenu(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \n--- Business Query ---
                    [1]  Выручка по зонам
                    [2]  Самая частотная модель в ремонте
                    [3]  Самый активный джусер
                    [4]  Самый активный пользователь
                    [5]  Сводка по тарифам
                    [6]  Средняя стоимость поездок
                    [7]  Средняя продолжительность поездок
                    [8]  Топ 3 непопулярных самоката
                    [9]  Все запросы
                    [0]  Назад
                    > """);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> businessService.revenueByZone();
                case "2" -> businessService.mostFrequentModelInRepair();
                case "3" -> businessService.mostActiveJuicer();
                case "4" -> businessService.mostActiveUser();
                case "5" -> businessService.tariffSummary();
                case "6" -> businessService.averageTripCost();
                case "7" -> businessService.averageTripDuration();
                case "8" -> businessService.top3UnpopularScooters();
                case "9" -> businessService.runAll();
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    // =========================================================
    // 3. TRANSACTION DEMO
    // =========================================================

    private static void runTransactionMenu(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \n--- Transaction Demo ---
                    [1] Batch Insert (массовая вставка самокатов)
                    [2] Transaction (тест аренды)
                    [3] Всё сразу
                    [0] Назад
                    > """);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> transactionService.demoBatchInsert(scanner);
                case "2" -> transactionService.demoTransaction();
                case "3" -> transactionService.runAll(scanner);
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }
}