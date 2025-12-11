<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Usuarios</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles_index.css">
    <script src="https://unpkg.com/lucide@latest"></script>
    <style>
        /* Estilos específicos para la tabla, consistentes con productos.jsp */
        .admin-container { padding: 40px; max-width: 1200px; margin: 0 auto; }
        
        .admin-table { 
            width: 100%; 
            border-collapse: collapse; 
            margin-top: 20px; 
            background: white; 
            color: #333; 
            border-radius: 8px; /* Borde redondeado */
            overflow: hidden;
        }
        
        .admin-table th { 
            background-color: #003d72; 
            color: white; 
            padding: 15px; 
            text-align: left;
        }
        
        .admin-table td { 
            padding: 12px 15px; 
            border-bottom: 1px solid #eee; 
            text-align: left; /* Alineación a la izquierda se ve mejor en nombres */
        }
        
        /* Badges para el estado */
        .badge {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: bold;
        }
        .badge-active { background-color: #d1fae5; color: #065f46; } /* Verde suave */
        .badge-inactive { background-color: #fee2e2; color: #991b1b; } /* Rojo suave */

        /* Botones de acción */
        .btn-action {
            border: none;
            background: none;
            cursor: pointer;
            transition: transform 0.2s;
        }
        .btn-action:hover { transform: scale(1.1); }
    </style>
</head>
<body style="background-color: #0a2034;">
    
    <%-- HEADER COMPLETO DEL PROYECTO --%>
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
            <h1 style="color: white; margin: 0;">Directorio de Usuarios</h1>
            <a href="${pageContext.request.contextPath}/admin/dashboard.jsp" style="color: #ccc; text-decoration: none;">&larr; Volver</a>
        </div>
        
        <table class="admin-table">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Rol</th>
                    <th style="text-align: center;">Estado</th>
                    <th style="text-align: center;">Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${listaUsuarios}">
                    <tr>
                        <td style="font-weight: bold;">${u.nombre}</td>
                        <td>${u.email}</td>
                        <td>
                            <c:if test="${u.rolUsuario == 'ADMIN'}"><span style="color: #2a7dc7;">Administrador</span></c:if>
                            <c:if test="${u.rolUsuario != 'ADMIN'}">Cliente</c:if>
                        </td>
                        <td style="text-align: center;">
                            <c:choose>
                                <c:when test="${u.esActivo}">
                                    <span class="badge badge-active">Activo</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-inactive">Inactivo</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td style="text-align: center;">
                            <c:if test="${u.id != sessionScope.usuarioLogueado.id}">
                                <form action="${pageContext.request.contextPath}/usuarios" method="POST" style="display: inline;">
                                    <input type="hidden" name="accion" value="cambiarEstado">
                                    <input type="hidden" name="idUsuario" value="${u.id}">
                                    
                                    <c:choose>
                                        <c:when test="${u.esActivo}">
                                            <input type="hidden" name="nuevoEstado" value="false">
                                            <button type="submit" class="btn-action" title="Desactivar Usuario">
                                                <i data-lucide="user-x" style="color: #ef4444;"></i>
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="nuevoEstado" value="true">
                                            <button type="submit" class="btn-action" title="Activar Usuario">
                                                <i data-lucide="user-check" style="color: #22c55e;"></i>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </c:if>
                            <c:if test="${u.id == sessionScope.usuarioLogueado.id}">
                                <span style="font-size: 12px; color: #888;">(Tu cuenta)</span>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <c:if test="${empty listaUsuarios}">
            <p style="color: white; text-align: center; margin-top: 30px;">No se encontraron usuarios registrados.</p>
        </c:if>
    </div>

    <script>lucide.createIcons();</script>
</body>
</html>