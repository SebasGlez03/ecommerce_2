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
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Panel de Administración</title>
        <link rel="stylesheet" href="../estilos/styles_index.css">
        <script src="https://unpkg.com/lucide@latest"></script>
    </head>
    <body>
        <header>
            <div class="logo">
                <a href="../index.jsp">
                    <img src="https://soendergaard.com/wp-content/uploads/2024/06/logoipsum-logo-29-1.png" alt="Logo">
                </a>
            </div>

            <nav class="nav_items">
                <a href="../index.jsp">Ir a Tienda</a>
                <span style="color: var(--color-txt-secondary);">|</span>
                <span style="color: var(--color-bg-5th); font-weight: bold;">Panel de Control</span>
            </nav>

            <div class="search_container" style="visibility: hidden; opacity: 0; width: 50px;"></div>

            <div class="user_section">
                <span style="margin-right: 15px; font-size: 14px;">Hola, <b>${sessionScope.usuarioLogueado.nombre}</b></span>
                <a class="login_btn" href="../usuarios?accion=logout" style="color: #ff6b6b; font-weight: bold; font-size: 14px;">(Cerrar Sesión)</a>
                <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
            </div>
        </header>

        <div class="admin-panel">
            <h1>Panel de Control</h1>
            <p>Bienvenido al área de gestión administrativa.</p>

            <div class="admin-grid">
                <div class="card-admin" onclick="alert('Funcionalidad de gestión de usuarios pendiente (Req 1.3)')">
                    <i data-lucide="users" style="width: 48px; height: 48px; color: var(--color-bg-6th);"></i>
                    <h3>Usuarios</h3>
                    <p>Gestionar clientes y administradores</p>
                </div>

                <a href="../productos?accion=listarAdmin" style="text-decoration:none;">
                    <div class="card-admin" onclick="window.location.href='../productos?accion=listarAdmin'" style="cursor: pointer;">
                        <i data-lucide="package" style="width: 48px; height: 48px; color: var(--color-bg-6th);"></i>
                        <h3>Productos</h3>
                        <p>Administrar Inventario</p> 
                    </div>
                </a>

                <a href="../compras?accion=adminListar" style="text-decoration:none;">
                    <div class="card-admin">
                        <i data-lucide="bar-chart-3" style="width: 48px; height: 48px; color: var(--color-bg-6th);"></i>
                        <h3>Pedidos</h3>
                        <p>Ver ventas y cambiar estados</p>
                    </div>
                </a>
            </div>
        </div>

        <div class="contenedor_inferior">
            <footer class="footer">
                <p>&copy; 2025 Ecommerce. Panel Administrativo.</p>
            </footer>
        </div>

        <script>
            lucide.createIcons();
        </script>
    </body>
</html>
