<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>Agregar alumno</h2>
<form method="post" action="${pageContext.request.contextPath}/Agregar">
  <label>Nombre</label>
  <input type="text" name="nombre" maxlength="45" required>
  <label>Registro</label>
  <input type="text" name="registro" maxlength="6" required>
  <label>Carrera</label>
  <select name="carreraId" required>
    <option value="">-- Seleccione --</option>
    <c:forEach var="c" items="${carreras}">
      <option value="${c.idcarrera}"><c:out value="${c.nombre}"/></option>
    </c:forEach>
  </select>
  <p>
    <button class="btn" type="submit">Guardar</button>
    <a class="btn btn-gray" href="${pageContext.request.contextPath}/Listar">Cancelar</a>
  </p>
</form>
<%@ include file="/WEB-INF/views/footer.jspf" %>