package com.mycompany.alumnos.sesiones;

import jakarta.persistence.EntityManager;
import java.util.List;

public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) { this.entityClass = entityClass; }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) { getEntityManager().persist(entity); }

    public void edit(T entity) { getEntityManager().merge(entity); }

    public void remove(T entity) { getEntityManager().remove(getEntityManager().merge(entity)); }

    public T find(Object id) { return getEntityManager().find(entityClass, id); }

    // Busca y borra en la misma transacción (antes lo hacía GenericDAO con RESOURCE_LOCAL)
    public void removeById(Object id) {
        T entity = find(id);
        if (entity != null) remove(entity);
    }

    public List<T> findAll() {
        return getEntityManager()
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }
}