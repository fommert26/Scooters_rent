package com.scooters_rent.repository;

import com.scooters_rent.model.Route;
import com.scooters_rent.model.Zone;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class RouteRepository extends GenericRepository<Route, Integer> {

    public RouteRepository() {
        super(Route.class);
    }

    // Найти маршрут между двумя зонами
    public List<Route> findByZones(int startZoneId, int endZoneId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Route> query = em.createQuery(
                    "FROM Route r WHERE r.startZone.id = :startZoneId " +
                            "AND r.endZone.id = :endZoneId", Route.class
            );
            query.setParameter("startZoneId", startZoneId);
            query.setParameter("endZoneId", endZoneId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти маршруты по ID начальной зоны
    public List<Route> findByStartZoneId(int startZoneId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Route> query = em.createQuery(
                    "FROM Route r WHERE r.startZone.id = :startZoneId", Route.class
            );
            query.setParameter("startZoneId", startZoneId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    // Найти маршруты по ID конечной зоны
    public List<Route> findByEndZoneId(int endZoneId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Route> query = em.createQuery(
                    "FROM Route r WHERE r.endZone.id = :endZoneId", Route.class
            );
            query.setParameter("endZoneId", endZoneId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    // Найти маршруты по диапазону расстояния
    public List<Route> findByDistanceBetween(int minDistance, int maxDistance) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Route> query = em.createQuery(
                    "FROM Route r WHERE r.distance BETWEEN :minDistance AND :maxDistance",
                    Route.class
            );
            query.setParameter("minDistance", (short) minDistance);
            query.setParameter("maxDistance", (short) maxDistance);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти самый короткий маршрут
    public Route findShortest() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Route> query = em.createQuery(
                    "FROM Route r ORDER BY r.distance ASC", Route.class
            );
            query.setMaxResults(1);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Найти самый длинный маршрут
    public Route findLongest() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Route> query = em.createQuery(
                    "FROM Route r ORDER BY r.distance DESC", Route.class
            );
            query.setMaxResults(1);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}