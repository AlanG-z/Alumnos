package com.mycompany.alumnos.controller;

import com.mycompany.alumnos.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/carreras")
public class CarreraServlet extends HttpServlet {

    private final GenericDAO<Carrera> dao = new GenericDAO<>(Carrera.class);
    private final GenericDAO<Facultad> facultadDao = new GenericDAO<>(Facultad.class);
    private static final String VISTAS = "/WEB-INF/views/carrera/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("nuevo".equals(accion) || "editar".equals(accion)) {
            if ("editar".equals(accion)) {
                req.setAttribute("carrera", dao.find(Integer.valueOf(req.getParameter("id"))));
            }
            req.setAttribute("facultades", facultadDao.findAll());
            req.getRequestDispatcher(VISTAS + "form.jsp").forward(req, resp);
        } else {
            listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            if ("eliminar".equals(req.getParameter("accion"))) {
                dao.delete(Integer.valueOf(req.getParameter("id")));
            } else {
                String idStr = req.getParameter("id");
                Carrera c = new Carrera();
                if (idStr != null && !idStr.isBlank()) c.setIdcarrera(Integer.valueOf(idStr));
                c.setNombre(req.getParameter("nombre").trim());
                c.setFacultadIdfacultad(facultadDao.find(Integer.valueOf(req.getParameter("facultadId"))));
                dao.save(c);
            }
            resp.sendRedirect(req.getContextPath() + "/carreras");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "No se pudo completar la operación: " + e.getMessage());
            listar(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("carreras", dao.findAll());
        req.getRequestDispatcher(VISTAS + "list.jsp").forward(req, resp);
    }
}