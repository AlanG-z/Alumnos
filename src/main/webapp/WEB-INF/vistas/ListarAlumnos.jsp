<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>Alumnos</h2>
<div class="table-wrap"><table>
  <tr><th>ID</th><th>Nombre</th><th>Registro</th><th>Carrera</th></tr>
  <c:forEach var="a" items="${alumnos}">
    <tr>
      <td>${a.idalumno}</td>
      <td><c:out value="${a.nombre}"/></td>
      <td><c:out value="${a.registro}"/></td>
      <td><c:out value="${a.carreraIdcarrera.nombre}"/></td>
    </tr>
  </c:forEach>
</table></div>
<%@ include file="/WEB-INF/views/footer.jspf" %>