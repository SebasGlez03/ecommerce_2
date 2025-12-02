<%-- 
    Document   : index
    Created on : 1 dic 2025, 23:42:59
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ecommerce</title>
        <link rel="stylesheet" href="estilos/styles_index.css">
    </head>
    <body>
        <header>
            <div class="logo">
                <a href="index.jsp">
                    <img src="https://soendergaard.com/wp-content/uploads/2024/06/logoipsum-logo-29-1.png" alt="Logo">
                </a>
            </div>

            <nav class="nav_items">
                <a href="#">Categorías</a>
                <a href="#">Ofertas</a>

                <!-- TODO: No se si vaya a funcionar esto, porque estamos hardcodeando el "ADMIN" en lugar de agregar rolUsuario.Admin -->
                <c:if test="${sessionScope.usuarioLogueado.rolUsuario == 'ADMIN'}">
                    <a href="admin/dashboard.jsp" style="color: red; font-weight: bold;">Panel Admin</a>
                </c:if>

                <div class="user_section">
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuarioLogueado}">
                            <span style="margin-right: 15px;">Hola, <b>${sessionScope.usuarioLogueado.nombre}</b></span>
                            <a class="login_btn" href="UsuariosServlet?accion=logout" style="background-color: #dc3545;">Cerrar Sesión</a>
                        </c:when>
                        <c:otherwise>
                            <a class="login_btn" href="login.jsp">Iniciar Sesión</a>
                        </c:otherwise>
                    </c:choose>

                    <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
                </div>
            </nav>
        </header>

        <div class="contenedor">
            <div style="padding: 20px; text-align: center;">
                <h1>Bienvenido a la Tienda</h1>
                <p>Contenido principal de productos...</p>
            </div>
        </div>

        <div class="contenedor_inferior">
            <footer class="footer">
                <p>&copy; 2025 Ecommerce. Todos los derechos reservados.</p>
            </footer>
        </div>
    </body>
</html>
