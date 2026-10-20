<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>${empty alumno ? 'Nuevo alumno' : 'Editar alumno'}</h2>
<form method="post" action="${pageContext.request.contextPath}/alumnos">
  <input type="hidden" name="accion" value="guardar">
  <input type="hidden" name="id" value="${alumno.idalumno}">

  <label>Nombre</label>
  <input type="text" name="nombre" maxlength="45" required value="<c:out value='${alumno.nombre}'/>">

  <label>Registro</label>
  <input type="text" name="registro" maxlength="6" required value="<c:out value='${alumno.registro}'/>">

  <label>Carrera</label>
  <select name="carreraId" required>
    <option value="">-- Seleccione --</option>
    <c:forEach var="c" items="${carreras}">
      <option value="${c.idcarrera}" ${alumno.carreraIdcarrera.idcarrera == c.idcarrera ? 'selected' : ''}>
        <c:out value="${c.nombre}"/>
      </option>
    </c:forEach>
  </select>

  <p>
    <button class="btn" type="submit">Guardar</button>
    <a class="btn btn-gray" href="${pageContext.request.contextPath}/alumnos">Cancelar</a>
  </p>
</form>
<%@ include file="/WEB-INF/views/footer.jspf" %>    