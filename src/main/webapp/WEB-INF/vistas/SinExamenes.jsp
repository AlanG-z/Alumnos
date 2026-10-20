<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>Alumnos sin exámenes rendidos en ${anio}</h2>
<div class="table-wrap"><table>
  <tr><th>ID</th><th>Nombre</th><th>Registro</th></tr>
  <c:forEach var="a" items="${alumnos}">
    <tr><td>${a.idalumno}</td><td><c:out value="${a.nombre}"/></td><td><c:out value="${a.registro}"/></td></tr>
  </c:forEach>
  <c:if test="${empty alumnos}"><tr><td colspan="3">Todos los alumnos rindieron al menos una materia.</td></tr></c:if>
</table></div>
<%@ include file="/WEB-INF/views/footer.jspf" %>