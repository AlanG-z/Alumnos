<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>${empty carrera ? 'Nueva carrera' : 'Editar carrera'}</h2>
<form method="post" action="${pageContext.request.contextPath}/carreras">
  <input type="hidden" name="accion" value="guardar">
  <input type="hidden" name="id" value="${carrera.idcarrera}">

  <label>Nombre</label>
  <input type="text" name="nombre" maxlength="45" required value="<c:out value='${carrera.nombre}'/>">

  <label>Facultad</label>
  <select name="facultadId" required>
    <option value="">-- Seleccione --</option>
    <c:forEach var="f" items="${facultades}">
      <option value="${f.idfacultad}" ${carrera.facultadIdfacultad.idfacultad == f.idfacultad ? 'selected' : ''}>
        <c:out value="${f.nombre}"/>
      </option>
    </c:forEach>
  </select>

  <p>
    <button class="btn" type="submit">Guardar</button>
    <a class="btn btn-gray" href="${pageContext.request.contextPath}/carreras">Cancelar</a>
  </p>
</form>
<%@ include file="/WEB-INF/views/footer.jspf" %>