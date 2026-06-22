package com.scooters_rent.repository;

import com.scooters_rent.model.Juicer;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class JuicerRepository extends GenericRepository<Juicer, Integer> {

    public JuicerRepository() {
        super(Juicer.class);
    }

    // Найти по роли
    public List<Juicer> findByRole(String role) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Juicer> query = em.createQuery(
                    "FROM Juicer j WHERE j.role = :role", Juicer.class
            );
            query.setParameter("role", role);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Найти по имени
    public List<Juicer> findByFullNameContaining(String namePart) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Juicer> query = em.createQuery(
                    "FROM Juicer j WHERE LOWER(j.fullName) LIKE LOWER(:namePart)",
                    Juicer.class
            );
            query.setParameter("namePart", "%" + namePart + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
