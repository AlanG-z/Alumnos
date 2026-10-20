package com.mycompany.alumnos.controller;

import com.mycompany.alumnos.Facultad;
import com.mycompany.alumnos.sesiones.*;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Year;

@WebServlet(name = "Manejador", loadOnStartup = 1,
        urlPatterns = {"/Agregar", "/Listar", "/Facultad",
                       "/MesasJulio", "/SinExamenes", "/DocentesMasDeDos"})
public class Manejador extends HttpServlet {

    @EJB private FacultadFacade facu;
    @EJB private CarreraFacade carreraFacade;
    @EJB private AlumnoFacade alumnoFacade;
    @EJB private ExamenFacade examenFacade;
    @EJB private DocenteFacade docenteFacade;

    private static final String VISTA = "/WEB-INF/vistas/";

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

                case "/Facultad": {   // Ejercicio 2
                    Integer nro = Integer.parseInt(req.getParameter("codigoFacu"));
                    Facultad miFacu = facu.find(nro);
                    req.setAttribute("facultad", miFacu);
                    req.setAttribute("carreras", carreraFacade.findByFacultad(nro));
                    url = VISTA + "ListarCarreras.jsp";
                    break;
                }

                case "/Agregar": {    // Ejercicio 3
                    if ("POST".equals(req.getMethod())) {
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
        }

        req.getRequestDispatcher(url).forward(req, resp);
    }
}