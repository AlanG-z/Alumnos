package com.mycompany.alumnos.sesiones;

import com.mycompany.alumnos.Facultad;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class FacultadFacade extends AbstractFacade<Facultad> {

    @PersistenceContext(unitName = "AlumnosEJB_PU")
    private EntityManager em;

    public FacultadFacade() { super(Facultad.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }
}