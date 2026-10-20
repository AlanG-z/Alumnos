<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>Docentes que dictan más de dos materias</h2>
<div class="table-wrap"><table>
  <tr><th>Docente</th><th>Cargo</th><th>Materias</th></tr>
  <c:forEach var="d" items="${docentes}">
    <tr>
      <td><c:out value="${d.nombre}"/></td>
      <td>${d.cargo}</td>
      <td>${d.materiaCollection.size()}</td>
    </tr>
  </c:forEach>
  <c:if test="${empty docentes}"><tr><td colspan="3">Ningún docente cumple la condición.</td></tr></c:if>
</table></div>
<%@ include file="/WEB-INF/views/footer.jspf" %>