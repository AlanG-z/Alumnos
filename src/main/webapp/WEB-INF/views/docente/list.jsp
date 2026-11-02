<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>

<c:set var="totalMaterias" value="0"/>
<c:set var="sinMaterias" value="0"/>
<c:forEach var="d" items="${docentes}">
  <c:set var="totalMaterias" value="${totalMaterias + d.materiaCollection.size()}"/>
  <c:if test="${empty d.materiaCollection}">
    <c:set var="sinMaterias" value="${sinMaterias + 1}"/>
  </c:if>
</c:forEach>

<div class="summary-cards">
  <div class="card-stat">
    <span class="stat-num">${docentes.size()}</span>
    <span class="stat-label">Docentes</span>
  </div>
  <div class="card-stat">
    <span class="stat-num">${totalMaterias}</span>
    <span class="stat-label">Materias dictadas</span>
  </div>
  <div class="card-stat">
    <span class="stat-num">${sinMaterias}</span>
    <span class="stat-label">Sin materias asignadas</span>
  </div>
</div>

<div class="page-head">
  <h2>Docentes</h2>
  <a class="btn" href="${pageContext.request.contextPath}/docentes?accion=nuevo">+ Nuevo docente</a>
</div>

<c:if test="${not empty docentes}">
  <div class="toolbar">
    <input type="text" id="search" class="search-input" placeholder="Buscar por nombre, cargo o materia…" autocomplete="off">
  </div>
  <div class="table-wrap">
    <table class="datatable" id="tabla-docentes" data-datatable>
      <thead>
        <tr><th class="col-id">ID</th><th>Nombre</th><th>Cargo</th><th>Materias</th><th>Acciones</th></tr>
      </thead>
      <tbody>
        <c:forEach var="d" items="${docentes}">
          <tr data-search="<c:out value='${busqueda[d.iddocente]}'/>">
            <td class="col-id">${d.iddocente}</td>
            <td data-avatar><c:out value="${d.nombre}"/></td>
            <td data-badge><c:out value="${d.cargo}"/></td>
            <td>
              <c:choose>
                <c:when test="${empty d.materiaCollection}"><span class="muted">—</span></c:when>
                <c:otherwise>
                  <c:forEach var="m" items="${d.materiaCollection}" varStatus="st">${st.index > 0 ? ', ' : ''}<c:out value="${m.nombre}"/></c:forEach>
                </c:otherwise>
              </c:choose>
            </td>
            <td class="col-actions">
              <a class="icon-btn edit" href="${pageContext.request.contextPath}/docentes?accion=editar&id=${d.iddocente}" title="Editar" aria-label="Editar a <c:out value='${d.nombre}'/>">
                <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/></svg>
              </a>
              <form class="inline" method="post" action="${pageContext.request.contextPath}/docentes"
                    onsubmit="return confirm('¿Eliminar este docente?');">
                <input type="hidden" name="accion" value="eliminar">
                <input type="hidden" name="id" value="${d.iddocente}">
                <button class="icon-btn del" type="submit" title="Eliminar" aria-label="Eliminar a <c:out value='${d.nombre}'/>">
                  <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/><path d="M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
  <p class="no-results" id="sin-resultados" hidden>No se encontraron docentes para “<span id="q"></span>”.</p>
  <div class="pagination" id="paginacion"></div>
</c:if>
<c:if test="${empty docentes}">
  <div class="empty-state">
    <p>No hay docentes cargados.</p>
    <a class="btn" href="${pageContext.request.contextPath}/docentes?accion=nuevo">+ Nuevo docente</a>
  </div>
</c:if>

<%@ include file="/WEB-INF/views/footer.jspf" %>
