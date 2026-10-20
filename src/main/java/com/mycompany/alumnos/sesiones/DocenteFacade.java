package com.mycompany.alumnos.sesiones;

import com.mycompany.alumnos.Docente;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocenteFacade extends AbstractFacade<Docente> {

    @PersistenceContext(unitName = "AlumnosEJB_PU")
    private EntityManager em;

    public DocenteFacade() { super(Docente.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    // Ejercicio 6.3: docentes que dictan más de N materias
    public List<Docente> conMasDeMaterias(int cantidad) {
        return em.createQuery(
                "SELECT d FROM Docente d WHERE SIZE(d.materiaCollection) > :n ORDER BY d.nombre",
                Docente.class)
                .setParameter("n", cantidad)
                .getResultList();
    }
}