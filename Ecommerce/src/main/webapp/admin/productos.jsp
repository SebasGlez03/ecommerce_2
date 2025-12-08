<%-- 
    Document   : productos
    Created on : 7 dic 2025, 23:49:24
    Author     : Beto_
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Productos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles_index.css">
    <script src="https://unpkg.com/lucide@latest"></script>
    <style>
        .admin-container { padding: 40px; max-width: 1200px; margin: 0 auto; }
        .admin-table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; color: #333; }
        .admin-table th { background-color: #003d72; color: white; padding: 15px; }
        .admin-table td { padding: 12px; border-bottom: 1px solid #eee; text-align: center; }
        .btn-new { background-color: #4ade80; color: #003d72; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold; float: right; }
        .action-btn { background: none; border: none; cursor: pointer; }
    </style>
</head>
<body style="background-color: #0a2034;">
    
    <%-- SOLUCIÓN PROBLEMA 3: HEADER AGREGADO --%>
    <header>
        <div class="logo">
            <a href="${pageContext.request.contextPath}/index.jsp">
                <img src="https://soendergaard.com/wp-content/uploads/2024/06/logoipsum-logo-29-1.png" alt="Logo">
            </a>
        </div>

        <nav class="nav_items">
            <a href="${pageContext.request.contextPath}/index.jsp">Ir a Tienda</a>
            <span style="color: var(--color-txt-secondary);">|</span>
            <a href="${pageContext.request.contextPath}/admin/dashboard.jsp" style="color: var(--color-bg-5th); font-weight: bold;">Dashboard</a>
        </nav>

        <div class="user_section">
            <span style="margin-right: 15px; font-size: 14px;">Hola, <b>${sessionScope.usuarioLogueado.nombre}</b></span>
            <a class="login_btn" href="${pageContext.request.contextPath}/usuarios?accion=logout" style="color: #ff6b6b; font-weight: bold; font-size: 14px;">(Salir)</a>
            <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
        </div>
    </header>
    <%-- FIN HEADER --%>

    <div class="admin-container">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
                <h1 style="color:white;">Inventario de Productos</h1>
                <a href="${pageContext.request.contextPath}/admin/dashboard.jsp" style="color: #ccc;">&larr; Volver al Dashboard</a>
            </div>
            
            <a href="?accion=formulario" class="btn-new">
                <i data-lucide="plus-circle" style="vertical-align: middle;"></i> Nuevo Producto
            </a>
        </div>
        
        <table class="admin-table">
            <thead>
                <tr>
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
                        <td><img src="${p.imagenUrl}" width="50" height="50" style="object-fit: contain;"></td>
                        <td>${p.nombre}</td>
                        <td>$${p.precio}</td>
                        <td style="${p.stock == 0 ? 'color:red; font-weight:bold;' : ''}">${p.stock}</td>
                        <td>
                            <a href="?accion=formulario&id=${p.id}" class="action-btn" title="Editar">
                                <i data-lucide="edit" style="color: #003d72;"></i>
                            </a>
                            
                            <form action="productos" method="POST" style="display:inline;">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="idProducto" value="${p.id}">
                                <button type="submit" class="action-btn" onclick="return confirm('¿Eliminar?');">
                                    <i data-lucide="trash-2" style="color: #ef4444;"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listaProductos}">
                    <tr><td colspan="5" style="padding:30px;">No hay productos.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
    <script>lucide.createIcons();</script>
</body>
</html>