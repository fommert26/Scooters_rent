package com.scooters_rent.repository;

import com.scooters_rent.model.Tariff;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TariffRepository extends GenericRepository<Tariff, Integer> {

    public TariffRepository() {
        super(Tariff.class);
    }

    // Найти по названию (частичное совпадение)
    public List<Tariff> findByName(String name) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Tariff> query = em.createQuery(
                    "FROM Tariff t WHERE LOWER(t.name) LIKE LOWER(:name)", Tariff.class
            );
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти самый дешёвый тариф
    public Tariff findCheapest() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Tariff> query = em.createQuery(
                    "FROM Tariff t ORDER BY t.pricePerMinute ASC", Tariff.class
            );
            query.setMaxResults(1);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Найти самый дорогой тариф
    public Tariff findMostExpensive() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Tariff> query = em.createQuery(
                    "FROM Tariff t ORDER BY t.pricePerMinute DESC", Tariff.class
            );
            query.setMaxResults(1);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
