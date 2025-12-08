<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tu Carrito</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100..900;1,100..900&display=swap">
    <script src="https://unpkg.com/lucide@latest"></script>
    <link rel="stylesheet" href="estilos/styles_carrito.css">
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
            <a href="index.jsp">Seguir Comprando</a>
        </nav>
        <div class="user_section">
             <c:if test="${not empty sessionScope.usuarioLogueado}">
                <span style="color:white; margin-right:10px;">${sessionScope.usuarioLogueado.nombre}</span>
            </c:if>
            <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
        </div>
    </header>

    <div class="cart_container">
        
        <c:if test="${not empty param.error}">
            <div style="background-color: #ffcccc; color: red; padding: 15px; margin-bottom: 20px; border-radius: 5px; text-align: center;">
                Error: ${param.error}
            </div>
        </c:if>

        <c:choose>
            <%-- Si el carrito tiene cosas --%>
            <c:when test="${not empty sessionScope.carrito and sessionScope.carrito.cantidadItems > 0}">
                
                <table class="cart_table">
                    <thead>
                        <tr>
                            <th>Producto</th>
                            <th>Precio</th>
                            <th>Cant.</th>
                            <th>Total</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${sessionScope.carrito.listaItems}">
                            <tr>
                                <td style="text-align: left; display: flex; align-items: center; gap: 10px;">
                                    <img src="${item.imagenUrl}" class="product_thumb" style="width: 50px;">
                                    <span>${item.nombre}</span>
                                </td>
                                <td>$${item.precioUnitario}</td>
                                <td>${item.cantidad}</td>
                                <td>$${item.subtotal}</td>
                                <td>
                                    <form action="carrito" method="POST">
                                        <input type="hidden" name="accion" value="eliminar">
                                        <input type="hidden" name="idProducto" value="${item.productoId}">
                                        <button type="submit" style="color: red; background: none; border: none; cursor: pointer;">
                                            <i data-lucide="trash-2"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <tr style="border-top: 2px solid white;">
                            <td colspan="3" style="text-align: right; font-weight: bold; font-size: 1.2em;">Total a Pagar:</td>
                            <td colspan="2" style="font-weight: bold; font-size: 1.2em; color: #4ade80;">
                                $${sessionScope.carrito.total}
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="next_step_container">
                    <a href="pago.jsp" class="btn_next_step" style="text-decoration:none;">Proceder al Pago</a>
                </div>

            </c:when>

            <%-- Si el carrito está vacío --%>
            <c:otherwise>
                <div class="empty_cart_view">
                    <div class="empty_cart_box">
                        <div class="empty_title">Tu carrito está vacío</div>
                        <i data-lucide="shopping-cart" style="width: 48px; height: 48px; color: #888;"></i>
                        <br><br>
                        <a href="index.jsp" class="btn_featured" style="display:inline-block; text-decoration:none;">
                            Ir al Catálogo
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <script>lucide.createIcons();</script>
</body>
</html>