<%-- 
    Document   : historial
    Created on : 5 dic 2025, 02:29:16
    Author     : Beto_
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %> <%-- Solo si usas fechas --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Mis Pedidos</title>
    <link rel="stylesheet" href="estilos/styles_index.css">
    <link rel="stylesheet" href="estilos/styles_carrito.css"> </head>
<body>
    <div class="cart_container">
        <h2 style="color: white; margin-bottom: 20px;">Mis Pedidos</h2>
        
        <table class="cart_table">
            <thead>
                <tr>
                    <th>ID Pedido</th>
                    <th>Fecha</th>
                    <th>Estado</th>
                    <th>Total</th>
                    <th>Detalles</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${listaCompras}">
                    <tr>
                        <td>${c.id}</td>
                        <td>${c.fechaCompra}</td>
                        <td>${c.estado}</td> <td>$${c.total}</td>
                        <td>
                            ${c.detalles.size()} productos
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <c:if test="${empty listaCompras}">
            <p style="color: #ccc; text-align: center; margin-top: 20px;">No has realizado compras aún.</p>
        </c:if>
    </div>
</body>
</html>