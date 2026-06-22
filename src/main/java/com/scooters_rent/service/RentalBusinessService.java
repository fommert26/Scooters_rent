package com.scooters_rent.service;

import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;

public class RentalBusinessService {

    // 1. Выручка по зонам
    public void revenueByZone() {
        System.out.println("1. Выручка по зонам");

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT z.address, COALESCE(SUM(t.cost), 0) " +
                            "FROM Zone z " +
                            "LEFT JOIN Route r ON z.id = r.startZone.id " +
                            "LEFT JOIN Trip t ON r.id = t.route.id " +
                            "GROUP BY z.id, z.address " +
                            "ORDER BY COALESCE(SUM(t.cost), 0) DESC",
                    Object[].class
            );

            List<Object[]> results = query.getResultList();
            System.out.printf("%-30s %-15s%n", "Зона", "Выручка");
            for (Object[] row : results) {
                System.out.printf("%-30s %-15.2f%n",
                        row[0],
                        ((BigDecimal) row[1]).doubleValue());
            }
        } finally {
            em.close();
        }
        System.out.println();
    }

    // 2. Самая частотная модель в ремонте
    public void mostFrequentModelInRepair() {
        System.out.println("2. Самая частотная модель в ремонте");

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> maxQuery = em.createQuery(
                    "SELECT COUNT(tk) FROM Task tk GROUP BY tk.scooter.model.id ORDER BY COUNT(tk) DESC",
                    Long.class
            );
            maxQuery.setMaxResults(1);
            Long maxCount = maxQuery.getResultList().isEmpty() ? 0 : maxQuery.getSingleResult();

            if (maxCount == 0) {
                System.out.println("Нет данных о ремонтах");
                return;
            }

            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT CONCAT(m.brand, ' ', m.model), COUNT(tk) " +
                            "FROM Task tk " +
                            "JOIN tk.scooter s " +
                            "JOIN s.model m " +
                            "GROUP BY m.id, m.brand, m.model " +
                            "HAVING COUNT(tk) = :maxCount " +
                            "ORDER BY m.brand, m.model",
                    Object[].class
            );
            query.setParameter("maxCount", maxCount);

            List<Object[]> results = query.getResultList();
            System.out.printf("%-30s %-15s%n", "Модель", "Ремонтов");
            for (Object[] row : results) {
                System.out.printf("%-30s %-15d%n", row[0], (Long) row[1]);
            }
        } finally {
            em.close();
        }
        System.out.println();
    }

    // 3. Самый активный джусер
    public void mostActiveJuicer() {
        System.out.println("3. Самый активный джусер");

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> maxQuery = em.createQuery(
                    "SELECT COUNT(tk) FROM Task tk GROUP BY tk.juicer.id ORDER BY COUNT(tk) DESC",
                    Long.class
            );
            maxQuery.setMaxResults(1);
            Long maxCount = maxQuery.getResultList().isEmpty() ? 0 : maxQuery.getSingleResult();

            if (maxCount == 0) {
                System.out.println("Нет данных о задачах");
                return;
            }

            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT j.fullName, COUNT(tk) " +
                            "FROM Task tk " +
                            "JOIN tk.juicer j " +
                            "GROUP BY j.id, j.fullName " +
                            "HAVING COUNT(tk) = :maxCount " +
                            "ORDER BY j.fullName",
                    Object[].class
            );
            query.setParameter("maxCount", maxCount);

            List<Object[]> results = query.getResultList();
            System.out.printf("%-30s %-15s%n", "Джусер", "Задач");
            for (Object[] row : results) {
                System.out.printf("%-30s %-15d%n", row[0], (Long) row[1]);
            }
        } finally {
            em.close();
        }
        System.out.println();
    }

    // 4. Самый активный пользователь
    public void mostActiveUser() {
        System.out.println("4. Самый активный пользователь");

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> maxQuery = em.createQuery(
                    "SELECT COUNT(t) FROM Trip t GROUP BY t.user.id ORDER BY COUNT(t) DESC",
                    Long.class
            );
            maxQuery.setMaxResults(1);
            Long maxCount = maxQuery.getResultList().isEmpty() ? 0 : maxQuery.getSingleResult();

            if (maxCount == 0) {
                System.out.println("Нет данных о поездках");
                return;
            }

            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT u.fullName, COUNT(t) " +
                            "FROM Trip t " +
                            "JOIN t.user u " +
                            "GROUP BY u.id, u.fullName " +
                            "HAVING COUNT(t) = :maxCount " +
                            "ORDER BY u.fullName",
                    Object[].class
            );
            query.setParameter("maxCount", maxCount);

            List<Object[]> results = query.getResultList();
            System.out.printf("%-30s %-15s%n", "Пользователь", "Поездок");
            for (Object[] row : results) {
                System.out.printf("%-30s %-15d%n", row[0], (Long) row[1]);
            }
        } finally {
            em.close();
        }
        System.out.println();
    }

    // 5. Сводка по тарифам
    public void tariffSummary() {
        System.out.println("5. Сводка по тарифам");

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT t.name, t.pricePerMinute, COUNT(DISTINCT u), COALESCE(AVG(tr.cost), 0) " +
                            "FROM Tariff t " +
                            "LEFT JOIN User u ON u.tariff.id = t.id " +
                            "LEFT JOIN Trip tr ON tr.user.id = u.id " +
                            "GROUP BY t.id, t.name, t.pricePerMinute " +
                            "ORDER BY t.pricePerMinute",
                    Object[].class
            );

            List<Object[]> results = query.getResultList();
            System.out.printf("%-25s %-15s %-10s %-15s%n",
                    "Тариф", "Цена/мин", "Пользователей", "Средний чек");
            for (Object[] row : results) {
                String tariffName = (String) row[0];
                BigDecimal price = (BigDecimal) row[1];
                Long userCount = (Long) row[2];

                // COALESCE(AVG(tr.cost), 0) возвращает Double
                Double avgCost = (Double) row[3];

                System.out.printf("%-25s %-15.2f %-10d %-15.2f%n",
                        tariffName,
                        price.doubleValue(),
                        userCount,
                        avgCost);
            }
        } finally {
            em.close();
        }
        System.out.println();
    }

    // 6. Средняя стоимость поездок
    public void averageTripCost() {
        System.out.println("6. Средняя стоимость поездок");

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT COALESCE(AVG(t.cost), 0) FROM Trip t",
                    Double.class
            );

            Double avg = query.getSingleResult();
            System.out.printf("Средняя стоимость поездки: %.2f%n", avg);
        } finally {
            em.close();
        }
        System.out.println();
    }

    // 7. Средняя продолжительность поездок
    public void averageTripDuration() {
        System.out.println("7. Средняя продолжительность поездок");

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT COALESCE(AVG(t.duration) / 60.0, 0) FROM Trip t",
                    Double.class
            );

            Double avgMinutes = query.getSingleResult();
            System.out.printf("Средняя продолжительность: %.1f минут%n", avgMinutes);
        } finally {
            em.close();
        }
        System.out.println();
    }

    // 8. Топ 3 непопулярных самоката
    public void top3UnpopularScooters() {
        System.out.println("8. Топ 3 непопулярных самоката");

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT s.id, CONCAT(m.brand, ' ', m.model), COALESCE(COUNT(t), 0) " +
                            "FROM Scooter s " +
                            "JOIN s.model m " +
                            "LEFT JOIN Trip t ON t.scooter.id = s.id " +
                            "GROUP BY s.id, m.brand, m.model " +
                            "ORDER BY COALESCE(COUNT(t), 0) ASC",
                    Object[].class
            );
            query.setMaxResults(3);

            List<Object[]> results = query.getResultList();
            System.out.printf("%-5s %-25s %-10s%n", "ID", "Модель", "Поездок");
            for (Object[] row : results) {
                System.out.printf("%-5d %-25s %-10d%n",
                        (Integer) row[0],
                        row[1],
                        (Long) row[2]);
            }
        } finally {
            em.close();
        }
        System.out.println();
    }

    public void runAll() {
        revenueByZone();
        mostFrequentModelInRepair();
        mostActiveJuicer();
        mostActiveUser();
        tariffSummary();
        averageTripCost();
        averageTripDuration();
        top3UnpopularScooters();
    }
}
