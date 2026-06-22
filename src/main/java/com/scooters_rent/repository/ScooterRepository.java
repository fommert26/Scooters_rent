package com.scooters_rent.repository;

import com.scooters_rent.model.Scooter;
import com.scooters_rent.model.Model;
import com.scooters_rent.model.Zone;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ScooterRepository extends GenericRepository<Scooter, Integer> {

    public ScooterRepository() {
        super(Scooter.class);
    }

    // Найти по ID модели
    public List<Scooter> findByModelId(int modelId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Scooter> query = em.createQuery(
                    "FROM Scooter s WHERE s.model.id = :modelId", Scooter.class
            );
            query.setParameter("modelId", modelId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти по ID зоны
    public List<Scooter> findByZoneId(int zoneId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Scooter> query = em.createQuery(
                    "FROM Scooter s WHERE s.location.id = :zoneId", Scooter.class
            );
            query.setParameter("zoneId", zoneId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Поиск с зарядом больше N
    public List<Scooter> searchByMinCharge(int minCharge) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Scooter> query = em.createQuery(
                    "FROM Scooter s WHERE s.chargeLevel > :minCharge", Scooter.class
            );
            query.setParameter("minCharge", (short) minCharge);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти по бренду модели
    public List<Scooter> findByModelBrand(String brand) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Scooter> query = em.createQuery(
                    "FROM Scooter s WHERE LOWER(s.model.brand) = LOWER(:brand)", Scooter.class
            );
            query.setParameter("brand", brand);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти все самокаты в зоне, отсортированные по заряду (от высокого к низкому)
    public List<Scooter> findByZoneIdSortedByChargeDesc(int zoneId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Scooter> query = em.createQuery(
                    "FROM Scooter s WHERE s.location.id = :zoneId ORDER BY s.chargeLevel DESC",
                    Scooter.class
            );
            query.setParameter("zoneId", zoneId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}