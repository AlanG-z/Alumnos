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

  <label>Materias que dicta <span class="hint">(Ctrl o Cmd para elegir varias)</span></label>
  <select name="materiaId" multiple size="6">
    <c:forEach var="m" items="${materias}">
      <option value="${m.idmateria}" ${not empty docente.materiaCollection && docente.materiaCollection.contains(m) ? 'selected' : ''}>
        <c:out value="${m.nombre}"/>
      </option>
    </c:forEach>
    <c:if test="${empty materias}"><option value="" disabled>No hay materias cargadas</option></c:if>
  </select>

  <p>
    <button class="btn" type="submit">Guardar</button>
    <a class="btn btn-gray" href="${pageContext.request.contextPath}/docentes">Cancelar</a>
  </p>
</form>
<%@ include file="/WEB-INF/views/footer.jspf" %>
