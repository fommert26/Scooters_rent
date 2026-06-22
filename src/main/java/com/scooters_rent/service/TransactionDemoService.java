package com.scooters_rent.service;

import com.scooters_rent.model.*;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class TransactionDemoService {

    public void demoSeedData() {
        System.out.println("=== SEED DATA — Заполнение тестовыми данными ===\n");

        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Long count = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
            if (count > 0) {
                System.out.println("Данные уже есть, пропускаем заполнение");
                tx.rollback();
                return;
            }

            // 1. Тарифы
            Tariff tariff1 = new Tariff("Эконом", new BigDecimal("2.50"));
            Tariff tariff2 = new Tariff("Стандарт", new BigDecimal("4.00"));
            Tariff tariff3 = new Tariff("Премиум", new BigDecimal("6.50"));
            Tariff tariff4 = new Tariff("Бизнес", new BigDecimal("8.00"));
            Tariff tariff5 = new Tariff("Ночной", new BigDecimal("3.00"));
            em.persist(tariff1);
            em.persist(tariff2);
            em.persist(tariff3);
            em.persist(tariff4);
            em.persist(tariff5);

            // 2. Зоны
            Zone zone1 = new Zone("Центральная, ул. Ленина 1", true);
            Zone zone2 = new Zone("Северная, ул. Мира 25", true);
            Zone zone3 = new Zone("Южная, ул. Гагарина 10", false);
            Zone zone4 = new Zone("Вокзальная, пл. Вокзальная 5", true);
            Zone zone5 = new Zone("Западная, ул. Западная 15", false);
            em.persist(zone1);
            em.persist(zone2);
            em.persist(zone3);
            em.persist(zone4);
            em.persist(zone5);

            // 3. Модели самокатов
            Model model1 = new Model("Xiaomi", "M365", 1, new BigDecimal("0.85"));
            Model model2 = new Model("Ninebot", "MAX", 2, new BigDecimal("0.95"));
            Model model3 = new Model("Kugoo", "Kirin G2", 1, new BigDecimal("0.80"));
            Model model4 = new Model("Xiaomi", "Pro 2", 2, new BigDecimal("0.90"));
            Model model5 = new Model("Ninebot", "E22", 1, new BigDecimal("0.75"));
            em.persist(model1);
            em.persist(model2);
            em.persist(model3);
            em.persist(model4);
            em.persist(model5);

            // 4. Самокаты
            Scooter scooter1 = new Scooter(model1, zone1, (short) 95);
            Scooter scooter2 = new Scooter(model2, zone1, (short) 87);
            Scooter scooter3 = new Scooter(model1, zone2, (short) 100);
            Scooter scooter4 = new Scooter(model3, zone3, (short) 45);
            Scooter scooter5 = new Scooter(model2, zone4, (short) 78);
            em.persist(scooter1);
            em.persist(scooter2);
            em.persist(scooter3);
            em.persist(scooter4);
            em.persist(scooter5);

            // 5. Маршруты
            Route route1 = new Route(zone1, zone2, (short) 15, (short) 30);
            Route route2 = new Route(zone2, zone3, (short) 8, (short) 25);
            Route route3 = new Route(zone1, zone4, (short) 20, (short) 40);
            Route route4 = new Route(zone3, zone1, (short) 12, (short) 30);
            Route route5 = new Route(zone4, zone5, (short) 25, (short) 45);
            em.persist(route1);
            em.persist(route2);
            em.persist(route3);
            em.persist(route4);
            em.persist(route5);

            // 6. Пользователи
            User user1 = new User("Алексей Иванов", tariff1);
            User user2 = new User("Мария Петрова", tariff2);
            User user3 = new User("Иван Смирнов", tariff3);
            User user4 = new User("Елена Козлова", tariff1);
            User user5 = new User("Дмитрий Соколов", tariff2);
            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
            em.persist(user4);
            em.persist(user5);

            // 7. Поездки
            Trip trip1 = new Trip(scooter1, user1, route1, LocalDateTime.now().minusMinutes(10), 3600, new BigDecimal("150.00"));
            Trip trip2 = new Trip(scooter4, user2, route2, LocalDateTime.now().minusHours(2), 1800, new BigDecimal("80.00"));
            Trip trip3 = new Trip(scooter5, user3, route3, LocalDateTime.now().minusDays(1), 3600, new BigDecimal("260.00"));
            Trip trip4 = new Trip(scooter4, user4, route4, LocalDateTime.now().minusMinutes(1440), 2400, new BigDecimal("160.00"));
            Trip trip5 = new Trip(scooter5, user5, route5, LocalDateTime.now().minusHours(5), 4200, new BigDecimal("336.00"));
            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.persist(trip4);
            em.persist(trip5);

            // 8. Джусеры
            Juicer juicer1 = new Juicer("Иван Техник", "repairman");
            Juicer juicer2 = new Juicer("Петр Мастер", "repairman");
            Juicer juicer3 = new Juicer("Анна Логист", "constellator");
            Juicer juicer4 = new Juicer("Сергей Админ", "administrator");
            Juicer juicer5 = new Juicer("Екатерина Инженер", "repairman");
            em.persist(juicer1);
            em.persist(juicer2);
            em.persist(juicer3);
            em.persist(juicer4);
            em.persist(juicer5);

            // 9. Задачи
            Task task1 = new Task(scooter1, juicer1, LocalDateTime.now().minusDays(1), "Заменить аккумулятор", false);
            Task task2 = new Task(scooter2, juicer2, LocalDateTime.now().minusHours(1), "Проверить тормоза", false);
            Task task3 = new Task(scooter3, juicer3, LocalDateTime.now().minusDays(2), "Обновить прошивку", true);
            Task task4 = new Task(scooter4, juicer4, LocalDateTime.now().minusHours(3), "Заменить покрышки", false);
            Task task5 = new Task(scooter5, juicer5, LocalDateTime.now().minusDays(1), "Диагностика", true);
            em.persist(task1);
            em.persist(task2);
            em.persist(task3);
            em.persist(task4);
            em.persist(task5);

            tx.commit();
            System.out.println("Тестовые данные успешно загружены!");

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // BATCH INSERT
    public void demoBatchInsert(Scanner scanner) {
        System.out.println("=== BATCH INSERT — Массовая вставка самокатов ===");

        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            TypedQuery<Model> modelQuery = em.createQuery("FROM Model", Model.class);
            List<Model> models = modelQuery.getResultList();
            if (models.isEmpty()) {
                System.out.println("Ошибка: нет моделей в БД");
                return;
            }
            System.out.println("\nДоступные модели:");
            for (Model m : models) {
                System.out.printf("  %d. %s %s (коэф: %.2f)%n", m.getId(), m.getBrand(), m.getModel(), m.getModelCoef());
            }
            System.out.print("Выберите ID модели: ");
            int modelId;
            try {
                modelId = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число");
                return;
            }
            Model model = em.find(Model.class, modelId);
            if (model == null) {
                System.out.println("Модель не найдена");
                return;
            }

            TypedQuery<Zone> zoneQuery = em.createQuery("FROM Zone", Zone.class);
            List<Zone> zones = zoneQuery.getResultList();
            if (zones.isEmpty()) {
                System.out.println("Ошибка: нет зон в БД");
                return;
            }
            System.out.println("\nДоступные зоны:");
            for (Zone z : zones) {
                System.out.printf("  %d. %s%n", z.getId(), z.getAddress());
            }
            System.out.print("Выберите ID зоны: ");
            int zoneId;
            try {
                zoneId = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число");
                return;
            }
            Zone zone = em.find(Zone.class, zoneId);
            if (zone == null) {
                System.out.println("Зона не найдена");
                return;
            }

            System.out.print("Введите количество самокатов для вставки: ");
            int count;
            try {
                count = Integer.parseInt(scanner.nextLine().trim());
                if (count <= 0) {
                    System.out.println("Количество должно быть больше 0");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число");
                return;
            }

            tx.begin();

            for (int i = 1; i <= count; i++) {
                Scooter scooter = new Scooter(model, zone, (short) 100);
                em.persist(scooter);
                System.out.println("Добавлен самокат #" + i + " (заряд 100%)");
            }

            tx.commit();
            System.out.printf("Вставлено %d самокатов (batch)%n", count);

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Ошибка batch-вставки: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        System.out.println();
    }

    // TRANSACTION
    public void demoTransaction() {
        System.out.println("=== TRANSACTION — Тестирование аренды ===");
        System.out.println("Тестируем 3 сценария:\n");

        testRentalScenario(3, "Сценарий 1: Самокат свободен (id=3)");
        testRentalScenario(1, "Сценарий 2: Самокат в аренде (id=1)");
        testRentalScenario(4, "Сценарий 3: Аренда завершилась (id=4)");

        System.out.println();
    }

    private void testRentalScenario(int scooterId, String scenarioName) {
        System.out.println("--- " + scenarioName + " ---");
        System.out.println("Попытка начать поездку: пользователь=1, самокат=" + scooterId);

        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            User user = em.find(User.class, 1);
            if (user == null) {
                System.out.println("  Результат: пользователь не найден");
                tx.rollback();
                return;
            }

            Scooter scooter = em.find(Scooter.class, scooterId);
            if (scooter == null) {
                System.out.println("  Результат: самокат не найден");
                tx.rollback();
                return;
            }

            Route route = em.find(Route.class, 1);
            if (route == null) {
                System.out.println("  Результат: маршрут не найден");
                tx.rollback();
                return;
            }

            // Проверяем доступность самоката (исправленный метод)
            if (!isScooterAvailable(em, scooterId)) {
                System.out.println("  Результат: самокат уже в аренде");
                tx.rollback();
                return;
            }

            int durationSeconds = 1800;
            BigDecimal cost = calculateCost(em, 1, scooterId, durationSeconds);

            System.out.println("  Расчёт стоимости:");
            System.out.println("    Длительность: " + (durationSeconds / 60) + " мин");
            System.out.println("    Итоговая стоимость: " + cost + " руб");

            Trip trip = new Trip(scooter, user, route, LocalDateTime.now(), durationSeconds, cost);
            em.persist(trip);

            tx.commit();

            System.out.println("  Результат: УСПЕШНО - поездка начата! ID=" + trip.getId());
            System.out.println("    Начало: " + trip.getStartTime());
            System.out.println("    Стоимость: " + cost + " руб");
            System.out.println("    Окончание: " + trip.getStartTime().plusSeconds(durationSeconds));

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        System.out.println();
    }

    public void runAll(Scanner scanner) {
        demoBatchInsert(scanner);
        demoTransaction();
    }

    // =========================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // =========================================================

    private boolean isScooterAvailable(EntityManager em, int scooterId) {
        // 1. Получаем все поездки для этого самоката
        TypedQuery<Trip> query = em.createQuery(
                "SELECT t FROM Trip t WHERE t.scooter.id = :scooterId",
                Trip.class
        );
        query.setParameter("scooterId", scooterId);
        List<Trip> trips = query.getResultList();

        if (trips.isEmpty()) {
            return true;
        }

        // 2. Ищем самую позднюю дату окончания аренды
        LocalDateTime lastEnd = null;
        for (Trip trip : trips) {
            LocalDateTime endTime = trip.getStartTime().plusSeconds(trip.getDuration());
            if (lastEnd == null || endTime.isAfter(lastEnd)) {
                lastEnd = endTime;
            }
        }

        // 3. Проверяем, свободен ли самокат (если последняя аренда закончилась больше 5 секунд назад)
        if (lastEnd == null) {
            return true;
        }

        return lastEnd.isBefore(LocalDateTime.now().minusSeconds(5));
    }

    private BigDecimal calculateCost(EntityManager em, int userId, int scooterId, int durationSeconds) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        if (user.getTariff() == null) {
            throw new RuntimeException("У пользователя нет тарифа");
        }

        Tariff tariff = user.getTariff();
        Scooter scooter = em.find(Scooter.class, scooterId);
        if (scooter == null) {
            throw new RuntimeException("Самокат не найден");
        }

        Model model = scooter.getModel();
        if (model == null) {
            throw new RuntimeException("Модель самоката не найдена");
        }

        BigDecimal pricePerMinute = tariff.getPricePerMinute();
        BigDecimal modelCoefficient = model.getModelCoef();
        int durationMinutes = durationSeconds / 60;

        return pricePerMinute
                .multiply(modelCoefficient)
                .multiply(BigDecimal.valueOf(durationMinutes))
                .setScale(2, RoundingMode.HALF_UP);
    }
}