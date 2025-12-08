<%-- 
    Document   : pedidos
    Created on : 7 dic 2025, 23:48:53
    Author     : Beto_
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Pedidos</title>
    <link rel="stylesheet" href="../estilos/styles_index.css">
</head>
<body>
    <h1>Administración de Pedidos</h1>
    <a href="dashboard.jsp">Volver al Dashboard</a>
    
    <table border="1" style="width:100%; border-collapse: collapse; margin-top: 20px;">
        <thead>
            <tr style="background-color: #003d72; color: white;">
                <th>ID</th>
                <th>Fecha</th>
                <th>Usuario ID</th>
                <th>Total</th>
                <th>Estado Actual</th>
                <th>Acción</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="compra" items="${listaComprasAdmin}">
                <tr>
                    <td>${compra.id}</td>
                    <td>${compra.fechaCompra}</td>
                    <td>${compra.usuarioId}</td>
                    <td>$${compra.total}</td>
                    <td>${compra.estado}</td>
                    <td>
                        <form action="../compras" method="POST" style="display:flex; gap:10px;">
                            <input type="hidden" name="accion" value="actualizarEstado">
                            <input type="hidden" name="idCompra" value="${compra.id}">
                            
                            <select name="nuevoEstado">
                                <option value="PENDIENTE" ${compra.estado == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                                <option value="ENVIADO" ${compra.estado == 'ENVIADO' ? 'selected' : ''}>Enviado</option>
                                <option value="ENTREGADO" ${compra.estado == 'ENTREGADO' ? 'selected' : ''}>Entregado</option>
                                <option value="CANCELADO" ${compra.estado == 'CANCELADO' ? 'selected' : ''}>Cancelado</option>
                            </select>
                            <button type="submit">Actualizar</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
