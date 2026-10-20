package com.mycompany.alumnos.sesiones;

import com.mycompany.alumnos.Carrera;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CarreraFacade extends AbstractFacade<Carrera> {

    @PersistenceContext(unitName = "AlumnosEJB_PU")
    private EntityManager em;

    public CarreraFacade() { super(Carrera.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    public List<Carrera> findByFacultad(Integer idFacultad) {
        return em.createQuery(
                "SELECT c FROM Carrera c WHERE c.facultadIdfacultad.idfacultad = :id ORDER BY c.nombre",
                Carrera.class)
                .setParameter("id", idFacultad)
                .getResultList();
    }
}