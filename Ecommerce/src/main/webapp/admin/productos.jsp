<%-- 
    Document   : productos
    Created on : 7 dic 2025, 23:49:24
    Author     : Beto_
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Productos</title>
    <link rel="stylesheet" href="../estilos/styles_index.css">
</head>
<body>
    <h1>Inventario</h1>
    <a href="dashboard.jsp">Volver</a> | 
    <a href="../productos?accion=formulario" style="background:green; color:white; padding:5px;">+ Nuevo Producto</a>
    
    <table border="1" style="width:100%; margin-top:20px;">
        <thead>
            <tr style="background:#003d72; color:white;">
                <th>Imagen</th>
                <th>Nombre</th>
                <th>Precio</th>
                <th>Stock</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="p" items="${listaProductos}">
                <tr>
                    <td><img src="${p.imagenUrl}" width="50"></td>
                    <td>${p.nombre}</td>
                    <td>$${p.precio}</td>
                    <td>${p.stock}</td>
                    <td>
                        <a href="../productos?accion=formulario&id=${p.id}">Editar</a>
                        
                        <form action="../productos" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="idProducto" value="${p.id}">
                            <button type="submit" onclick="return confirm('¿Seguro?');" style="color:red;">X</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
