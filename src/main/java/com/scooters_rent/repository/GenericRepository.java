package com.scooters_rent.repository;

import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public abstract class GenericRepository<T, ID> {

    protected final Class<T> entityClass;

    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // Сохранить
    public T save(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Ошибка при сохранении: " + entityClass.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    // Найти по ID
    public Optional<T> findById(ID id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } finally {
            em.close();
        }
    }

    // Найти все
    public List<T> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<T> query = em.createQuery("FROM " + entityClass.getSimpleName(), entityClass);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Обновить
    public T update(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Ошибка при обновлении: " + entityClass.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    // Удалить по ID
    public void deleteById(ID id) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Ошибка при удалении: " + entityClass.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    // Количество записей
    public long count() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e",
                    Long.class
            );
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Проверить, существует ли запись
    public boolean existsById(ID id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            T entity = em.find(entityClass, id);
            return entity != null;
        } finally {
            em.close();
        }
    }
}