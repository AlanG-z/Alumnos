<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>Carreras</h2>
<a class="btn" href="${pageContext.request.contextPath}/carreras?accion=nuevo">+ Nueva carrera</a>
<div class="table-wrap"><table>
  <tr><th>ID</th><th>Nombre</th><th>Facultad</th><th>Acciones</th></tr>
  <c:forEach var="c" items="${carreras}">
    <tr>
      <td>${c.idcarrera}</td>
      <td><c:out value="${c.nombre}"/></td>
      <td><c:out value="${c.facultadIdfacultad.nombre}"/></td>
      <td>
        <a class="btn btn-edit" href="${pageContext.request.contextPath}/carreras?accion=editar&id=${c.idcarrera}">Editar</a>
        <form class="inline" method="post" action="${pageContext.request.contextPath}/carreras"
              onsubmit="return confirm('¿Eliminar esta carrera? Se eliminarán también sus alumnos.');">
          <input type="hidden" name="accion" value="eliminar">
          <input type="hidden" name="id" value="${c.idcarrera}">
          <button class="btn btn-del" type="submit">Eliminar</button>
        </form>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty carreras}"><tr><td colspan="4">No hay carreras registradas.</td></tr></c:if>
</table></div>
<%@ include file="/WEB-INF/views/footer.jspf" %>