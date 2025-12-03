<%-- 
    Document   : registro
    Created on : 2 dic 2025, 00:06:38
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro</title>
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
                <a class="login_btn" href="login.jsp" style="font-weight: bold;">Iniciar Sesión</a>
                <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
            </div>
        </header>

        <div class="contenedor_login">
            <form class="login_form" action="usuarios" method="POST">
                <h2>Registro</h2>

                <c:if test="${not empty error}">
                    <div style="background-color: #ffdddd; color: #a00; padding: 10px; margin-bottom: 10px;">${error}</div>
                </c:if>

                <input type="hidden" name="accion" value="registrar">

                <div class="form_group">
                    <label>Nombre:</label>
                    <input type="text" name="nombre" required placeholder="Tu nombre completo">
                </div>
                <div class="form_group">
                    <label>Email:</label>
                    <input type="email" name="email" required placeholder="correo@ejemplo.com">
                </div>
                <div class="form_group">
                    <label>Teléfono:</label>
                    <input type="text" name="telefono" placeholder="(644) 000-0000">
                </div>
                <div class="form_group">
                    <label>Dirección:</label>
                    <input type="text" name="direccion" placeholder="Calle, Número, Colonia">
                </div>
                <div class="form_group">
                    <label>Contraseña:</label>
                    <input type="password" name="password" required placeholder="Crea una contraseña segura">
                </div>

                <button type="submit" class="submit_btn">Registrarse</button>
                
                <div class="form_links">
                    <a href="login.jsp">¿Ya tienes cuenta? Inicia Sesión</a>
                </div>
            </form>
        </div>
        <script>lucide.createIcons();</script>
    </body>
</html>
