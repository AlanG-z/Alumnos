<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>Carreras de <c:out value="${facultad.nombre}"/></h2>
<div class="table-wrap"><table>
  <tr><th>ID</th><th>Carrera</th></tr>
  <c:forEach var="c" items="${carreras}">
    <tr><td>${c.idcarrera}</td><td><c:out value="${c.nombre}"/></td></tr>
  </c:forEach>
  <c:if test="${empty carreras}"><tr><td colspan="2">Esta facultad no tiene carreras.</td></tr></c:if>
</table></div>
<%@ include file="/WEB-INF/views/footer.jspf" %>