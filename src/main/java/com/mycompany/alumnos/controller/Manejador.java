package com.mycompany.alumnos.controller;

import com.mycompany.alumnos.*;
import com.mycompany.alumnos.sesiones.*;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Único servlet de la aplicación: CRUD de alumnos, carreras, materias y
 * docentes, más las vistas de reportes. Antes la lógica CRUD vivía en
 * AlumnoServlet, CarreraServlet, MateriaServlet y FacultadServlet.
 */
@WebServlet(name = "Manejador", loadOnStartup = 1,
        urlPatterns = {"/alumnos", "/carreras", "/materias", "/docentes",
                       "/Agregar", "/Listar", "/Facultad",
                       "/MesasJulio", "/SinExamenes", "/DocentesMasDeDos"})
public class Manejador extends HttpServlet {

    private static final String VISTA = "/WEB-INF/vistas/";
    private static final String VIEW = "/WEB-INF/views/";

    @EJB private FacultadFacade facu;
    @EJB private CarreraFacade carreraFacade;
    @EJB private AlumnoFacade alumnoFacade;
    @EJB private ExamenFacade examenFacade;
    @EJB private DocenteFacade docenteFacade;
    @EJB private MateriaFacade materiaFacade;

    @Override
    public void init() throws ServletException {
        // Ejercicio 1: lista de facultades disponible para el menú
        getServletContext().setAttribute("facultades", facu.findAll());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        procesar(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        procesar(req, resp);
    }

    private void procesar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String url;

        try {
            switch (req.getServletPath()) {

                // ---------- CRUD ----------
                case "/alumnos":  url = crudAlumno(req, resp);  break;
                case "/carreras": url = crudCarrera(req, resp); break;
                case "/materias": url = crudMateria(req, resp); break;
                case "/docentes": url = crudDocente(req, resp); break;

                // ---------- Reportes y vistas con EJB ----------
                case "/Facultad": {   // Ejercicio 2
                    Integer nro = Integer.parseInt(req.getParameter("codigoFacu"));
                    Facultad miFacu = facu.find(nro);
                    req.setAttribute("facultad", miFacu);
                    req.setAttribute("carreras", carreraFacade.findByFacultad(nro));
                    url = VISTA + "ListarCarreras.jsp";
                    break;
                }

                case "/Agregar": {    // Ejercicio 3
                    if (esPost(req)) {
                        try {
                            alumnoFacade.crear(
                                    req.getParameter("nombre").trim(),
                                    req.getParameter("registro").trim(),
                                    Integer.valueOf(req.getParameter("carreraId")));
                            resp.sendRedirect(req.getContextPath() + "/Listar");
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            req.setAttribute("error", "No se pudo guardar el alumno. Revisá los datos.");
                        }
                    }
                    req.setAttribute("carreras", carreraFacade.findAll());
                    url = VISTA + "Agregar.jsp";
                    break;
                }

                case "/Listar":
                    req.setAttribute("alumnos", alumnoFacade.findAll());
                    url = VISTA + "ListarAlumnos.jsp";
                    break;

                case "/MesasJulio":   // Ejercicio 6.1
                    req.setAttribute("examenes", examenFacade.porMes(7));
                    url = VISTA + "MesasJulio.jsp";
                    break;

                case "/SinExamenes": { // Ejercicio 6.2
                    int anio = Year.now().getValue();
                    req.setAttribute("anio", anio);
                    req.setAttribute("alumnos", alumnoFacade.sinExamenesEnAnio(anio));
                    url = VISTA + "SinExamenes.jsp";
                    break;
                }

                case "/DocentesMasDeDos": // Ejercicio 6.3
                    req.setAttribute("docentes", docenteFacade.conMasDeMaterias(2));
                    url = VISTA + "DocentesMasDeDos.jsp";
                    break;

                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro inválido");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno");
            return;
        }

        if (url == null) return;   // la operación ya respondió con un redirect
        req.getRequestDispatcher(url).forward(req, resp);
    }

    // ==================== ALUMNOS ====================

