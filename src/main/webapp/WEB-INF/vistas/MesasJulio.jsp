<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>Mesas de examen de julio</h2>
<div class="table-wrap"><table>
  <tr><th>Fecha</th><th>Materia</th><th>Alumno</th><th>Nota</th></tr>
  <c:forEach var="e" items="${examenes}">
    <tr>
      <td><fmt:formatDate value="${e.examenPK.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
      <td><c:out value="${e.materia.nombre}"/></td>
      <td><c:out value="${e.alumno.nombre}"/></td>
      <td>${e.nota}</td>
    </tr>
  </c:forEach>
  <c:if test="${empty examenes}"><tr><td colspan="4">No hay mesas en julio.</td></tr></c:if>
</table></div>
<%@ include file="/WEB-INF/views/footer.jspf" %>