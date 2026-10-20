package com.mycompany.alumnos.controller;

import com.mycompany.alumnos.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/materias")
public class MateriaServlet extends HttpServlet {

    private final GenericDAO<Materia> dao = new GenericDAO<>(Materia.class);
    private static final String VISTAS = "/WEB-INF/views/materia/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("nuevo".equals(accion) || "editar".equals(accion)) {
            if ("editar".equals(accion)) {
                req.setAttribute("materia", dao.find(Integer.valueOf(req.getParameter("id"))));
            }
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
                Materia m = new Materia();
                if (idStr != null && !idStr.isBlank()) m.setIdmateria(Integer.valueOf(idStr));
                m.setNombre(req.getParameter("nombre").trim());
                dao.save(m);
            }
            resp.sendRedirect(req.getContextPath() + "/materias");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "No se pudo completar la operación: " + e.getMessage());
            listar(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("materias", dao.findAll());
        req.getRequestDispatcher(VISTAS + "list.jsp").forward(req, resp);
    }
}