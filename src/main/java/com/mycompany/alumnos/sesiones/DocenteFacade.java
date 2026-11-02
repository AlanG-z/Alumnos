package com.mycompany.alumnos.sesiones;

import com.mycompany.alumnos.Cargo;
import com.mycompany.alumnos.Docente;
import com.mycompany.alumnos.Materia;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Stateless
public class DocenteFacade extends AbstractFacade<Docente> {

    @PersistenceContext(unitName = "AlumnosEJB_PU")
    private EntityManager em;

    public DocenteFacade() { super(Docente.class); }

    @Override
    protected EntityManager getEntityManager() { return em; }

    // CRUD: crear y actualizar resuelven las materias en la misma transacción
    public void crear(String nombre, Cargo cargo, Collection<Integer> idsMaterias) {
        Docente d = new Docente();
        d.setNombre(nombre);
        d.setCargo(cargo);
        d.setMateriaCollection(new ArrayList<>(materiasDe(idsMaterias)));
        em.persist(d);
    }

    public void actualizar(Integer id, String nombre, Cargo cargo, Collection<Integer> idsMaterias) {
        Docente d = em.find(Docente.class, id);
        d.setNombre(nombre);
        d.setCargo(cargo);
        Collection<Materia> actuales = d.getMateriaCollection();
        if (actuales == null) {
            d.setMateriaCollection(new ArrayList<>(materiasDe(idsMaterias)));
        } else {
            // La colección está gestionada: se limpia y se vuelve a llenar para que
            // JPA actualice la tabla intermedia docente_has_materia.
            actuales.clear();
            actuales.addAll(materiasDe(idsMaterias));
        }
    }

    private List<Materia> materiasDe(Collection<Integer> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        return em.createQuery(
                "SELECT m FROM Materia m WHERE m.idmateria IN :ids ORDER BY m.nombre",
                Materia.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    // Ejercicio 6.3: docentes que dictan más de N materias
    public List<Docente> conMasDeMaterias(int cantidad) {
        return em.createQuery(
                "SELECT d FROM Docente d WHERE SIZE(d.materiaCollection) > :n ORDER BY d.nombre",
                Docente.class)
                .setParameter("n", cantidad)
                .getResultList();
    }
}
