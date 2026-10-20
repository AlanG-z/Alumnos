<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<h2>Materias</h2>
<a class="btn" href="${pageContext.request.contextPath}/materias?accion=nuevo">+ Nueva materia</a>
<div class="table-wrap"><table>
  <tr><th>ID</th><th>Nombre</th><th>Acciones</th></tr>
  <c:forEach var="m" items="${materias}">
    <tr>
      <td>${m.idmateria}</td>
      <td><c:out value="${m.nombre}"/></td>
      <td>
        <a class="btn btn-edit" href="${pageContext.request.contextPath}/materias?accion=editar&id=${m.idmateria}">Editar</a>
        <form class="inline" method="post" action="${pageContext.request.contextPath}/materias"
              onsubmit="return confirm('¿Eliminar esta materia?');">
          <input type="hidden" name="accion" value="eliminar">
          <input type="hidden" name="id" value="${m.idmateria}">
          <button class="btn btn-del" type="submit">Eliminar</button>
        </form>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty materias}"><tr><td colspan="3">No hay materias registradas.</td></tr></c:if>
</table></div>
<%@ include file="/WEB-INF/views/footer.jspf" %>