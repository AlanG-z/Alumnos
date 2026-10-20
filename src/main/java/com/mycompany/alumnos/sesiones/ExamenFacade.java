package com.mycompany.alumnos.sesiones;

import com.mycompany.alumnos.Examen;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ExamenFacade extends AbstractFacade<Examen> {

    @PersistenceContext(unitName = "AlumnosEJB_PU")
    private EntityManager em;

    public ExamenFacade() { super(Examen.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    // Ejercicio 6.1: mesas de examen de un mes (julio = 7), con datos del alumno
    public List<Examen> porMes(int mes) {
        return em.createQuery(
                "SELECT e FROM Examen e JOIN FETCH e.alumno JOIN FETCH e.materia "
                + "WHERE FUNCTION('MONTH', e.examenPK.fecha) = :mes "
                + "ORDER BY e.examenPK.fecha, e.alumno.nombre", Examen.class)
                .setParameter("mes", mes)
                .getResultList();
    }
}