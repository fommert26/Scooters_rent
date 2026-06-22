package com.scooters_rent.repository;

import com.scooters_rent.model.Task;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TaskRepository extends GenericRepository<Task, Integer> {

    public TaskRepository() {
        super(Task.class);
    }

    // Найти все задачи по статусу (выполнена/не выполнена)
    public List<Task> findByStatus(boolean status) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery(
                    "FROM Task t WHERE t.status = :status", Task.class
            );
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти все невыполненные задачи
    public List<Task> findPending() {
        return findByStatus(false);
    }

    // Найти все выполненные задачи
    public List<Task> findCompleted() {
        return findByStatus(true);
    }

    // Найти задачи по самокату
    public List<Task> findByScooterId(int scooterId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery(
                    "FROM Task t WHERE t.scooter.id = :scooterId", Task.class
            );
            query.setParameter("scooterId", scooterId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти задачи по исполнителю
    public List<Task> findByJuicerId(int juicerId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery(
                    "FROM Task t WHERE t.juicer.id = :juicerId", Task.class
            );
            query.setParameter("juicerId", juicerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
