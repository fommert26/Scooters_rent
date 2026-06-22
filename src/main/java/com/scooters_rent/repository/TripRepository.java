package com.scooters_rent.repository;

import com.scooters_rent.model.Trip;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TripRepository extends GenericRepository<Trip, Integer> {

    public TripRepository() {
        super(Trip.class);
    }

    // Найти по ID пользователя
    public List<Trip> findByUserId(int userId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery(
                    "FROM Trip t WHERE t.user.id = :userId", Trip.class
            );
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти по ID самоката
    public List<Trip> findByScooterId(int scooterId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery(
                    "FROM Trip t WHERE t.scooter.id = :scooterId", Trip.class
            );
            query.setParameter("scooterId", scooterId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти по ID маршрута
    public List<Trip> findByRouteId(int routeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery(
                    "FROM Trip t WHERE t.route.id = :routeId", Trip.class
            );
            query.setParameter("routeId", routeId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }



    // Найти самую дорогую поездку
    public Trip findMostExpensiveTrip() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery(
                    "FROM Trip t ORDER BY t.cost DESC", Trip.class
            );
            query.setMaxResults(1);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Найти самую дешёвую поездку
    public Trip findCheapestTrip() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery(
                    "FROM Trip t ORDER BY t.cost ASC", Trip.class
            );
            query.setMaxResults(1);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Найти самую длительную поездку
    public Trip findLongestTrip() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery(
                    "FROM Trip t ORDER BY t.duration DESC", Trip.class
            );
            query.setMaxResults(1);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}