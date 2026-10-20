package com.mycompany.alumnos.controller;

import com.mycompany.alumnos.GenericDAO;
import com.mycompany.alumnos.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/alumnos")
public class AlumnoServlet extends HttpServlet {

    private final GenericDAO<Alumno> dao = new GenericDAO<>(Alumno.class);
    private final GenericDAO<Carrera> carreraDao = new GenericDAO<>(Carrera.class);
    private static final String VISTAS = "/WEB-INF/views/alumno/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("nuevo".equals(accion) || "editar".equals(accion)) {
            if ("editar".equals(accion)) {
                req.setAttribute("alumno", dao.find(Integer.valueOf(req.getParameter("id"))));
            }
            req.setAttribute("carreras", carreraDao.findAll());
            req.getRequestDispatcher(VISTAS + "form.jsp").forward(req, resp);
        } else {
            listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String accion = req.getParameter("accion");
        try {
            if ("eliminar".equals(accion)) {
                dao.delete(Integer.valueOf(req.getParameter("id")));
            } else {
                String idStr = req.getParameter("id");
                Alumno a = new Alumno();
                if (idStr != null && !idStr.isBlank()) a.setIdalumno(Integer.valueOf(idStr));
                a.setNombre(req.getParameter("nombre").trim());
                a.setRegistro(req.getParameter("registro").trim());
                a.setCarreraIdcarrera(carreraDao.find(Integer.valueOf(req.getParameter("carreraId"))));
                dao.save(a);
            }
            resp.sendRedirect(req.getContextPath() + "/alumnos");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "No se pudo completar la operación: " + e.getMessage());
            listar(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Alumno> alumnos = dao.findAll();
        req.setAttribute("alumnos", alumnos);
        req.setAttribute("alumnosPorCarrera", contarPorCarrera(alumnos));
        req.getRequestDispatcher(VISTAS + "list.jsp").forward(req, resp);
    }

    private Map<String, Long> contarPorCarrera(List<Alumno> alumnos) {
        Map<String, Long> porCarrera = new LinkedHashMap<>();
        for (Alumno a : alumnos) {
            String nombre = a.getCarreraIdcarrera() != null ? a.getCarreraIdcarrera().getNombre() : "Sin carrera";
            porCarrera.merge(nombre, 1L, Long::sum);
        }
        return porCarrera;
    }
}