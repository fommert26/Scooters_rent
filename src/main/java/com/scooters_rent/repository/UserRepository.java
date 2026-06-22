package com.scooters_rent.repository;

import com.scooters_rent.model.User;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserRepository extends GenericRepository<User, Integer> {

    public UserRepository() {
        super(User.class);
    }

    // Поиск по части имени
    public List<User> searchByName(String namePart) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "FROM User u WHERE LOWER(u.fullName) LIKE LOWER(:namePart)", User.class
            );
            query.setParameter("namePart", "%" + namePart + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти по точному ID тарифа
    public List<User> findByTariffId(int tariffId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "FROM User u WHERE u.tariff.id = :tariffId", User.class
            );
            query.setParameter("tariffId", tariffId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти всех пользователей с тарифом
    public List<User> findAllWithTariff() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "FROM User u WHERE u.tariff IS NOT NULL", User.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти всех пользователей без тарифа
    public List<User> findAllWithoutTariff() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "FROM User u WHERE u.tariff IS NULL", User.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти пользователя с наибольшим количеством поездок
    public User findMostActiveUser() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u ORDER BY SIZE(u.trips) DESC", User.class
            );
            query.setMaxResults(1);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Найти пользователя с максимальной суммой потраченных средств
    public User findTopSpender() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT t.user.id, SUM(t.cost) FROM Trip t GROUP BY t.user.id ORDER BY SUM(t.cost) DESC",
                    Object[].class
            );
            query.setMaxResults(1);
            Object[] result = query.getSingleResult();
            if (result == null || result[0] == null) {
                return null;
            }
            int userId = (Integer) result[0];
            return findById(userId).orElse(null);
        } finally {
            em.close();
        }
    }
}