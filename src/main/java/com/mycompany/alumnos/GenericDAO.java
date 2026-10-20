package com.mycompany.alumnos;

import jakarta.persistence.EntityManager;
import java.util.List;

public class GenericDAO<T> {

    private final Class<T> clase;

    public GenericDAO(Class<T> clase) {
        this.clase = clase;
    }

    public List<T> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM " + clase.getSimpleName() + " e", clase)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public T find(Object id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(clase, id);
        } finally {
            em.close();
        }
    }

    public void save(T entidad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entidad);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Object id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            T entidad = em.find(clase, id);
            if (entidad != null) em.remove(entidad);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}