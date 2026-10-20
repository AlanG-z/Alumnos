<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>${empty materia ? 'Nueva materia' : 'Editar materia'}</h2>
<form method="post" action="${pageContext.request.contextPath}/materias">
  <input type="hidden" name="accion" value="guardar">
  <input type="hidden" name="id" value="${materia.idmateria}">

  <label>Nombre</label>
  <input type="text" name="nombre" maxlength="45" required value="<c:out value='${materia.nombre}'/>">

  <p>
    <button class="btn" type="submit">Guardar</button>
    <a class="btn btn-gray" href="${pageContext.request.contextPath}/materias">Cancelar</a>
  </p>
</form>
<%@ include file="/WEB-INF/views/footer.jspf" %>