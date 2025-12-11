<%-- 
    Document   : index
    Created on : 1 dic 2025, 23:42:59
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty listaProductos and param.accion == null}">
    <c:redirect url="productos"/>
</c:if>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ecommerce</title>
        <link rel="stylesheet" href="estilos/styles_index.css">
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

                <a href="#">Ofertas</a>

                <c:if test="${sessionScope.usuarioLogueado.rolUsuario == 'ADMIN'}">
                    <span style="color: var(--color-txt-secondary);">|</span>
                    <a href="admin/dashboard.jsp" style="color: #ff6b6b; font-weight: bold;">Panel Admin</a>
                </c:if>
            </nav>

            <div class="search_container">
                <form action="productos" method="GET" style="width: 100%; display: flex; align-items: center;">
                    <%-- Input principal de búsqueda --%>
                    <input type="text" name="busqueda" placeholder="Buscar productos..." value="${param.busqueda}">

                    <%-- TRUCO: Si el usuario ya está en una categoría, la mantenemos oculta en el formulario --%>
                    <c:if test="${not empty param.categoria}">
                        <input type="hidden" name="categoria" value="${param.categoria}">
                    </c:if>

                    <%-- El icono ahora es un botón de submit --%>
                    <button type="submit" style="background: none; border: none; cursor: pointer; display: flex; align-items: center;">
                        <i data-lucide="search" style="width: 18px; color: var(--color-txt-secondary);"></i>
                    </button>
                </form>
            </div>

            <div class="user_section">
                <c:choose>
                    <%-- CASO 1: USUARIO LOGUEADO --%>
                    <c:when test="${not empty sessionScope.usuarioLogueado}">
                        <span style="margin-right: 15px; font-size: 14px;">Hola, <b>${sessionScope.usuarioLogueado.nombre}</b></span>

                        <a class="login_btn" href="usuarios?accion=logout" style="color: #ff6b6b; font-weight: bold; font-size: 14px; margin-right: 10px;">(Cerrar Sesión)</a>

                        <%-- Aquí envolvemos la imagen en un enlace hacia el perfil --%>
                        <a href="usuarios?accion=verPerfil" title="Editar mi perfil">
                            <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar" style="cursor: pointer;">
                        </a>
                    </c:when>

                    <%-- CASO 2: USUARIO NO LOGUEADO --%>
                    <c:otherwise>
                        <a class="login_btn" href="login.jsp" style="font-weight: bold; margin-right: 10px;">Iniciar Sesión</a>

                        <%-- Si no está logueado, la imagen puede llevar al login también --%>
                        <a href="login.jsp" title="Iniciar Sesión">
                            <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>

        <div class="contenedor">
            <div class="categorias">
                <h3 id="titulo_categorias">Categorías</h3>

                <a class="categorias_disponibles" href="productos?categoria=AUDIFONOS">Audífonos</a>
                <a class="categorias_disponibles" href="productos?categoria=MONITORES">Monitores</a>
                <a class="categorias_disponibles" href="productos?categoria=TECLADOS">Teclados</a>
                <a class="categorias_disponibles" href="productos?categoria=RATONES">Ratones</a>
                <a class="categorias_disponibles" href="productos?categoria=SILLAS">Sillas</a>
                <a class="categorias_disponibles" href="productos?categoria=COMPONENTES">Componentes</a>
                <a class="categorias_disponibles" href="productos?categoria=LAPTOPS">Laptops</a>


                <div style="margin-top: 30px; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 20px;">
                    <h3>Rango de Precio</h3>
                    <form action="productos" method="GET" style="display: flex; flex-direction: column; gap: 10px;">

                        <%-- Mantenemos la categoría (Esto ya lo tenías) --%>
                        <c:if test="${not empty param.categoria}">
                            <input type="hidden" name="categoria" value="${param.categoria}">
                        </c:if>

                        <%-- NUEVO: Mantenemos la búsqueda si existe --%>
                        <c:if test="${not empty param.busqueda}">
                            <input type="hidden" name="busqueda" value="${param.busqueda}">
                        </c:if>

                        <input type="number" name="precioMin" placeholder="Mínimo" min="0" step="0.01" class="input-precio" value="${param.precioMin}">
                        <input type="number" name="precioMax" placeholder="Máximo" min="0" step="0.01" class="input-precio" value="${param.precioMax}">

                        <button type="submit" class="btn-filtrar">Filtrar</button>

                        <a href="index.jsp" style="font-size: 12px; text-align: center; color: #888; margin-top: 5px;">Limpiar Filtros</a>
                    </form>
                </div>
            </div>

            <div class="info">
                <div class="ofertas">
                    <img class="img_oferta" src="https://www.cyberpuerta.mx/out/pictures/banner-manager/bundle-nvidia-arc-raiders-oct25-hb-cp.jpg?1760454915" alt="Ofertas">
                </div>

                <div class="titulo_productos_destacados">
                    <h2>Productos Destacados</h2>
                </div>

                <div class="carousel_container">
                    <div class="carousel_track">
                        <div class="product_card">
                            <img src="https://ddtech.mx/assets/uploads/27dc82b93650bc921da9076e74babdbe.png" alt="Prod 1">
                            <div class="product_info"><p>Teclado Gamer</p><p>$399.00</p></div>
                        </div>
                        <div class="product_card">
                            <img src="https://ddtech.mx/assets/uploads/57867eb0af4d762c9a9b7b84fec4702a.png" alt="Prod 2">
                            <div class="product_info"><p>Diadema Gamer</p><p>$5,299.00</p></div>
                        </div>
                        <div class="product_card">
                            <img src="https://ddtech.mx/assets/uploads/c10a87543113eb7c3bb9caaf958119bf.png" alt="Prod 3">
                            <div class="product_info"><p>Mochila Gamer</p><p>$799.00</p></div>
                        </div>
                        <div class="product_card">
                            <img src="https://ddtech.mx/assets/uploads/e22f6d5e9dd5c30f6737665e5da99899.png" alt="Prod 4">
                            <div class="product_info"><p>Mouse Corsair</p><p>$2,899.00</p></div>
                        </div>
                    </div>
                </div>

                <div class="titulo_productos_generales">
                    <h2>Todos los Productos</h2>
                </div>



                <div class="general_products_container">
                    <div class="general_products_track">

                        <c:forEach var="p" items="${listaProductos}">
                            <div class="general_product_card">
                                <a href="productos?accion=ver&id=${p.id}">
                                    <img src="${p.imagenUrl}" alt="${p.nombre}">
                                </a>
                                <div class="general_product_info">
                                    <p><b>${p.nombre}</b></p>
                                    <p style="color: #4ade80;">$${p.precio}</p>
                                </div>

                                <form action="carrito" method="POST" style="margin-top: 5px;">
                                    <input type="hidden" name="accion" value="agregar">
                                    <input type="hidden" name="idProducto" value="${p.id}">
                                    <input type="hidden" name="cantidad" value="1"> <button class="add_to_cart_btn" style="padding: 5px 10px; font-size: 12px; width: 100%;">
                                        Agregar
                                    </button>
                                </form>
                            </div>
                        </c:forEach>

                        <c:if test="${empty listaProductos}">
                            <p style="color: white; text-align: center; width: 100%;">
                                No se encontraron productos disponibles.
                            </p>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>

        <div class="contenedor_inferior">
            <footer class="footer">
                <p>&copy; 2025 Ecommerce. Todos los derechos reservados.</p>
                <p>Hecho por Equipo #2</p>
            </footer>
        </div>
        <script>lucide.createIcons();</script>
    </body>
</html>
