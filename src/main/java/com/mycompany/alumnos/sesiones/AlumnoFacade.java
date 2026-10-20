package com.mycompany.alumnos.sesiones;

import com.mycompany.alumnos.Alumno;
import com.mycompany.alumnos.Carrera;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AlumnoFacade extends AbstractFacade<Alumno> {

    @PersistenceContext(unitName = "AlumnosEJB_PU")
    private EntityManager em;

    public AlumnoFacade() { super(Alumno.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    public void crear(String nombre, String registro, Integer idCarrera) {
        Alumno a = new Alumno();
        a.setNombre(nombre);
        a.setRegistro(registro);
        a.setCarreraIdcarrera(em.find(Carrera.class, idCarrera));
        em.persist(a);
    }

    // Ejercicio 6.2: alumnos que no rindieron ningún examen en el año indicado
    public List<Alumno> sinExamenesEnAnio(int anio) {
        return em.createQuery(
                "SELECT a FROM Alumno a "
                + "WHERE NOT EXISTS (SELECT e FROM Examen e "
                + "WHERE e.alumno = a AND FUNCTION('YEAR', e.examenPK.fecha) = :anio) "
                + "ORDER BY a.nombre", Alumno.class)
                .setParameter("anio", anio)
                .getResultList();
    }
}