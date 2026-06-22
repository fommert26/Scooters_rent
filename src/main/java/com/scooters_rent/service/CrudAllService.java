package com.scooters_rent.service;

import com.scooters_rent.model.*;
import com.scooters_rent.repository.*;

import java.util.Scanner;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityTransaction;


public class CrudAllService {
    private final UserRepository userRepo = new UserRepository();
    private final ScooterRepository scooterRepo = new ScooterRepository();
    private final TripRepository tripRepo = new TripRepository();
    private final RouteRepository routeRepo = new RouteRepository();
    private final TariffRepository tariffRepo = new TariffRepository();
    private final ZoneRepository zoneRepo = new ZoneRepository();
    private final ModelRepository modelRepo = new ModelRepository();
    private final TaskRepository taskRepo = new TaskRepository();
    private final JuicerRepository juicerRepo = new JuicerRepository();

    // Juicer
    public void manageJuicers(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \nДжусеры меню:
                    [1] Создать
                    [2] Найти по ID
                    [3] Найти по роли
                    [4] Поиск по имени
                    [5] Найти всех
                    [6] Обновить
                    [7] Удалить
                    [0] Назад
                    >""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createJuicer(scanner);
                case "2" -> findJuicerById(scanner);
                case "3" -> findJuicerByRole(scanner);
                case "4" -> searchJuicerByName(scanner);
                case "5" -> findAllJuicers();
                case "6" -> updateJuicer(scanner);
                case "7" -> deleteJuicer(scanner);
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createJuicer(Scanner scanner) {
        System.out.println("\nСоздание джусера");
        System.out.print("Введите полное имя: ");
        String fullName = scanner.nextLine().trim();
        if (fullName.isEmpty()) {
            System.out.println("Ошибка: имя не может быть пустым!");
            return;
        }

        System.out.print("Введите роль (repairman / constellator / administrator, или оставьте пустым): ");
        String role = scanner.nextLine().trim();
        if (role.isEmpty()) {
            role = null;
        }

        Juicer juicer = new Juicer(fullName, role);
        juicerRepo.save(juicer);

        System.out.println("Джусер создан: ID=" + juicer.getId());
    }

    private void findJuicerById(Scanner scanner) {
        System.out.println("\nПоиск джусера по ID.");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Juicer juicer = juicerRepo.findById(id).orElse(null);
            if (juicer == null) {
                System.out.println("Джусер с ID=" + id + " не найден");
            } else {
                System.out.println(juicer);
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findJuicerByRole(Scanner scanner) {
        System.out.println("\nПоиск джусера по роли");
        System.out.print("Введите роль (repairman / constellator / administrator): ");
        String role = scanner.nextLine().trim();
        if (role.isEmpty()) {
            System.out.println("Роль не может быть пустой");
            return;
        }
        List<Juicer> juicers = juicerRepo.findByRole(role);
        if (juicers.isEmpty()) {
            System.out.println("Джусеры с ролью '" + role + "' не найдены");
            return;
        }
        System.out.printf("%-5s %-30s %-15s%n", "ID", "Имя", "Роль");
        for (Juicer j : juicers) {
            System.out.printf("%-5d %-30s %-15s%n",
                    j.getId(), j.getFullName(), j.getRole());
        }
    }

    private void searchJuicerByName(Scanner scanner) {
        System.out.println("\nПоиск джусера по части имени");
        System.out.print("Введите часть имени: ");
        String namePart = scanner.nextLine().trim();
        if (namePart.isEmpty()) {
            System.out.println("Имя не может быть пустым");
            return;
        }
        List<Juicer> juicers = juicerRepo.findByFullNameContaining(namePart);
        if (juicers.isEmpty()) {
            System.out.println("Джусеры с именем содержащим '" + namePart + "' не найдены");
            return;
        }
        System.out.printf("%-5s %-30s %-15s%n", "ID", "Имя", "Роль");
        for (Juicer j : juicers) {
            System.out.printf("%-5d %-30s %-15s%n",
                    j.getId(), j.getFullName(),
                    j.getRole() != null ? j.getRole() : "—");
        }
    }

    private void findAllJuicers() {
        System.out.println("\nВсе джусеры");
        List<Juicer> juicers = juicerRepo.findAll();
        if (juicers.isEmpty()) {
            System.out.println("Джусеров нет");
            return;
        }
        System.out.printf("%-5s %-30s %-15s%n", "ID", "Имя", "Роль");
        for (Juicer j : juicers) {
            System.out.printf("%-5d %-30s %-15s%n",
                    j.getId(), j.getFullName(),
                    j.getRole() != null ? j.getRole() : "—");
        }
    }

    private void updateJuicer(Scanner scanner) {
        System.out.println("\nОбновление джусера");
        System.out.print("Введите ID джусера для обновления: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Juicer juicer = juicerRepo.findById(id).orElse(null);
            if (juicer == null) {
                System.out.println("Джусер с ID=" + id + " не найден");
                return;
            }

            System.out.println("Текущие данные: " + juicer);
            System.out.print("Введите новое имя: ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                juicer.setFullName(newName);
            } else {
                System.out.println("Ошибка: имя не может быть пустым!");
                return;
            }

            System.out.print("Введите новую роль (repairman / constellator / administrator, или оставьте пустым для пропуска): ");
            String newRole = scanner.nextLine().trim();
            if (!newRole.isEmpty()) {
                juicer.setRole(newRole);
            } else {
                juicer.setRole(null);
            }

            juicerRepo.update(juicer);
            System.out.println("Джусер обновлён:");
            System.out.println(juicer);

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void deleteJuicer(Scanner scanner) {
        System.out.println("\nУдаление джусера");
        System.out.print("Введите ID джусера для удаления: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            EntityManager em = HibernateUtil.getEntityManager();
            try {
                Long taskCount = em.createQuery(
                        "SELECT COUNT(t) FROM Task t WHERE t.juicer.id = :juicerId",
                        Long.class
                ).setParameter("juicerId", id).getSingleResult();

                if (taskCount > 0) {
                    System.out.println("Ошибка: у джусера есть " + taskCount + " задач. Сначала удалите задачи.");
                    return;
                }
            } finally {
                em.close();
            }

            juicerRepo.deleteById(id);
            System.out.println("Джусер с ID=" + id + " удалён");

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    // Model

    public void manageModels(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \nМодели меню:
                    [1] Создать
                    [2] Найти по ID
                    [3] Найти всех
                    [4] Поиск по бренду
                    [5] Поиск по модели
                    [6] Поиск по поколению
                    [7] Обновить
                    [8] Удалить
                    [0] Назад
                    > """);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createModel(scanner);
                case "2" -> findModelById(scanner);
                case "3" -> findAllModels();
                case "4" -> searchModelByBrand(scanner);
                case "5" -> searchModelByName(scanner);
                case "6" -> searchModelByGeneration(scanner);
                case "7" -> updateModel(scanner);
                case "8" -> deleteModel(scanner);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    public void createModel(Scanner scanner){
        System.out.println("\nСоздание новой модели");
        System.out.print("Введите название бренда: ");
        String brand = scanner.nextLine().trim();
        if(brand.isEmpty()){
            System.out.println("Ошибка: Название бренда не может быть пустым.");
            return;
        }

        System.out.print("Введите название модели: ");
        String modelName = scanner.nextLine().trim();
        if(modelName.isEmpty()){
            System.out.println("Ошибка: Название модели не может быть пустым");
            return;
        }

        System.out.print("Введите поколение (или 0 для пропуска): ");
        Integer generation = null;
        try {
            int gen = Integer.parseInt(scanner.nextLine().trim());
            if (gen > 0) generation = gen;
        } catch (NumberFormatException e) {
        }

        System.out.print("Введите коэффициент модели (например 0.85, от 0 до 1): ");
        BigDecimal modelCoef;
        try {
            modelCoef = new BigDecimal(scanner.nextLine().trim());
            if (modelCoef.compareTo(BigDecimal.ZERO) > 0 && modelCoef.compareTo(BigDecimal.ONE) < 0) {
            } else {
                System.out.println("Ошибка: коэффициент должен быть больше 0 и меньше 1");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
            return;
        }

        Model model = new Model(brand, modelName, generation, modelCoef);
        modelRepo.save(model);
        System.out.println("Модель создана: ID=" + model.getId());
    }

    private void findModelById(Scanner scanner) {
        System.out.println("\nПоиск модели по ID");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Model model = modelRepo.findById(id).orElse(null);
            if (model == null) System.out.println("Модель не найдена");
            else System.out.println(model);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findAllModels() {
        System.out.println("\nВсе модели");
        List<Model> models = modelRepo.findAll();
        if (models.isEmpty()) {
            System.out.println("Нет моделей");
            return;
        }
        System.out.printf("%-5s %-20s %-20s %-10s %-10s%n", "ID", "Бренд", "Модель", "Поколение", "Коэф");
        for (Model m : models) {
            System.out.printf("%-5d %-20s %-20s %-10s %-10.2f%n",
                    m.getId(),
                    m.getBrand(),
                    m.getModel(),
                    m.getGeneration() != null ? m.getGeneration() : "—",
                    m.getModelCoef());
        }
    }

    private void searchModelByBrand(Scanner scanner) {
        System.out.println("\nПоиск по бренду");
        System.out.print("Введите бренд: ");
        String brand = scanner.nextLine().trim();
        if (brand.isEmpty()) {
            System.out.println("Бренд не может быть пустым");
            return;
        }
        List<Model> models = modelRepo.searchByBrand(brand);
        if (models.isEmpty()) {
            System.out.println("Модели с брендом '" + brand + "' не найдены");
            return;
        }
        System.out.printf("%-5s %-20s %-20s %-10s %-10s%n", "ID", "Бренд", "Модель", "Поколение", "Коэф");
        for (Model m : models) {
            System.out.printf("%-5d %-20s %-20s %-10s %-10.2f%n",
                    m.getId(),
                    m.getBrand(),
                    m.getModel(),
                    m.getGeneration() != null ? m.getGeneration() : "—",
                    m.getModelCoef());
        }
    }

    private void searchModelByName(Scanner scanner) {
        System.out.println("\nПоиск по модели");
        System.out.print("Введите название модели: ");
        String modelName = scanner.nextLine().trim();
        if (modelName.isEmpty()) {
            System.out.println("Название не может быть пустым");
            return;
        }
        List<Model> models = modelRepo.searchByModel(modelName);
        if (models.isEmpty()) {
            System.out.println("Модели с названием '" + modelName + "' не найдены");
            return;
        }
        System.out.printf("%-5s %-20s %-20s %-10s %-10s%n", "ID", "Бренд", "Модель", "Поколение", "Коэф");
        for (Model m : models) {
            System.out.printf("%-5d %-20s %-20s %-10s %-10.2f%n",
                    m.getId(),
                    m.getBrand(),
                    m.getModel(),
                    m.getGeneration() != null ? m.getGeneration() : "—",
                    m.getModelCoef());
        }
    }

    private void searchModelByGeneration(Scanner scanner) {
        System.out.println("\nПоиск по поколению");
        System.out.print("Введите поколение: ");
        try {
            int generation = Integer.parseInt(scanner.nextLine().trim());
            List<Model> models = modelRepo.searchByGeneration(generation);
            if (models.isEmpty()) {
                System.out.println("Модели с поколением " + generation + " не найдены");
                return;
            }
            System.out.printf("%-5s %-20s %-20s %-10s %-10s%n", "ID", "Бренд", "Модель", "Поколение", "Коэф");
            for (Model m : models) {
                System.out.printf("%-5d %-20s %-20s %-10s %-10.2f%n",
                        m.getId(),
                        m.getBrand(),
                        m.getModel(),
                        m.getGeneration() != null ? m.getGeneration() : "—",
                        m.getModelCoef());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }
    private void updateModel(Scanner scanner) {
        System.out.println("\nОбновление модели");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Model model = modelRepo.findById(id).orElse(null);
            if (model == null) {
                System.out.println("Модель не найдена");
                return;
            }
            System.out.println("Текущие данные: " + model);

            System.out.print("Новый бренд (или Enter для пропуска): ");
            String brand = scanner.nextLine().trim();
            if (!brand.isEmpty()) {
                model.setBrand(brand);
            }

            System.out.print("Новая модель (или Enter для пропуска): ");
            String modelName = scanner.nextLine().trim();
            if (!modelName.isEmpty()) {
                model.setModel(modelName);
            }

            System.out.print("Новое поколение (или Enter для пропуска, 0 для null): ");
            String genInput = scanner.nextLine().trim();
            if (!genInput.isEmpty()) {
                try {
                    int gen = Integer.parseInt(genInput);
                    if (gen == 0) {
                        model.setGeneration(null);
                    } else if (gen > 0) {
                        model.setGeneration(gen);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите корректное число");
                    return;
                }
            }

            System.out.print("Новый коэффициент (или Enter для пропуска): ");
            String coefInput = scanner.nextLine().trim();
            if (!coefInput.isEmpty()) {
                try {
                    BigDecimal coef = new BigDecimal(coefInput);
                    if (coef.compareTo(BigDecimal.ZERO) > 0 && coef.compareTo(BigDecimal.ONE) < 0) {
                        model.setModelCoef(coef);
                    } else {
                        System.out.println("Ошибка: коэффициент должен быть больше 0 и меньше 1");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите корректное число");
                    return;
                }
            }

            modelRepo.update(model);
            System.out.println("Модель обновлена: " + model);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void deleteModel(Scanner scanner) {
        System.out.println("\nУдаление модели");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            List<Scooter> scooters = scooterRepo.findByModelId(id);
            if (!scooters.isEmpty()) {
                System.out.println("Ошибка: есть самокаты с этой моделью (" + scooters.size() + " шт.)");
                return;
            }
            modelRepo.deleteById(id);
            System.out.println("Модель удалена");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

// Tariff

    public void manageTariffs(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \nУправление тарифами (Tariff)
                    [1] Создать
                    [2] Найти по ID
                    [3] Найти всех
                    [4] Найти по названию
                    [5] Самый дешёвый
                    [6] Самый дорогой
                    [7] Обновить
                    [8] Удалить
                    [0] Назад
                    > """);
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createTariff(scanner);
                case "2" -> findTariffById(scanner);
                case "3" -> findAllTariffs();
                case "4" -> findTariffByName(scanner);
                case "5" -> findCheapestTariff();
                case "6" -> findMostExpensiveTariff();
                case "7" -> updateTariff(scanner);
                case "8" -> deleteTariff(scanner);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createTariff(Scanner scanner) {
        System.out.println("\nСоздание тарифа");
        System.out.print("Введите название: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Ошибка: название не может быть пустым");
            return;
        }
        System.out.print("Введите цену за минуту (например 5.00, от 0 до 10): ");
        BigDecimal price;
        try {
            price = new BigDecimal(scanner.nextLine().trim());
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Ошибка: цена должна быть больше 0");
                return;
            }
            if (price.compareTo(new BigDecimal("10.00")) > 0) {
                System.out.println("Ошибка: цена должна быть меньше 10");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
            return;
        }
        Tariff tariff = new Tariff(name, price);
        tariffRepo.save(tariff);
        System.out.println("Тариф создан: ID=" + tariff.getId());
    }

    private void findTariffById(Scanner scanner) {
        System.out.println("\nПоиск тарифа по ID");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Tariff tariff = tariffRepo.findById(id).orElse(null);
            if (tariff == null) System.out.println("Тариф не найден");
            else System.out.println(tariff);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findAllTariffs() {
        System.out.println("\nВсе тарифы");
        List<Tariff> tariffs = tariffRepo.findAll();
        if (tariffs.isEmpty()) {
            System.out.println("Нет тарифов");
            return;
        }
        System.out.printf("%-5s %-25s %-15s%n", "ID", "Название", "Цена/мин");
        for (Tariff t : tariffs) {
            System.out.printf("%-5d %-25s %-15.2f%n", t.getId(), t.getName(), t.getPricePerMinute());
        }
    }

    private void findTariffByName(Scanner scanner) {
        System.out.println("\nПоиск тарифа по названию");
        System.out.print("Введите название (или часть): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Название не может быть пустым");
            return;
        }
        List<Tariff> tariffs = tariffRepo.findByName(name);
        if (tariffs.isEmpty()) {
            System.out.println("Тарифы с названием '" + name + "' не найдены");
            return;
        }
        System.out.printf("%-5s %-25s %-15s%n", "ID", "Название", "Цена/мин");
        for (Tariff t : tariffs) {
            System.out.printf("%-5d %-25s %-15.2f%n", t.getId(), t.getName(), t.getPricePerMinute());
        }
    }

    private void findCheapestTariff() {
        System.out.println("\nСамый дешёвый тариф");
        try {
            Tariff tariff = tariffRepo.findCheapest();
            System.out.println(tariff);
        } catch (Exception e) {
            System.out.println("Нет тарифов в базе данных");
        }
    }

    private void findMostExpensiveTariff() {
        System.out.println("\nСамый дорогой тариф");
        try {
            Tariff tariff = tariffRepo.findMostExpensive();
            System.out.println(tariff);
        } catch (Exception e) {
            System.out.println("Нет тарифов в базе данных");
        }
    }

    private void updateTariff(Scanner scanner) {
        System.out.println("\nОбновление тарифа");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Tariff tariff = tariffRepo.findById(id).orElse(null);
            if (tariff == null) {
                System.out.println("Тариф не найден");
                return;
            }
            System.out.println("Текущие данные: " + tariff);
            System.out.print("Новое название (или Enter для пропуска): ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) tariff.setName(name);

            System.out.print("Новая цена (или Enter для пропуска, от 0 до 10): ");
            String priceInput = scanner.nextLine().trim();
            if (!priceInput.isEmpty()) {
                try {
                    BigDecimal price = new BigDecimal(priceInput);
                    if (price.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Ошибка: цена должна быть больше 0");
                        return;
                    }
                    if (price.compareTo(new BigDecimal("10.00")) > 0) {
                        System.out.println("Ошибка: цена должна быть меньше 10");
                        return;
                    }
                    tariff.setPricePerMinute(price);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите корректное число");
                    return;
                }
            }
            tariffRepo.update(tariff);
            System.out.println("Тариф обновлён: " + tariff);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void deleteTariff(Scanner scanner) {
        System.out.println("\nУдаление тарифа");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            List<User> users = userRepo.findByTariffId(id);
            if (!users.isEmpty()) {
                System.out.println("Ошибка: есть пользователи с этим тарифом (" + users.size() + " шт.)");
                return;
            }
            tariffRepo.deleteById(id);
            System.out.println("Тариф удалён");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    // Zone

    public void manageZones(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \nУправление зонами (Zone)
                    [1] Создать
                    [2] Найти по ID
                    [3] Найти всех
                    [4] Поиск по адресу
                    [5] С парковкой
                    [6] Без парковки
                    [7] Обновить
                    [8] Удалить
                    [0] Назад
                    > """);
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createZone(scanner);
                case "2" -> findZoneById(scanner);
                case "3" -> findAllZones();
                case "4" -> searchZoneByAddress(scanner);
                case "5" -> findZonesWithParking();
                case "6" -> findZonesWithoutParking();
                case "7" -> updateZone(scanner);
                case "8" -> deleteZone(scanner);
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createZone(Scanner scanner) {
        System.out.println("\nСоздание зоны");
        System.out.print("Введите адрес: ");
        String address = scanner.nextLine().trim();
        if (address.isEmpty()) {
            System.out.println("Ошибка: адрес не может быть пустым");
            return;
        }
        System.out.print("Есть парковка? (true/false): ");
        boolean parkingAvailable;
        try {
            parkingAvailable = Boolean.parseBoolean(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Ошибка: введите true или false");
            return;
        }
        Zone zone = new Zone(address, parkingAvailable);
        zoneRepo.save(zone);
        System.out.println("Зона создана: ID=" + zone.getId());
    }

    private void findZoneById(Scanner scanner) {
        System.out.println("\nПоиск зоны по ID");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Zone zone = zoneRepo.findById(id).orElse(null);
            if (zone == null) System.out.println("Зона не найдена");
            else System.out.println(zone);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findAllZones() {
        System.out.println("\nВсе зоны");
        List<Zone> zones = zoneRepo.findAll();
        if (zones.isEmpty()) {
            System.out.println("Нет зон");
            return;
        }
        System.out.printf("%-5s %-30s %-15s%n", "ID", "Адрес", "Парковка");
        for (Zone z : zones) {
            System.out.printf("%-5d %-30s %-15s%n",
                    z.getId(),
                    z.getAddress(),
                    z.isParkingAvailable() ? "Да" : "Нет");
        }
    }

    private void searchZoneByAddress(Scanner scanner) {
        System.out.println("\nПоиск зоны по части адреса");
        System.out.print("Введите часть адреса: ");
        String addressPart = scanner.nextLine().trim();
        if (addressPart.isEmpty()) {
            System.out.println("Адрес не может быть пустым");
            return;
        }
        List<Zone> zones = zoneRepo.searchByAddress(addressPart);
        if (zones.isEmpty()) {
            System.out.println("Зоны с адресом '" + addressPart + "' не найдены");
            return;
        }
        System.out.printf("%-5s %-30s %-15s%n", "ID", "Адрес", "Парковка");
        for (Zone z : zones) {
            System.out.printf("%-5d %-30s %-15s%n",
                    z.getId(),
                    z.getAddress(),
                    z.isParkingAvailable() ? "Да" : "Нет");
        }
    }

    private void findZonesWithParking() {
        System.out.println("\nЗоны с парковкой");
        List<Zone> zones = zoneRepo.findAllWithParking();
        if (zones.isEmpty()) {
            System.out.println("Нет зон с парковкой");
            return;
        }
        System.out.printf("%-5s %-30s%n", "ID", "Адрес");
        for (Zone z : zones) {
            System.out.printf("%-5d %-30s%n", z.getId(), z.getAddress());
        }
    }

    private void findZonesWithoutParking() {
        System.out.println("\nЗоны без парковки");
        List<Zone> zones = zoneRepo.findAllWithoutParking();
        if (zones.isEmpty()) {
            System.out.println("Нет зон без парковки");
            return;
        }
        System.out.printf("%-5s %-30s%n", "ID", "Адрес");
        for (Zone z : zones) {
            System.out.printf("%-5d %-30s%n", z.getId(), z.getAddress());
        }
    }

    private void updateZone(Scanner scanner) {
        System.out.println("\nОбновление зоны");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Zone zone = zoneRepo.findById(id).orElse(null);
            if (zone == null) {
                System.out.println("Зона не найдена");
                return;
            }
            System.out.println("Текущие данные: " + zone);
            System.out.print("Новый адрес (или Enter для пропуска): ");
            String address = scanner.nextLine().trim();
            if (!address.isEmpty()) zone.setAddress(address);
            System.out.print("Есть парковка? (true/false, или Enter для пропуска): ");
            String parkingInput = scanner.nextLine().trim();
            if (!parkingInput.isEmpty()) {
                zone.setParkingAvailable(Boolean.parseBoolean(parkingInput));
            }
            zoneRepo.update(zone);
            System.out.println("Зона обновлена: " + zone);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void deleteZone(Scanner scanner) {
        System.out.println("\nУдаление зоны");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            List<Scooter> scooters = scooterRepo.findByZoneId(id);
            if (!scooters.isEmpty()) {
                System.out.println("Ошибка: есть самокаты в этой зоне (" + scooters.size() + " шт.)");
                return;
            }
            List<Route> routesStart = routeRepo.findByStartZoneId(id);
            List<Route> routesEnd = routeRepo.findByEndZoneId(id);
            if (!routesStart.isEmpty() || !routesEnd.isEmpty()) {
                System.out.println("Ошибка: есть маршруты с этой зоной");
                return;
            }
            zoneRepo.deleteById(id);
            System.out.println("Зона удалена");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    // Route

    public void manageRoutes(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \nУправление маршрутами (Route)
                    [1] Создать
                    [2] Найти по ID
                    [3] Найти всех
                    [4] Поиск по зонам (start/end)
                    [5] По диапазону расстояния
                    [6] Самый короткий
                    [7] Самый длинный
                    [8] Обновить
                    [9] Удалить
                    [0] Назад
                    > """);
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createRoute(scanner);
                case "2" -> findRouteById(scanner);
                case "3" -> findAllRoutes();
                case "4" -> findRouteByZones(scanner);
                case "5" -> findRouteByDistanceRange(scanner);
                case "6" -> findShortestRoute();
                case "7" -> findLongestRoute();
                case "8" -> updateRoute(scanner);
                case "9" -> deleteRoute(scanner);
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createRoute(Scanner scanner) {
        System.out.println("\nСоздание маршрута");
        List<Zone> zones = zoneRepo.findAll();
        if (zones.isEmpty()) {
            System.out.println("Ошибка: нет зон. Сначала создайте зону.");
            return;
        }
        System.out.println("Доступные зоны:");
        for (Zone z : zones) {
            System.out.printf("  %d. %s%n", z.getId(), z.getAddress());
        }
        System.out.print("Введите ID начальной зоны: ");
        try {
            int startId = Integer.parseInt(scanner.nextLine().trim());
            Zone startZone = zoneRepo.findById(startId).orElse(null);
            if (startZone == null) {
                System.out.println("Начальная зона не найдена");
                return;
            }
            System.out.print("Введите ID конечной зоны: ");
            int endId = Integer.parseInt(scanner.nextLine().trim());
            Zone endZone = zoneRepo.findById(endId).orElse(null);
            if (endZone == null) {
                System.out.println("Конечная зона не найдена");
                return;
            }
            System.out.print("Введите расстояние (км): ");
            short distance = Short.parseShort(scanner.nextLine().trim());
            if (distance <= 0 || distance >= 100) {
                System.out.println("Ошибка: расстояние должно быть от 1 до 99");
                return;
            }
            System.out.print("Введите ограничение скорости (или 0 для пропуска): ");
            Short speedLimit = null;
            try {
                int limit = Integer.parseInt(scanner.nextLine().trim());
                if (limit > 0) speedLimit = (short) limit;
            } catch (NumberFormatException e) {}

            Route route = new Route(startZone, endZone, distance, speedLimit);
            routeRepo.save(route);
            System.out.println("Маршрут создан: ID=" + route.getId());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findRouteById(Scanner scanner) {
        System.out.println("\nПоиск маршрута по ID");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Route route = routeRepo.findById(id).orElse(null);
            if (route == null) System.out.println("Маршрут не найден");
            else System.out.println(route);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findAllRoutes() {
        System.out.println("\nВсе маршруты");
        List<Route> routes = routeRepo.findAll();
        if (routes.isEmpty()) {
            System.out.println("Нет маршрутов");
            return;
        }
        System.out.printf("%-5s %-25s %-25s %-10s %-10s%n", "ID", "Откуда", "Куда", "Км", "Скорость");
        for (Route r : routes) {
            System.out.printf("%-5d %-25s %-25s %-10d %-10s%n",
                    r.getId(),
                    r.getStartZone() != null ? r.getStartZone().getAddress() : "—",
                    r.getEndZone() != null ? r.getEndZone().getAddress() : "—",
                    r.getDistance(),
                    r.getSpeedLimit() != null ? r.getSpeedLimit() + " км/ч" : "нет");
        }
    }

    private void findRouteByZones(Scanner scanner) {
        System.out.println("\nПоиск маршрута по зонам");
        System.out.print("Введите ID начальной зоны: ");
        try {
            int startId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Введите ID конечной зоны: ");
            int endId = Integer.parseInt(scanner.nextLine().trim());
            List<Route> routes = routeRepo.findByZones(startId, endId);
            if (routes.isEmpty()) {
                System.out.println("Маршрут не найден");
                return;
            }
            System.out.printf("%-5s %-10s %-10s%n", "ID", "Км", "Скорость");
            for (Route r : routes) {
                System.out.printf("%-5d %-10d %-10s%n",
                        r.getId(),
                        r.getDistance(),
                        r.getSpeedLimit() != null ? r.getSpeedLimit() + " км/ч" : "нет");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findRouteByDistanceRange(Scanner scanner) {
        System.out.println("\nПоиск маршрута по расстоянию");
        System.out.print("Введите минимальное расстояние (км): ");
        try {
            int min = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Введите максимальное расстояние (км): ");
            int max = Integer.parseInt(scanner.nextLine().trim());
            List<Route> routes = routeRepo.findByDistanceBetween(min, max);
            if (routes.isEmpty()) {
                System.out.println("Маршруты не найдены");
                return;
            }
            System.out.printf("%-5s %-25s %-25s %-10s%n", "ID", "Откуда", "Куда", "Км");
            for (Route r : routes) {
                System.out.printf("%-5d %-25s %-25s %-10d%n",
                        r.getId(),
                        r.getStartZone() != null ? r.getStartZone().getAddress() : "—",
                        r.getEndZone() != null ? r.getEndZone().getAddress() : "—",
                        r.getDistance());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findShortestRoute() {
        System.out.println("\nСамый короткий маршрут");
        try {
            Route route = routeRepo.findShortest();
            System.out.println(route);
        } catch (Exception e) {
            System.out.println("Нет маршрутов");
        }
    }

    private void findLongestRoute() {
        System.out.println("\nСамый длинный маршрут");
        try {
            Route route = routeRepo.findLongest();
            System.out.println(route);
        } catch (Exception e) {
            System.out.println("Нет маршрутов");
        }
    }

    private void updateRoute(Scanner scanner) {
        System.out.println("\nОбновление маршрута");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Route route = routeRepo.findById(id).orElse(null);
            if (route == null) {
                System.out.println("Маршрут не найден");
                return;
            }
            System.out.println("Текущие данные: " + route);
            System.out.print("Новое расстояние (или Enter для пропуска): ");
            String distInput = scanner.nextLine().trim();
            if (!distInput.isEmpty()) {
                short dist = Short.parseShort(distInput);
                if (dist > 0 && dist < 100) route.setDistance(dist);
                else System.out.println("Ошибка: расстояние должно быть от 1 до 99");
            }
            System.out.print("Новое ограничение скорости (или Enter для пропуска): ");
            String speedInput = scanner.nextLine().trim();
            if (!speedInput.isEmpty()) {
                if (speedInput.equals("0")) {
                    route.setSpeedLimit(null);
                } else {
                    route.setSpeedLimit(Short.parseShort(speedInput));
                }
            }
            routeRepo.update(route);
            System.out.println("Маршрут обновлён: " + route);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void deleteRoute(Scanner scanner) {
        System.out.println("\nУдаление маршрута");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            List<Trip> trips = tripRepo.findByRouteId(id);
            if (!trips.isEmpty()) {
                System.out.println("Ошибка: есть поездки с этим маршрутом (" + trips.size() + " шт.)");
                return;
            }
            routeRepo.deleteById(id);
            System.out.println("Маршрут удалён");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    // Scooter

    public void manageScooters(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \nУправление самокатами (Scooter)
                    [1] Создать
                    [2] Найти по ID
                    [3] Найти всех
                    [4] Поиск по модели
                    [5] Поиск по зоне
                    [6] Поиск с зарядом больше
                    [7] По бренду модели
                    [8] По зоне с сортировкой
                    [9] Обновить
                    [10] Удалить
                    [0] Назад
                    >""");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createScooter(scanner);
                case "2" -> findScooterById(scanner);
                case "3" -> findAllScooters();
                case "4" -> findScooterByModel(scanner);
                case "5" -> findScooterByZone(scanner);
                case "6" -> findScooterByMinCharge(scanner);
                case "7" -> findScooterByModelBrand(scanner);
                case "8" -> findScooterByZoneSorted(scanner);
                case "9" -> updateScooter(scanner);
                case "10" -> deleteScooter(scanner);
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createScooter(Scanner scanner) {
        System.out.println("\nСоздание самоката");
        List<Model> models = modelRepo.findAll();
        if (models.isEmpty()) {
            System.out.println("Ошибка: нет моделей");
            return;
        }
        System.out.println("Модели:");
        for (Model m : models) {
            System.out.printf("  %d. %s %s%n", m.getId(), m.getBrand(), m.getModel());
        }
        System.out.print("Введите ID модели: ");
        try {
            int modelId = Integer.parseInt(scanner.nextLine().trim());
            Model model = modelRepo.findById(modelId).orElse(null);
            if (model == null) {
                System.out.println("Модель не найдена");
                return;
            }
            List<Zone> zones = zoneRepo.findAll();
            if (zones.isEmpty()) {
                System.out.println("Ошибка: нет зон");
                return;
            }
            System.out.println("Зоны:");
            for (Zone z : zones) {
                System.out.printf("  %d. %s%n", z.getId(), z.getAddress());
            }
            System.out.print("Введите ID зоны: ");
            int zoneId = Integer.parseInt(scanner.nextLine().trim());
            Zone zone = zoneRepo.findById(zoneId).orElse(null);
            if (zone == null) {
                System.out.println("Зона не найдена");
                return;
            }
            System.out.print("Введите заряд (0-100): ");
            int charge = Integer.parseInt(scanner.nextLine().trim());
            if (charge < 0 || charge > 100) {
                System.out.println("Заряд должен быть от 0 до 100");
                return;
            }
            Scooter scooter = new Scooter(model, zone, (short) charge);
            scooterRepo.save(scooter);
            System.out.println("Самокат создан: ID=" + scooter.getId());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findScooterById(Scanner scanner) {
        System.out.println("\nПоиск самоката по ID");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Scooter scooter = scooterRepo.findById(id).orElse(null);
            if (scooter == null) System.out.println("Самокат не найден");
            else System.out.println(scooter);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findAllScooters() {
        System.out.println("\nВсе самокаты");
        List<Scooter> scooters = scooterRepo.findAll();
        if (scooters.isEmpty()) {
            System.out.println("Нет самокатов");
            return;
        }
        System.out.printf("%-5s %-25s %-20s %-10s%n", "ID", "Модель", "Зона", "Заряд");
        for (Scooter s : scooters) {
            System.out.printf("%-5d %-25s %-20s %-10d%%%n",
                    s.getId(),
                    s.getModel() != null ? s.getModel().getBrand() + " " + s.getModel().getModel() : "—",
                    s.getLocation() != null ? s.getLocation().getAddress() : "—",
                    s.getChargeLevel());
        }
    }

    private void findScooterByModel(Scanner scanner) {
        System.out.println("\nПоиск самоката по модели");
        System.out.print("Введите ID модели: ");
        try {
            int modelId = Integer.parseInt(scanner.nextLine().trim());
            List<Scooter> scooters = scooterRepo.findByModelId(modelId);
            if (scooters.isEmpty()) {
                System.out.println("Самокаты не найдены");
                return;
            }
            System.out.printf("%-5s %-10s %-20s%n", "ID", "Заряд", "Зона");
            for (Scooter s : scooters) {
                System.out.printf("%-5d %-10d%% %-20s%n",
                        s.getId(),
                        s.getChargeLevel(),
                        s.getLocation() != null ? s.getLocation().getAddress() : "—");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findScooterByZone(Scanner scanner) {
        System.out.println("\nПоиск самоката по зоне");
        System.out.print("Введите ID зоны: ");
        try {
            int zoneId = Integer.parseInt(scanner.nextLine().trim());
            List<Scooter> scooters = scooterRepo.findByZoneId(zoneId);
            if (scooters.isEmpty()) {
                System.out.println("Самокаты не найдены");
                return;
            }
            System.out.printf("%-5s %-15s %-10s%n", "ID", "Модель", "Заряд");
            for (Scooter s : scooters) {
                System.out.printf("%-5d %-15s %-10d%%%n",
                        s.getId(),
                        s.getModel() != null ? s.getModel().getBrand() + " " + s.getModel().getModel() : "—",
                        s.getChargeLevel());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findScooterByMinCharge(Scanner scanner) {
        System.out.println("\nПоиск самоката с зарядом больше N");
        System.out.print("Введите минимальный заряд: ");
        try {
            int min = Integer.parseInt(scanner.nextLine().trim());
            List<Scooter> scooters = scooterRepo.searchByMinCharge(min);
            if (scooters.isEmpty()) {
                System.out.println("Самокаты не найдены");
                return;
            }
            System.out.printf("%-5s %-15s %-10s%n", "ID", "Модель", "Заряд");
            for (Scooter s : scooters) {
                System.out.printf("%-5d %-15s %-10d%%%n",
                        s.getId(),
                        s.getModel() != null ? s.getModel().getBrand() + " " + s.getModel().getModel() : "—",
                        s.getChargeLevel());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findScooterByModelBrand(Scanner scanner) {
        System.out.println("\nПоиск самоката по бренду модели");
        System.out.print("Введите бренд: ");
        String brand = scanner.nextLine().trim();
        if (brand.isEmpty()) {
            System.out.println("Бренд не может быть пустым");
            return;
        }
        List<Scooter> scooters = scooterRepo.findByModelBrand(brand);
        if (scooters.isEmpty()) {
            System.out.println("Самокаты не найдены");
            return;
        }
        System.out.printf("%-5s %-15s %-10s%n", "ID", "Модель", "Заряд");
        for (Scooter s : scooters) {
            System.out.printf("%-5d %-15s %-10d%%%n",
                    s.getId(),
                    s.getModel() != null ? s.getModel().getBrand() + " " + s.getModel().getModel() : "—",
                    s.getChargeLevel());
        }
    }

    private void findScooterByZoneSorted(Scanner scanner) {
        System.out.println("\nПоиск самоката по зоне (сортировка по заряду)");
        System.out.print("Введите ID зоны: ");
        try {
            int zoneId = Integer.parseInt(scanner.nextLine().trim());
            List<Scooter> scooters = scooterRepo.findByZoneIdSortedByChargeDesc(zoneId);
            if (scooters.isEmpty()) {
                System.out.println("Самокаты не найдены");
                return;
            }
            System.out.printf("%-5s %-10s%n", "ID", "Заряд");
            for (Scooter s : scooters) {
                System.out.printf("%-5d %-10d%%%n", s.getId(), s.getChargeLevel());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void updateScooter(Scanner scanner) {
        System.out.println("\nОбновление самоката");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Scooter scooter = scooterRepo.findById(id).orElse(null);
            if (scooter == null) {
                System.out.println("Самокат не найден");
                return;
            }
            System.out.println("Текущие данные: " + scooter);
            System.out.print("Новый заряд (или Enter для пропуска): ");
            String chargeInput = scanner.nextLine().trim();
            if (!chargeInput.isEmpty()) {
                int charge = Integer.parseInt(chargeInput);
                if (charge >= 0 && charge <= 100) scooter.setChargeLevel((short) charge);
            }
            System.out.print("Новый ID зоны (или Enter для пропуска): ");
            String zoneInput = scanner.nextLine().trim();
            if (!zoneInput.isEmpty()) {
                int zoneId = Integer.parseInt(zoneInput);
                Zone zone = zoneRepo.findById(zoneId).orElse(null);
                if (zone != null) scooter.setLocation(zone);
            }
            scooterRepo.update(scooter);
            System.out.println("Самокат обновлён: " + scooter);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void deleteScooter(Scanner scanner) {
        System.out.println("\nУдаление самоката");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            EntityManager em = HibernateUtil.getEntityManager();
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();

                // Проверяем поездки
                Long tripCount = em.createQuery(
                        "SELECT COUNT(t) FROM Trip t WHERE t.scooter.id = :scooterId",
                        Long.class
                ).setParameter("scooterId", id).getSingleResult();

                if (tripCount > 0) {
                    System.out.println("Ошибка: у самоката есть " + tripCount + " поездок. Удаление невозможно.");
                    tx.rollback();
                    return;
                }

                // Проверяем задачи
                Long taskCount = em.createQuery(
                        "SELECT COUNT(t) FROM Task t WHERE t.scooter.id = :scooterId",
                        Long.class
                ).setParameter("scooterId", id).getSingleResult();

                if (taskCount > 0) {
                    System.out.println("Ошибка: у самоката есть " + taskCount + " задач. Удаление невозможно.");
                    tx.rollback();
                    return;
                }

                // Если связей нет — удаляем
                Scooter scooter = em.find(Scooter.class, id);
                if (scooter != null) {
                    em.remove(scooter);
                    tx.commit();
                    System.out.println("Самокат удалён");
                } else {
                    System.out.println("Самокат не найден");
                    tx.rollback();
                }

            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                System.err.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
            } finally {
                em.close();
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    // Task

    public void manageTasks(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \nУправление задачами (Task)
                    [1] Создать
                    [2] Найти по ID
                    [3] Найти всех
                    [4] Невыполненные
                    [5] Выполненные
                    [6] По самокату
                    [7] По джусеру
                    [8] Обновить
                    [9] Удалить
                    [0] Назад
                    > """);
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createTask(scanner);
                case "2" -> findTaskById(scanner);
                case "3" -> findAllTasks();
                case "4" -> findPendingTasks();
                case "5" -> findCompletedTasks();
                case "6" -> findTasksByScooter(scanner);
                case "7" -> findTasksByJuicer(scanner);
                case "8" -> updateTask(scanner);
                case "9" -> deleteTask(scanner);
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createTask(Scanner scanner) {
        System.out.println("\nСоздание задачи");
        try {
            List<Scooter> scooters = scooterRepo.findAll();
            if (scooters.isEmpty()) {
                System.out.println("Ошибка: нет самокатов");
                return;
            }
            System.out.println("Самокаты:");
            for (Scooter s : scooters) {
                System.out.printf("  %d. %s (заряд: %d%%)%n", s.getId(),
                        s.getModel() != null ? s.getModel().getBrand() + " " + s.getModel().getModel() : "—",
                        s.getChargeLevel());
            }
            System.out.print("Введите ID самоката: ");
            int scooterId = Integer.parseInt(scanner.nextLine().trim());
            Scooter scooter = scooterRepo.findById(scooterId).orElse(null);
            if (scooter == null) {
                System.out.println("Самокат не найден");
                return;
            }

            List<Juicer> juicers = juicerRepo.findAll();
            if (juicers.isEmpty()) {
                System.out.println("Ошибка: нет джусеров");
                return;
            }
            System.out.println("Джусеры:");
            for (Juicer j : juicers) {
                System.out.printf("  %d. %s (%s)%n", j.getId(), j.getFullName(),
                        j.getRole() != null ? j.getRole() : "без роли");
            }
            System.out.print("Введите ID джусера: ");
            int juicerId = Integer.parseInt(scanner.nextLine().trim());
            Juicer juicer = juicerRepo.findById(juicerId).orElse(null);
            if (juicer == null) {
                System.out.println("Джусер не найден");
                return;
            }

            System.out.print("Введите описание: ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Описание не может быть пустым");
                return;
            }

            System.out.print("Задача выполнена? (true/false): ");
            boolean status = Boolean.parseBoolean(scanner.nextLine().trim());

            Task task = new Task(scooter, juicer, LocalDateTime.now(), description, status);
            taskRepo.save(task);
            System.out.println("Задача создана: ID=" + task.getId());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findTaskById(Scanner scanner) {
        System.out.println("\nПоиск задачи по ID");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Task task = taskRepo.findById(id).orElse(null);
            if (task == null) System.out.println("Задача не найдена");
            else System.out.println(task);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findAllTasks() {
        System.out.println("\nВсе задачи");
        List<Task> tasks = taskRepo.findAll();
        if (tasks.isEmpty()) {
            System.out.println("Нет задач");
            return;
        }
        System.out.printf("%-5s %-20s %-20s %-10s %-15s%n", "ID", "Самокат", "Джусер", "Статус", "Описание");
        for (Task t : tasks) {
            System.out.printf("%-5d %-20s %-20s %-10s %-15s%n",
                    t.getId(),
                    t.getScooter() != null ? "SC-" + t.getScooter().getId() : "—",
                    t.getJuicer() != null ? t.getJuicer().getFullName() : "—",
                    t.isStatus() ? "Выполнена" : "В работе",
                    t.getDescription().length() > 15 ? t.getDescription().substring(0, 15) + "..." : t.getDescription());
        }
    }

    private void findPendingTasks() {
        System.out.println("\nНевыполненные задачи");
        List<Task> tasks = taskRepo.findPending();
        if (tasks.isEmpty()) {
            System.out.println("Нет невыполненных задач");
            return;
        }
        System.out.printf("%-5s %-20s %-20s %-15s%n", "ID", "Самокат", "Джусер", "Описание");
        for (Task t : tasks) {
            System.out.printf("%-5d %-20s %-20s %-15s%n",
                    t.getId(),
                    t.getScooter() != null ? "SC-" + t.getScooter().getId() : "—",
                    t.getJuicer() != null ? t.getJuicer().getFullName() : "—",
                    t.getDescription().length() > 15 ? t.getDescription().substring(0, 15) + "..." : t.getDescription());
        }
    }

    private void findCompletedTasks() {
        System.out.println("\nВыполненные задачи");
        List<Task> tasks = taskRepo.findCompleted();
        if (tasks.isEmpty()) {
            System.out.println("Нет выполненных задач");
            return;
        }
        System.out.printf("%-5s %-20s %-20s %-15s%n", "ID", "Самокат", "Джусер", "Описание");
        for (Task t : tasks) {
            System.out.printf("%-5d %-20s %-20s %-15s%n",
                    t.getId(),
                    t.getScooter() != null ? "SC-" + t.getScooter().getId() : "—",
                    t.getJuicer() != null ? t.getJuicer().getFullName() : "—",
                    t.getDescription().length() > 15 ? t.getDescription().substring(0, 15) + "..." : t.getDescription());
        }
    }

    private void findTasksByScooter(Scanner scanner) {
        System.out.println("\nПоиск задач по самокату");
        System.out.print("Введите ID самоката: ");
        try {
            int scooterId = Integer.parseInt(scanner.nextLine().trim());
            List<Task> tasks = taskRepo.findByScooterId(scooterId);
            if (tasks.isEmpty()) {
                System.out.println("Задачи не найдены");
                return;
            }
            System.out.printf("%-5s %-15s %-10s%n", "ID", "Джусер", "Статус");
            for (Task t : tasks) {
                System.out.printf("%-5d %-15s %-10s%n",
                        t.getId(),
                        t.getJuicer() != null ? t.getJuicer().getFullName() : "—",
                        t.isStatus() ? "Выполнена" : "В работе");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findTasksByJuicer(Scanner scanner) {
        System.out.println("\nПоиск задач по джусеру");
        System.out.print("Введите ID джусера: ");
        try {
            int juicerId = Integer.parseInt(scanner.nextLine().trim());
            List<Task> tasks = taskRepo.findByJuicerId(juicerId);
            if (tasks.isEmpty()) {
                System.out.println("Задачи не найдены");
                return;
            }
            System.out.printf("%-5s %-10s %-15s%n", "ID", "Статус", "Описание");
            for (Task t : tasks) {
                System.out.printf("%-5d %-10s %-15s%n",
                        t.getId(),
                        t.isStatus() ? "Выполнена" : "В работе",
                        t.getDescription().length() > 15 ? t.getDescription().substring(0, 15) + "..." : t.getDescription());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void updateTask(Scanner scanner) {
        System.out.println("\nОбновление задачи");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Task task = taskRepo.findById(id).orElse(null);
            if (task == null) {
                System.out.println("Задача не найдена");
                return;
            }
            System.out.println("Текущие данные: " + task);
            System.out.print("Новое описание (или Enter для пропуска): ");
            String desc = scanner.nextLine().trim();
            if (!desc.isEmpty()) task.setDescription(desc);
            System.out.print("Статус (true - выполнена, false - в работе, Enter для пропуска): ");
            String statusInput = scanner.nextLine().trim();
            if (!statusInput.isEmpty()) {
                task.setStatus(Boolean.parseBoolean(statusInput));
            }
            taskRepo.update(task);
            System.out.println("Задача обновлена: " + task);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void deleteTask(Scanner scanner) {
        System.out.println("\nУдаление задачи");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            taskRepo.deleteById(id);
            System.out.println("Задача удалена");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    // User

    public void manageUsers(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \nУправление пользователями (User)
                    [1] Создать
                    [2] Найти по ID
                    [3] Найти всех
                    [4] Поиск по имени
                    [5] С тарифом
                    [6] Без тарифа
                    [7] По тарифу
                    [8] Самый активный
                    [9] Топ по тратам
                    [10] Обновить
                    [11] Удалить
                    [0] Назад
                    > """);
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createUser(scanner);
                case "2" -> findUserById(scanner);
                case "3" -> findAllUsers();
                case "4" -> searchUserByName(scanner);
                case "5" -> findUsersWithTariff();
                case "6" -> findUsersWithoutTariff();
                case "7" -> findUsersByTariff(scanner);
                case "8" -> findMostActiveUser();
                case "9" -> findTopSpender();
                case "10" -> updateUser(scanner);
                case "11" -> deleteUser(scanner);
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createUser(Scanner scanner) {
        System.out.println("\nСоздание пользователя");
        System.out.print("Введите полное имя: ");
        String fullName = scanner.nextLine().trim();
        if (fullName.isEmpty()) {
            System.out.println("Ошибка: имя не может быть пустым");
            return;
        }

        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            TypedQuery<Tariff> tariffQuery = em.createQuery("FROM Tariff", Tariff.class);
            List<Tariff> tariffs = tariffQuery.getResultList();

            Tariff selectedTariff = null;

            if (tariffs.isEmpty()) {
                System.out.println("Внимание: нет доступных тарифов. Пользователь будет создан без тарифа.");
            } else {
                System.out.println("\nДоступные тарифы:");
                for (Tariff t : tariffs) {
                    System.out.printf("  %d. %s (%.2f руб/мин)%n", t.getId(), t.getName(), t.getPricePerMinute());
                }
                System.out.println("  0. Без тарифа");
                System.out.print("Выберите ID тарифа (или 0 для пропуска): ");

                try {
                    int tariffId = Integer.parseInt(scanner.nextLine().trim());
                    if (tariffId > 0) {
                        selectedTariff = em.find(Tariff.class, tariffId);
                        if (selectedTariff == null) {
                            System.out.println("Тариф не найден, пользователь будет создан без тарифа");
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите корректное число. Пользователь будет создан без тарифа");
                }
            }

            User user = new User(fullName, selectedTariff);
            em.persist(user);
            tx.commit();

            System.out.printf("Пользователь создан: ID=%d, Имя='%s', Тариф=%s%n",
                    user.getId(),
                    user.getFullName(),
                    user.getTariff() != null ? user.getTariff().getName() : "отсутствует");

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void findUserById(Scanner scanner) {
        System.out.println("\nПоиск пользователя по ID");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            User user = userRepo.findById(id).orElse(null);
            if (user == null) System.out.println("Пользователь не найден");
            else System.out.println(user);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findAllUsers() {
        System.out.println("\nВсе пользователи");
        List<User> users = userRepo.findAll();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей");
            return;
        }
        System.out.printf("%-5s %-25s %-15s%n", "ID", "Имя", "Тариф");
        for (User u : users) {
            System.out.printf("%-5d %-25s %-15s%n",
                    u.getId(),
                    u.getFullName(),
                    u.getTariff() != null ? u.getTariff().getName() : "—");
        }
    }

    private void searchUserByName(Scanner scanner) {
        System.out.println("\nПоиск пользователя по части имени");
        System.out.print("Введите часть имени: ");
        String namePart = scanner.nextLine().trim();
        if (namePart.isEmpty()) {
            System.out.println("Имя не может быть пустым");
            return;
        }
        List<User> users = userRepo.searchByName(namePart);
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены");
            return;
        }
        System.out.printf("%-5s %-25s %-15s%n", "ID", "Имя", "Тариф");
        for (User u : users) {
            System.out.printf("%-5d %-25s %-15s%n",
                    u.getId(),
                    u.getFullName(),
                    u.getTariff() != null ? u.getTariff().getName() : "—");
        }
    }

    private void findUsersWithTariff() {
        System.out.println("\nПользователи с тарифом");
        List<User> users = userRepo.findAllWithTariff();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей с тарифом");
            return;
        }
        System.out.printf("%-5s %-25s %-15s%n", "ID", "Имя", "Тариф");
        for (User u : users) {
            System.out.printf("%-5d %-25s %-15s%n",
                    u.getId(),
                    u.getFullName(),
                    u.getTariff() != null ? u.getTariff().getName() : "—");
        }
    }

    private void findUsersWithoutTariff() {
        System.out.println("\nПользователи без тарифа");
        List<User> users = userRepo.findAllWithoutTariff();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей без тарифа");
            return;
        }
        System.out.printf("%-5s %-25s%n", "ID", "Имя");
        for (User u : users) {
            System.out.printf("%-5d %-25s%n", u.getId(), u.getFullName());
        }
    }

    private void findUsersByTariff(Scanner scanner) {
        System.out.println("\nПоиск пользователей по тарифу");
        System.out.print("Введите ID тарифа: ");
        try {
            int tariffId = Integer.parseInt(scanner.nextLine().trim());
            List<User> users = userRepo.findByTariffId(tariffId);
            if (users.isEmpty()) {
                System.out.println("Пользователи не найдены");
                return;
            }
            System.out.printf("%-5s %-25s%n", "ID", "Имя");
            for (User u : users) {
                System.out.printf("%-5d %-25s%n", u.getId(), u.getFullName());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findMostActiveUser() {
        System.out.println("\nСамый активный пользователь");
        try {
            User user = userRepo.findMostActiveUser();
            System.out.println(user);
        } catch (Exception e) {
            System.out.println("Нет пользователей");
        }
    }

    private void findTopSpender() {
        System.out.println("\nТоп по тратам");
        try {
            User user = userRepo.findTopSpender();
            if (user == null) {
                System.out.println("Нет данных");
                return;
            }
            System.out.println(user);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void updateUser(Scanner scanner) {
        System.out.println("\nОбновление пользователя");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            User user = userRepo.findById(id).orElse(null);
            if (user == null) {
                System.out.println("Пользователь не найден");
                return;
            }
            System.out.println("Текущие данные: " + user);
            System.out.print("Новое имя (или Enter для пропуска): ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) user.setFullName(newName);
            System.out.print("Новый ID тарифа (или 0 для пропуска): ");
            try {
                int tariffId = Integer.parseInt(scanner.nextLine().trim());
                if (tariffId > 0) {
                    Tariff tariff = tariffRepo.findById(tariffId).orElse(null);
                    user.setTariff(tariff);
                }
            } catch (NumberFormatException e) {}

            userRepo.update(user);
            System.out.println("Пользователь обновлён: " + user);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void deleteUser(Scanner scanner) {
        System.out.println("\nУдаление пользователя");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            List<Trip> trips = tripRepo.findByUserId(id);
            if (!trips.isEmpty()) {
                System.out.println("Ошибка: у пользователя есть " + trips.size() + " поездок");
                return;
            }
            userRepo.deleteById(id);
            System.out.println("Пользователь удалён");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    // Trip

    public void manageTrips(Scanner scanner) {
        while (true) {
            System.out.print("""
                    \nУправление поездками (Trip)
                    [1] Создать
                    [2] Найти по ID
                    [3] Найти всех
                    [4] Поиск по пользователю
                    [5] Поиск по самокату
                    [6] Поиск по маршруту
                    [7] Самая дорогая
                    [8] Самая дешёвая
                    [9] Самая длительная
                    [10] Обновить
                    [11] Удалить
                    [0] Назад
                    >""");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createTrip(scanner);
                case "2" -> findTripById(scanner);
                case "3" -> findAllTrips();
                case "4" -> findTripsByUser(scanner);
                case "5" -> findTripsByScooter(scanner);
                case "6" -> findTripsByRoute(scanner);
                case "7" -> findMostExpensiveTrip();
                case "8" -> findCheapestTrip();
                case "9" -> findLongestTrip();
                case "10" -> updateTrip(scanner);
                case "11" -> deleteTrip(scanner);
                case "0" -> { return; }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createTrip(Scanner scanner) {
        System.out.println("\nСоздание поездки");
        try {
            List<User> users = userRepo.findAll();
            if (users.isEmpty()) {
                System.out.println("Ошибка: нет пользователей");
                return;
            }
            System.out.println("Пользователи:");
            for (User u : users) {
                System.out.printf("  %d. %s%n", u.getId(), u.getFullName());
            }
            System.out.print("Введите ID пользователя: ");
            int userId = Integer.parseInt(scanner.nextLine().trim());
            User user = userRepo.findById(userId).orElse(null);
            if (user == null) {
                System.out.println("Пользователь не найден");
                return;
            }

            List<Scooter> scooters = scooterRepo.findAll();
            if (scooters.isEmpty()) {
                System.out.println("Ошибка: нет самокатов");
                return;
            }
            System.out.println("Самокаты:");
            for (Scooter s : scooters) {
                System.out.printf("  %d. %s (заряд: %d%%)%n", s.getId(),
                        s.getModel() != null ? s.getModel().getBrand() + " " + s.getModel().getModel() : "—",
                        s.getChargeLevel());
            }
            System.out.print("Введите ID самоката: ");
            int scooterId = Integer.parseInt(scanner.nextLine().trim());
            Scooter scooter = scooterRepo.findById(scooterId).orElse(null);
            if (scooter == null) {
                System.out.println("Самокат не найден");
                return;
            }

            List<Route> routes = routeRepo.findAll();
            if (routes.isEmpty()) {
                System.out.println("Ошибка: нет маршрутов");
                return;
            }
            System.out.println("Маршруты:");
            for (Route r : routes) {
                System.out.printf("  %d. %s → %s (%d км)%n", r.getId(),
                        r.getStartZone() != null ? r.getStartZone().getAddress() : "—",
                        r.getEndZone() != null ? r.getEndZone().getAddress() : "—",
                        r.getDistance());
            }
            System.out.print("Введите ID маршрута: ");
            int routeId = Integer.parseInt(scanner.nextLine().trim());
            Route route = routeRepo.findById(routeId).orElse(null);
            if (route == null) {
                System.out.println("Маршрут не найден");
                return;
            }

            System.out.print("Введите длительность (секунды): ");
            int duration = Integer.parseInt(scanner.nextLine().trim());
            if (duration <= 0) {
                System.out.println("Длительность должна быть положительной");
                return;
            }
            System.out.print("Введите стоимость: ");
            BigDecimal cost = new BigDecimal(scanner.nextLine().trim());
            if (cost.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Стоимость должна быть положительной");
                return;
            }

            Trip trip = new Trip(scooter, user, route, LocalDateTime.now(), duration, cost);
            tripRepo.save(trip);
            System.out.println("Поездка создана: ID=" + trip.getId());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findTripById(Scanner scanner) {
        System.out.println("\nПоиск поездки по ID");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Trip trip = tripRepo.findById(id).orElse(null);
            if (trip == null) System.out.println("Поездка не найдена");
            else System.out.println(trip);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findAllTrips() {
        System.out.println("\nВсе поездки");
        List<Trip> trips = tripRepo.findAll();
        if (trips.isEmpty()) {
            System.out.println("Нет поездок");
            return;
        }
        System.out.printf("%-5s %-15s %-15s %-15s %-10s%n", "ID", "Пользователь", "Самокат", "Стоимость", "Длит.");
        for (Trip t : trips) {
            System.out.printf("%-5d %-15s %-15s %-15.2f %-10d%n",
                    t.getId(),
                    t.getUser() != null ? t.getUser().getFullName() : "—",
                    t.getScooter() != null ? "SC-" + t.getScooter().getId() : "—",
                    t.getCost(),
                    t.getDuration());
        }
    }

    private void findTripsByUser(Scanner scanner) {
        System.out.println("\nПоиск поездок по пользователю");
        System.out.print("Введите ID пользователя: ");
        try {
            int userId = Integer.parseInt(scanner.nextLine().trim());
            List<Trip> trips = tripRepo.findByUserId(userId);
            if (trips.isEmpty()) {
                System.out.println("Поездки не найдены");
                return;
            }
            System.out.printf("%-5s %-20s %-10s%n", "ID", "Самокат", "Стоимость");
            for (Trip t : trips) {
                System.out.printf("%-5d %-20s %-10.2f%n",
                        t.getId(),
                        t.getScooter() != null ? "SC-" + t.getScooter().getId() : "—",
                        t.getCost());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findTripsByScooter(Scanner scanner) {
        System.out.println("\nПоиск поездок по самокату");
        System.out.print("Введите ID самоката: ");
        try {
            int scooterId = Integer.parseInt(scanner.nextLine().trim());
            List<Trip> trips = tripRepo.findByScooterId(scooterId);
            if (trips.isEmpty()) {
                System.out.println("Поездки не найдены");
                return;
            }
            System.out.printf("%-5s %-20s %-10s%n", "ID", "Пользователь", "Стоимость");
            for (Trip t : trips) {
                System.out.printf("%-5d %-20s %-10.2f%n",
                        t.getId(),
                        t.getUser() != null ? t.getUser().getFullName() : "—",
                        t.getCost());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findTripsByRoute(Scanner scanner) {
        System.out.println("\nПоиск поездок по маршруту");
        System.out.print("Введите ID маршрута: ");
        try {
            int routeId = Integer.parseInt(scanner.nextLine().trim());
            List<Trip> trips = tripRepo.findByRouteId(routeId);
            if (trips.isEmpty()) {
                System.out.println("Поездки не найдены");
                return;
            }
            System.out.printf("%-5s %-20s %-10s%n", "ID", "Пользователь", "Стоимость");
            for (Trip t : trips) {
                System.out.printf("%-5d %-20s %-10.2f%n",
                        t.getId(),
                        t.getUser() != null ? t.getUser().getFullName() : "—",
                        t.getCost());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void findMostExpensiveTrip() {
        System.out.println("\nСамая дорогая поездка");
        try {
            Trip trip = tripRepo.findMostExpensiveTrip();
            System.out.println(trip);
        } catch (Exception e) {
            System.out.println("Нет поездок");
        }
    }

    private void findCheapestTrip() {
        System.out.println("\nСамая дешёвая поездка");
        try {
            Trip trip = tripRepo.findCheapestTrip();
            System.out.println(trip);
        } catch (Exception e) {
            System.out.println("Нет поездок");
        }
    }

    private void findLongestTrip() {
        System.out.println("\nСамая длительная поездка");
        try {
            Trip trip = tripRepo.findLongestTrip();
            System.out.println(trip);
        } catch (Exception e) {
            System.out.println("Нет поездок");
        }
    }

    private void updateTrip(Scanner scanner) {
        System.out.println("\nОбновление поездки");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Trip trip = tripRepo.findById(id).orElse(null);
            if (trip == null) {
                System.out.println("Поездка не найдена");
                return;
            }
            System.out.println("Текущие данные: " + trip);
            System.out.print("Новая длительность (или Enter для пропуска): ");
            String durInput = scanner.nextLine().trim();
            if (!durInput.isEmpty()) {
                trip.setDuration(Integer.parseInt(durInput));
            }
            System.out.print("Новая стоимость (или Enter для пропуска): ");
            String costInput = scanner.nextLine().trim();
            if (!costInput.isEmpty()) {
                trip.setCost(new BigDecimal(costInput));
            }
            tripRepo.update(trip);
            System.out.println("Поездка обновлена: " + trip);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }

    private void deleteTrip(Scanner scanner) {
        System.out.println("\nУдаление поездки");
        System.out.print("Введите ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            tripRepo.deleteById(id);
            System.out.println("Поездка удалена");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
        }
    }
}