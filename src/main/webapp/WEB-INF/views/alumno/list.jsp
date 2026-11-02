<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>

<div class="summary-cards">
  <div class="card-stat">
    <span class="stat-num">${alumnos.size()}</span>
    <span class="stat-label">Alumnos</span>
  </div>
  <div class="card-stat">
    <span class="stat-num">${carreras.size()}</span>
    <span class="stat-label">Carreras activas</span>
  </div>
  <c:forEach var="e" items="${alumnosPorCarrera}">
    <div class="card-stat">
      <span class="stat-num">${e.value}</span>
      <span class="stat-label"><c:out value="${e.key}"/></span>
    </div>
  </c:forEach>
</div>

<div class="page-head">
  <h2>Alumnos</h2>
  <a class="btn" href="${pageContext.request.contextPath}/alumnos?accion=nuevo">+ Nuevo alumno</a>
</div>

<c:if test="${not empty alumnos}">
  <div class="toolbar">
    <input type="text" id="search" class="search-input" placeholder="Buscar por nombre, registro o carrera…" autocomplete="off">
  </div>
  <div class="table-wrap">
    <table class="datatable" id="tabla-alumnos" data-datatable>
      <thead>
        <tr><th class="col-id">ID</th><th>Nombre</th><th>Registro</th><th>Carrera</th><th>Acciones</th></tr>
      </thead>
      <tbody>
        <c:forEach var="a" items="${alumnos}">
          <tr data-search="<c:out value='${a.nombre}'/> <c:out value='${a.registro}'/> <c:out value='${a.carreraIdcarrera.nombre}'/>">
            <td class="col-id">${a.idalumno}</td>
            <td data-avatar><c:out value="${a.nombre}"/></td>
            <td>${a.registro}</td>
            <td data-badge><c:out value="${a.carreraIdcarrera.nombre}"/></td>
            <td class="col-actions">
              <a class="icon-btn edit" href="${pageContext.request.contextPath}/alumnos?accion=editar&id=${a.idalumno}" title="Editar" aria-label="Editar a <c:out value='${a.nombre}'/>">
                <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/></svg>
              </a>
              <form class="inline" method="post" action="${pageContext.request.contextPath}/alumnos"
                    onsubmit="return confirm('¿Eliminar este alumno?');">
                <input type="hidden" name="accion" value="eliminar">
                <input type="hidden" name="id" value="${a.idalumno}">
                <button class="icon-btn del" type="submit" title="Eliminar" aria-label="Eliminar a <c:out value='${a.nombre}'/>">
                  <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/><path d="M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
  <p class="no-results" id="sin-resultados" hidden>No se encontraron alumnos para “<span id="q"></span>”.</p>
  <div class="pagination" id="paginacion"></div>
</c:if>
<c:if test="${empty alumnos}">
  <div class="empty-state">
    <p>No hay alumnos cargados.</p>
    <a class="btn" href="${pageContext.request.contextPath}/alumnos?accion=nuevo">+ Nuevo alumno</a>
  </div>
</c:if>

<%@ include file="/WEB-INF/views/footer.jspf" %>
