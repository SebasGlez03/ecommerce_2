<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>${producto.nombre} - Detalle</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles_productos.css">
    <script src="https://unpkg.com/lucide@latest"></script>
</head>
<body>

    <header>
        <div class="logo">
            <a href="${pageContext.request.contextPath}/index.jsp">
                <img src="https://soendergaard.com/wp-content/uploads/2024/06/logoipsum-logo-29-1.png" alt="Logo">
            </a>
        </div>
        <nav class="nav_items">
            <a href="${pageContext.request.contextPath}/index.jsp">Volver al Catálogo</a>
        </nav>
        <div class="user_section">
             <c:if test="${not empty sessionScope.usuarioLogueado}">
                <span>${sessionScope.usuarioLogueado.nombre}</span>
            </c:if>
            <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
        </div>
    </header>

    <div class="contenedor">
        <div class="categorias">
            <h3>Categorías</h3>
            <a href="${pageContext.request.contextPath}/productos?categoria=AUDIFONOS">Audífonos</a>
            <a href="${pageContext.request.contextPath}/productos?categoria=MONITORES">Monitores</a>
            <a href="${pageContext.request.contextPath}/productos?categoria=TECLADOS">Teclados</a>
            <a href="${pageContext.request.contextPath}/productos?categoria=RATONES">Ratones</a>
            <a href="${pageContext.request.contextPath}/productos?categoria=SILLAS">Sillas</a>
            <a href="${pageContext.request.contextPath}/productos?categoria=COMPONENTES">Componentes</a>
            <a href="${pageContext.request.contextPath}/productos?categoria=LAPTOPS">Laptops</a>
        </div>

        <div class="product_view_container">
            <div class="product_gallery">
                <div class="main_image_container">
                    <img src="${producto.imagenUrl}" alt="${producto.nombre}" class="main_image">
                </div>
            </div>

            <div class="product_details">
                <h1 class="product_title">${producto.nombre}</h1>
                
                <div style="display:flex; align-items:center; gap:10px; margin-bottom:15px;">
                    <div style="color:gold; font-size:20px;">
                        <c:forEach begin="1" end="5" var="i">
                            <c:choose>
                                <c:when test="${i <= producto.promedioCalificacion}">★</c:when>
                                <c:otherwise>☆</c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                    <span style="color:#888; font-size:14px;">
                        (<fmt:formatNumber value="${producto.promedioCalificacion}" maxFractionDigits="1"/> / 5.0)
                    </span>
                </div>
                    
                <div class="availability">
                    <c:choose>
                        <c:when test="${producto.stock > 0}">
                            <span style="color:var(--color-success);">Disponible (${producto.stock} piezas)</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color:red;">Agotado</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <p class="product_description">${producto.descripcion}</p>
                
                <c:if test="${not empty producto.especificaciones}">
                    <div style="background:rgba(255,255,255,0.05); padding:15px; border-radius:8px; margin:20px 0;">
                        <h4 style="margin-top:0; color:var(--color-bg-6th);">Especificaciones Técnicas</h4>
                        <p style="white-space: pre-line; color:#ccc; font-size:14px; margin:0;">${producto.especificaciones}</p>
                    </div>
                </c:if>
                
                <hr class="divider">
                <div class="product_price">$${producto.precio}</div>
                
                <div class="purchase_controls">
                    <form action="${pageContext.request.contextPath}/carrito" method="POST" style="display:flex; gap:10px; align-items:center;">
                        <input type="hidden" name="accion" value="agregar">
                        <input type="hidden" name="idProducto" value="${producto.id}">
                        
                        <div class="qty_selector">
                            <span>Cant:</span>
                            <input type="number" name="cantidad" class="qty_input" value="1" min="1" max="${producto.stock}">
                        </div>
                        
                        <button type="submit" class="add_to_cart_btn" ${producto.stock == 0 ? 'disabled' : ''} 
                                style="${producto.stock == 0 ? 'background-color:#ccc; cursor:not-allowed;' : ''}">
                            Agregar al carrito
                            <i data-lucide="shopping-cart"></i>
                        </button>
                    </form>
                </div>
            </div>

            <div class="reviews_section_container">
                <hr class="reviews_divider">
                <h3 class="reviews_header">Opiniones de los clientes</h3>

                <c:if test="${not empty sessionScope.usuarioLogueado}">
                    <div class="leave_review_box" style="margin-bottom: 30px;">
                        <h4>Deja tu opinión</h4>
                        <form action="${pageContext.request.contextPath}/resenias" method="POST">
                            <input type="hidden" name="accion" value="agregar">
                            <input type="hidden" name="idProducto" value="${producto.id}">
                            
                            <textarea name="comentario" class="review_input" placeholder="Escribe tu experiencia..." required></textarea>
                            <div class="review_actions">
                                <label>Calificación (1-5): <input type="number" name="calificacion" min="1" max="5" value="5" style="width:50px;"></label>
                                <button type="submit" class="btn_submit_review">Publicar</button>
                            </div>
                        </form>
                    </div>
                </c:if>

                <c:forEach var="resenia" items="${listaResenias}">
                    <div class="review_card">
                        <div class="reviewer_info">
                            <div class="reviewer_avatar">
                                <i data-lucide="user"></i>
                            </div>
                            <span class="reviewer_name">Usuario</span> 
                            <span class="review_date"><fmt:formatDate value="${resenia.fecha}" pattern="dd/MM/yyyy"/></span>
                        </div>
                        <div class="review_content">
                            <div class="review_stars_display">
                                <span style="color:gold;">
                                    <c:forEach begin="1" end="${resenia.calificacion}">★</c:forEach>
                                </span>
                            </div>
                            <p class="review_text">${resenia.comentario}</p>
                            
                            <c:if test="${sessionScope.usuarioLogueado.rolUsuario == 'ADMIN' or sessionScope.usuarioLogueado.id == resenia.idUsuario}">
                                <form action="${pageContext.request.contextPath}/resenias" method="POST">
                                    <input type="hidden" name="accion" value="eliminar">
                                    <input type="hidden" name="idResenia" value="${resenia.id}">
                                    <button type="submit" style="color:red; background:none; border:none; cursor:pointer; font-size:12px;">Eliminar</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
                
                <c:if test="${empty listaResenias}">
                    <p style="color:#888; font-style:italic;">No hay reseñas todavía. ¡Sé el primero en opinar!</p>
                </c:if>
            </div>
        </div>
    </div>

    <script>lucide.createIcons();</script>
</body>
</html>