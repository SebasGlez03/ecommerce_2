<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/c" %>

<div class="product_view_container">
    <div class="product_gallery">
        <div class="main_image_container">
            <img src="${producto.imagenUrl}" alt="${producto.nombre}" class="main_image">
        </div>
    </div>

    <div class="product_details">
        <h1 class="product_title">${producto.nombre}</h1>
        
        <div class="availability">
            <c:choose>
                <c:when test="${producto.stock > 0}">Disponible (${producto.stock})</c:when>
                <c:otherwise><span style="color:red;">Agotado</span></c:otherwise>
            </c:choose>
        </div>

        <p class="product_description">${producto.descripcion}</p>
        <hr class="divider">
        <div class="product_price">$${producto.precio}</div>
        
        <div class="purchase_controls">
            <form action="carrito" method="POST">
                <input type="hidden" name="accion" value="agregar">
                <input type="hidden" name="idProducto" value="${producto.id}">
                
                <div class="qty_selector">
                    <span>Cantidad:</span>
                    <input type="number" name="cantidad" class="qty_input" value="1" min="1" max="${producto.stock}">
                </div>
                
                <button type="submit" class="add_to_cart_btn" ${producto.stock == 0 ? 'disabled' : ''}>
                    Agregar al carrito
                    <i data-lucide="shopping-cart" style="width: 18px;"></i>
                </button>
            </form>
        </div>
    </div>
</div>