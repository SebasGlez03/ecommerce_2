<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Perfil</title>
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
            <a href="index.jsp">Volver a Tienda</a>
        </nav>
        <div class="user_section">
            <span style="margin-right: 15px; font-size: 14px;">Hola, <b>${sessionScope.usuarioLogueado.nombre}</b></span>
            <a class="login_btn" href="usuarios?accion=logout" style="color: #ff6b6b; font-weight: bold; font-size: 14px;">(Salir)</a>
        </div>
    </header>

    <div class="contenedor_login">
        <form class="login_form" action="usuarios" method="POST">
            <h2>Mi Perfil</h2>

            <c:if test="${not empty error}">
                <div class="c_if_error">${error}</div>
            </c:if>
            <c:if test="${not empty mensajeExito}">
                <div class="c_if_exito">${mensajeExito}</div>
            </c:if>

            <input type="hidden" name="accion" value="editar">

            <div class="form_group">
                <label>Nombre:</label>
                <input type="text" name="nombre" value="${sessionScope.usuarioLogueado.nombre}" required>
            </div>
            
            <div class="form_group">
                <label>Email:</label>
                <input type="email" name="email" value="${sessionScope.usuarioLogueado.email}" required>
            </div>
            
            <div class="form_group">
                <label>Teléfono:</label>
                <input type="text" name="telefono" value="${sessionScope.usuarioLogueado.telefono}">
            </div>
            
            <div class="form_group">
                <label>Dirección:</label>
                <input type="text" name="direccion" value="${sessionScope.usuarioLogueado.direccion}">
            </div>
            
            <div class="form_group">
                <label>Nueva Contraseña (Opcional):</label>
                <input type="password" name="password" placeholder="Dejar vacía para no cambiar">
            </div>

            <button type="submit" class="submit_btn">Guardar Cambios</button>
        </form>
    </div>
    <script>lucide.createIcons();</script>
</body>
</html>