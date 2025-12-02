<%-- 
    Document   : dashboard
    Created on : 2 dic 2025, 00:09:01
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${sessionScope.usuarioLogueado.rolUsuario != 'ADMIN'}">
    <% response.sendRedirect("../login.jsp");%>
</c:if>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Panel de Administración</title>
        <link rel="stylesheet" href="../estilos/styles_index.css">
    </head>
    <body>
        <header>
            <div class="logo">
                <a href="../index.jsp">TIENDA (Volver)</a>
            </div>
            <div class="user_section">
                <span>Admin: <b>${sessionScope.usuarioLogueado.nombre}</b></span>
                <a class="login_btn" href="../UsuariosServlet?accion=logout" style="background-color: #dc3545;">Salir</a>
            </div>
        </header>

        <div class="admin-panel">
            <h1>Panel de Control</h1>
            <p>Bienvenido al área de gestión.</p>

            <div class="admin-grid">
                <div class="card-admin">
                    <h3>Usuarios</h3>
                    <p>Gestionar clientes y administradores</p>
                </div>
                <div class="card-admin">
                    <h3>Productos</h3>
                    <p>Agregar o editar inventario</p>
                </div>
                <div class="card-admin">
                    <h3>Reportes</h3>
                    <p>Ver ventas del mes</p>
                </div>
            </div>
        </div>
    </body>
</html>
