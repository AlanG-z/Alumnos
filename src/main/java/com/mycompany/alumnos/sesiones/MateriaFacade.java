package com.mycompany.alumnos.sesiones;

import com.mycompany.alumnos.Materia;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class MateriaFacade extends AbstractFacade<Materia> {

    @PersistenceContext(unitName = "AlumnosEJB_PU")
    private EntityManager em;

    public MateriaFacade() { super(Materia.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    public void crear(String nombre) {
        Materia m = new Materia();
        m.setNombre(nombre);
        em.persist(m);
    }

    public void actualizar(Integer id, String nombre) {
        Materia m = em.find(Materia.class, id);
        m.setNombre(nombre);
    }
}