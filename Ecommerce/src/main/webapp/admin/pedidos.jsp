<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <title>Gestión de Pedidos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles_index.css">
    <script src="https://unpkg.com/lucide@latest"></script>
    <style>
        /* --- ESTILOS CONSISTENTES CON USUARIOS.JSP --- */
        .admin-container { padding: 40px; max-width: 1200px; margin: 0 auto; }
        
        .admin-table { 
            width: 100%; 
            border-collapse: collapse; 
            margin-top: 20px; 
            background: white; 
            color: #333; 
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        .admin-table th { 
            background-color: #003d72; 
            color: white; 
            padding: 15px; 
            text-align: left;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .admin-table td { 
            padding: 15px; 
            border-bottom: 1px solid #eee; 
            vertical-align: middle;
        }

        /* Badges de Estado (Colores según el estado) */
        .badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
            display: inline-block;
            text-align: center;
        }
        
        .status-pendiente { background-color: #fef3c7; color: #b45309; } /* Amarillo */
        .status-enviado   { background-color: #dbeafe; color: #1e40af; } /* Azul */
        .status-entregado { background-color: #d1fae5; color: #065f46; } /* Verde */
        .status-cancelado { background-color: #fee2e2; color: #991b1b; } /* Rojo */

        /* Controles de formulario (Select y Botón) */
        .form-update {
            display: flex;
            gap: 8px;
            align-items: center;
        }

        .select-estado {
            padding: 6px;
            border-radius: 4px;
            border: 1px solid #ccc;
            background-color: #f9f9f9;
            font-size: 13px;
            cursor: pointer;
        }

        .btn-update {
            background-color: var(--color-bg-5th);
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 13px;
            font-weight: bold;
            transition: background 0.3s;
        }
        .btn-update:hover { background-color: var(--color-bg-6th); }
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
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <h1 style="color: white; margin: 0;">Administración de Pedidos</h1>
            <a href="admin/dashboard.jsp" style="color: #ccc; text-decoration: none;">&larr; Volver</a>
        </div>
        
        <table class="admin-table">
            <thead>
                <tr>
                    <th>Folio / ID</th>
                    <th>Fecha</th>
                    <th>Cliente (ID)</th>
                    <th>Total</th>
                    <th>Estado Actual</th>
                    <th>Actualizar Estado</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="compra" items="${listaComprasAdmin}">
                    <tr>
                        <td style="font-family: monospace; font-size: 12px; color: #666;">
                            ${compra.id}
                        </td>
                        <td>
                            <fmt:formatDate value="${compra.fechaCompra}" pattern="dd/MM/yyyy HH:mm"/>
                        </td>
                        <td>
                            <span title="${compra.usuarioId}" style="cursor: help; border-bottom: 1px dotted #999;">
                                Ver ID
                            </span>
                        </td>
                        <td style="font-weight: bold; color: #0a2034;">
                            $${compra.total}
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${compra.estado == 'PENDIENTE'}">
                                    <span class="badge status-pendiente">Pendiente</span>
                                </c:when>
                                <c:when test="${compra.estado == 'ENVIADO'}">
                                    <span class="badge status-enviado">Enviado</span>
                                </c:when>
                                <c:when test="${compra.estado == 'ENTREGADO'}">
                                    <span class="badge status-entregado">Entregado</span>
                                </c:when>
                                <c:when test="${compra.estado == 'CANCELADO'}">
                                    <span class="badge status-cancelado">Cancelado</span>
                                </c:when>
                                <c:otherwise>${compra.estado}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/compras" method="POST" class="form-update">
                                <input type="hidden" name="accion" value="actualizarEstado">
                                <input type="hidden" name="idCompra" value="${compra.id}">
                                
                                <select name="nuevoEstado" class="select-estado">
                                    <option value="PENDIENTE" ${compra.estado == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                                    <option value="ENVIADO" ${compra.estado == 'ENVIADO' ? 'selected' : ''}>Enviado</option>
                                    <option value="ENTREGADO" ${compra.estado == 'ENTREGADO' ? 'selected' : ''}>Entregado</option>
                                    <option value="CANCELADO" ${compra.estado == 'CANCELADO' ? 'selected' : ''}>Cancelado</option>
                                </select>
                                
                                <button type="submit" class="btn-update">
                                    <i data-lucide="save" style="width: 14px; height: 14px; vertical-align: middle;"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                
                <c:if test="${empty listaComprasAdmin}">
                    <tr>
                        <td colspan="6" style="text-align: center; padding: 40px; color: #666;">
                            No hay pedidos registrados en el sistema.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
    <script>lucide.createIcons();</script>
</body>
</html>