    private String crudAlumno(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if (esPost(req)) {
                if ("eliminar".equals(req.getParameter("accion"))) {
                    alumnoFacade.removeById(Integer.valueOf(req.getParameter("id")));
                } else {
                    String nombre = req.getParameter("nombre").trim();
                    String registro = req.getParameter("registro").trim();
                    Integer idCarrera = Integer.valueOf(req.getParameter("carreraId"));
                    String id = req.getParameter("id");
                    if (id == null || id.isBlank()) {
                        alumnoFacade.crear(nombre, registro, idCarrera);
                    } else {
                        alumnoFacade.actualizar(Integer.valueOf(id), nombre, registro, idCarrera);
                    }
                }
                resp.sendRedirect(req.getContextPath() + "/alumnos");
                return null;
            }

            String accion = req.getParameter("accion");
            if ("nuevo".equals(accion) || "editar".equals(accion)) {
                if ("editar".equals(accion)) {
                    req.setAttribute("alumno", alumnoFacade.find(Integer.valueOf(req.getParameter("id"))));
                }
                req.setAttribute("carreras", carreraFacade.findAll());
                return VIEW + "alumno/form.jsp";
            }
            return listarAlumnos(req);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "No se pudo completar la operación: " + e.getMessage());
            return listarAlumnos(req);
        }
    }

    private String listarAlumnos(HttpServletRequest req) {
        List<Alumno> alumnos = alumnoFacade.findAll();
        req.setAttribute("alumnos", alumnos);
        req.setAttribute("alumnosPorCarrera", contarPorCarrera(alumnos));
        return VIEW + "alumno/list.jsp";
    }

    private Map<String, Long> contarPorCarrera(List<Alumno> alumnos) {
        Map<String, Long> porCarrera = new LinkedHashMap<>();
        for (Alumno a : alumnos) {
            String nombre = a.getCarreraIdcarrera() != null ? a.getCarreraIdcarrera().getNombre() : "Sin carrera";
            porCarrera.merge(nombre, 1L, Long::sum);
        }
        return porCarrera;
    }

    // ==================== CARRERAS ====================

    private String crudCarrera(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if (esPost(req)) {
                if ("eliminar".equals(req.getParameter("accion"))) {
                    carreraFacade.removeById(Integer.valueOf(req.getParameter("id")));
                } else {
                    String nombre = req.getParameter("nombre").trim();
                    Integer idFacultad = Integer.valueOf(req.getParameter("facultadId"));
                    String id = req.getParameter("id");
                    if (id == null || id.isBlank()) {
                        carreraFacade.crear(nombre, idFacultad);
                    } else {
                        carreraFacade.actualizar(Integer.valueOf(id), nombre, idFacultad);
                    }
                }
                resp.sendRedirect(req.getContextPath() + "/carreras");
                return null;
            }

            String accion = req.getParameter("accion");
            if ("nuevo".equals(accion) || "editar".equals(accion)) {
                if ("editar".equals(accion)) {
                    req.setAttribute("carrera", carreraFacade.find(Integer.valueOf(req.getParameter("id"))));
                }
                // Mismo nombre que el atributo de contexto del menú: la lista es idéntica
                req.setAttribute("facultades", facu.findAll());
                return VIEW + "carrera/form.jsp";
            }
            return listarCarreras(req);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "No se pudo completar la operación: " + e.getMessage());
            return listarCarreras(req);
        }
    }

    private String listarCarreras(HttpServletRequest req) {
        req.setAttribute("carreras", carreraFacade.findAll());
        return VIEW + "carrera/list.jsp";
    }

    // ==================== MATERIAS ====================

    private String crudMateria(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if (esPost(req)) {
                if ("eliminar".equals(req.getParameter("accion"))) {
                    materiaFacade.removeById(Integer.valueOf(req.getParameter("id")));
                } else {
                    String nombre = req.getParameter("nombre").trim();
                    String id = req.getParameter("id");
                    if (id == null || id.isBlank()) {
                        materiaFacade.crear(nombre);
                    } else {
                        materiaFacade.actualizar(Integer.valueOf(id), nombre);
                    }
                }
                resp.sendRedirect(req.getContextPath() + "/materias");
                return null;
            }

            String accion = req.getParameter("accion");
            if ("nuevo".equals(accion) || "editar".equals(accion)) {
                if ("editar".equals(accion)) {
                    req.setAttribute("materia", materiaFacade.find(Integer.valueOf(req.getParameter("id"))));
                }
                return VIEW + "materia/form.jsp";
            }
            return listarMaterias(req);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "No se pudo completar la operación: " + e.getMessage());
            return listarMaterias(req);
        }
    }

    private String listarMaterias(HttpServletRequest req) {
        req.setAttribute("materias", materiaFacade.findAll());
        return VIEW + "materia/list.jsp";
    }

    // ==================== DOCENTES ====================

    private String crudDocente(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if (esPost(req)) {
                if ("eliminar".equals(req.getParameter("accion"))) {
                    docenteFacade.removeById(Integer.valueOf(req.getParameter("id")));
                } else {
                    String nombre = req.getParameter("nombre").trim();
                    Cargo cargo = Cargo.valueOf(req.getParameter("cargo"));
                    Collection<Integer> idsMaterias = idsSeleccionados(req, "materiaId");
                    String id = req.getParameter("id");
                    if (id == null || id.isBlank()) {
                        docenteFacade.crear(nombre, cargo, idsMaterias);
                    } else {
                        docenteFacade.actualizar(Integer.valueOf(id), nombre, cargo, idsMaterias);
                    }
                }
                resp.sendRedirect(req.getContextPath() + "/docentes");
                return null;
            }

            String accion = req.getParameter("accion");
            if ("nuevo".equals(accion) || "editar".equals(accion)) {
                if ("editar".equals(accion)) {
                    req.setAttribute("docente", docenteFacade.find(Integer.valueOf(req.getParameter("id"))));
                }
                req.setAttribute("materias", materiaFacade.findAll());
                req.setAttribute("cargos", Cargo.values());
                return VIEW + "docente/form.jsp";
            }
            return listarDocentes(req);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "No se pudo completar la operación: " + e.getMessage());
            return listarDocentes(req);
        }
    }

    private String listarDocentes(HttpServletRequest req) {
        List<Docente> docentes = docenteFacade.findAll();
        // Texto de búsqueda por fila (nombre + cargo + materias), calculado acá para
        // no anidar etiquetas dentro de un atributo del JSP.
        Map<Integer, String> busqueda = new LinkedHashMap<>();
        for (Docente d : docentes) {
            StringBuilder sb = new StringBuilder(d.getNombre()).append(' ').append(d.getCargo());
            if (d.getMateriaCollection() != null) {
                for (Materia m : d.getMateriaCollection()) sb.append(' ').append(m.getNombre());
            }
            busqueda.put(d.getIddocente(), sb.toString());
        }
        req.setAttribute("docentes", docentes);
        req.setAttribute("busqueda", busqueda);
        return VIEW + "docente/list.jsp";
    }

    private Collection<Integer> idsSeleccionados(HttpServletRequest req, String parametro) {
        String[] valores = req.getParameterValues(parametro);
        Collection<Integer> ids = new ArrayList<>();
        if (valores != null) {
            for (String v : valores) {
                if (v != null && !v.isBlank()) ids.add(Integer.valueOf(v));
            }
        }
        return ids;
    }

    private boolean esPost(HttpServletRequest req) {
        return "POST".equals(req.getMethod());
    }
}
