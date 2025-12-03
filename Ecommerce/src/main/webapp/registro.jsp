<%-- 
    Document   : registro
    Created on : 2 dic 2025, 00:06:38
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Registro</title>
        <link rel="stylesheet" href="estilos/styles_login.css">
    </head>
    <body>
        <header>
            <div class="logo"><a href="index.jsp">Logo</a></div>
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
                    <input type="text" name="nombre" required>
                </div>
                <div class="form_group">
                    <label>Email:</label>
                    <input type="email" name="email" required>
                </div>
                <div class="form_group">
                    <label>Teléfono:</label>
                    <input type="text" name="telefono">
                </div>
                <div class="form_group">
                    <label>Dirección:</label>
                    <input type="text" name="direccion">
                </div>
                <div class="form_group">
                    <label>Contraseña:</label>
                    <input type="password" name="password" required>
                </div>

                <button type="submit" class="submit_btn">Registrarse</button>
            </form>
        </div>
    </body>
</html>
