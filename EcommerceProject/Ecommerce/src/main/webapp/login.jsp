<%-- 
    Document   : login
    Created on : 1 dic 2025, 23:09:23
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Iniciar Sesión</title>
        <link rel="stylesheet" href="estilos/styles_login.css">
        <script src="https://unpkg.com/lucide@latest"></script>
    </head>
    <body>
        <header>
            <div class="logo">
                <a href="index.jsp">
                    <img src="https://soendergaard.com/wp-content/uploads/2024/06/logoipsum-logo-29-1.png" alt="Logo">
                </a>
            </div>

            <nav class="nav_items">
                <a href="index.jsp">Volver al Inicio</a>
            </nav>

            <div class="search_container" style="visibility: hidden; opacity: 0;">
                <input type="text">
            </div>

            <div class="user_section">
                <a class="login_btn" href="registro.jsp" style="font-weight: bold;">Registrarse</a>
                <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
            </div>
        </header>

        <div class="contenedor_login">
            <form class="login_form" action="usuarios" method="POST">
                <h2>Iniciar Sesión</h2>
                
                <c:if test="${not empty error}">
                    <div class="c_if_error">${error}</div>
                </c:if>
                <c:if test="${not empty mensajeExito}">
                    <div class="c_if_exito">${mensajeExito}</div>
                </c:if>

                <input type="hidden" name="accion" value="login">

                <div class="form_group">
                    <label for="email">Correo Electrónico:</label>
                    <input type="email" name="email" id="email" required placeholder="ejemplo@correo.com">
                </div>

                <div class="form_group">
                    <label for="password">Contraseña:</label>
                    <input type="password" name="password" id="password" required placeholder="********">
                </div>

                <button type="submit" class="submit_btn">Ingresar</button>

                <div class="form_links">
                    <a href="registro.jsp">¿No tienes cuenta? Regístrate</a>
                </div>
            </form>
        </div>
        <script>lucide.createIcons();</script>
    </body>
</html>
