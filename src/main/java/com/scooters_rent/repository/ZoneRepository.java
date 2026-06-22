package com.scooters_rent.repository;

import com.scooters_rent.model.Zone;
import com.scooters_rent.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ZoneRepository extends GenericRepository<Zone, Integer>{
    public ZoneRepository(){
        super(Zone.class);
    }

    // Поиск по адресу частично
    public List<Zone> searchByAddress(String addressPart) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Zone> query = em.createQuery(
                    "FROM Zone z WHERE LOWER(z.address) LIKE LOWER(:address)", Zone.class
            );
            query.setParameter("address", "%" + addressPart + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    //Поиск по доступности парковки
    public List<Zone> findByParkingAvailable(boolean parkingAvailable) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Zone> query = em.createQuery(
                    "FROM Zone z WHERE z.parkingAvailable = :parkingAvailable", Zone.class
            );
            query.setParameter("parkingAvailable", parkingAvailable);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Все зоны с парковкой
    public List<Zone> findAllWithParking() {
        return findByParkingAvailable(true);
    }

    // Все зоны без парковки
    public List<Zone> findAllWithoutParking() {
        return findByParkingAvailable(false);
    }
}
