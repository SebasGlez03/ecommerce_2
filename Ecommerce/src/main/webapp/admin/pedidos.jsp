<%-- 
    Document   : pedidos
    Created on : 7 dic 2025, 23:48:53
    Author     : Beto_
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Pedidos</title>
    <link rel="stylesheet" href="../estilos/styles_index.css">
    <script src="https://unpkg.com/lucide@latest"></script>
    <style>
        /* Estilos inline simples para mantener consistencia con productos.jsp */
        .admin-container { padding: 40px; max-width: 1200px; margin: 0 auto; color: white;}
        table { background: white; color: #333; }
        th { padding: 15px; }
        td { padding: 10px; text-align: center; }
    </style>
</head>
<body style="background-color: #0a2034;">

    <%-- HEADER --%>
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
        <h1>Administración de Pedidos</h1>
        <a href="dashboard.jsp" style="color:#ccc;">&larr; Volver al Dashboard</a>
        
        <table border="1" style="width:100%; border-collapse: collapse; margin-top: 20px;">
            <thead>
                <tr style="background-color: #003d72; color: white;">
                    <th>ID</th>
                    <th>Fecha</th>
                    <th>Usuario ID</th>
                    <th>Total</th>
                    <th>Estado Actual</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="compra" items="${listaComprasAdmin}">
                    <tr>
                        <td>${compra.id}</td>
                        <td>${compra.fechaCompra}</td>
                        <td>${compra.usuarioId}</td>
                        <td>$${compra.total}</td>
                        <td>${compra.estado}</td>
                        <td>
                            <form action="../compras" method="POST" style="display:flex; gap:10px; justify-content:center;">
                                <input type="hidden" name="accion" value="actualizarEstado">
                                <input type="hidden" name="idCompra" value="${compra.id}">
                                
                                <select name="nuevoEstado">
                                    <option value="PENDIENTE" ${compra.estado == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                                    <option value="ENVIADO" ${compra.estado == 'ENVIADO' ? 'selected' : ''}>Enviado</option>
                                    <option value="ENTREGADO" ${compra.estado == 'ENTREGADO' ? 'selected' : ''}>Entregado</option>
                                    <option value="CANCELADO" ${compra.estado == 'CANCELADO' ? 'selected' : ''}>Cancelado</option>
                                </select>
                                <button type="submit" style="cursor:pointer;">Actualizar</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listaComprasAdmin}">
                    <tr><td colspan="6" style="padding:20px;">No hay pedidos registrados.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
    <script>lucide.createIcons();</script>
</body>
</html>