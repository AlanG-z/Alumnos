<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>${empty docente ? 'Nuevo docente' : 'Editar docente'}</h2>
<form method="post" action="${pageContext.request.contextPath}/docentes">
  <input type="hidden" name="accion" value="guardar">
  <input type="hidden" name="id" value="${docente.iddocente}">

  <label>Nombre</label>
  <input type="text" name="nombre" maxlength="45" required value="<c:out value='${docente.nombre}'/>">

  <label>Cargo</label>
  <select name="cargo" required>
    <c:forEach var="c" items="${cargos}">
      <option value="${c}" ${docente.cargo == c ? 'selected' : ''}><c:out value="${c}"/></option>
    </c:forEach>
  </select>

  <label>Materias que dicta <span class="hint">(marcá todas las que correspondan)</span></label>
  <div class="checkbox-list">
    <c:forEach var="m" items="${materias}">
      <label class="checkbox-item">
        <input type="checkbox" name="materiaId" value="${m.idmateria}"
               ${not empty docente.materiaCollection && docente.materiaCollection.contains(m) ? 'checked' : ''}>
        <c:out value="${m.nombre}"/>
      </label>
    </c:forEach>
    <c:if test="${empty materias}"><span class="muted">No hay materias cargadas</span></c:if>
  </div>

  <p>
    <button class="btn" type="submit">Guardar</button>
    <a class="btn btn-gray" href="${pageContext.request.contextPath}/docentes">Cancelar</a>
  </p>
</form>
<%@ include file="/WEB-INF/views/footer.jspf" %>
