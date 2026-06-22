package com.scooters_rent.repository;

import com.scooters_rent.model.Model;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ModelRepository extends GenericRepository<Model, Integer> {

    public ModelRepository() {
        super(Model.class);
    }

    // Поиск по бренду частично
    public List<Model> searchByBrand(String brand) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Model> query = em.createQuery(
                    "FROM Model m WHERE LOWER(m.brand) LIKE LOWER(:brand)", Model.class
            );
            query.setParameter("brand", "%" + brand + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Поиск по модели частично
    public List<Model> searchByModel(String model) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Model> query = em.createQuery(
                    "FROM Model m WHERE LOWER(m.model) LIKE LOWER(:model)", Model.class
            );
            query.setParameter("model", "%" + model + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Поиск по поколению точно
    public List<Model> searchByGeneration(Integer generation) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Model> query = em.createQuery(
                    "FROM Model m WHERE m.generation = :generation", Model.class
            );
            query.setParameter("generation", generation);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}