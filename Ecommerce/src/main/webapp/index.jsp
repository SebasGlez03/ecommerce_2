<%-- 
    Document   : index
    Created on : 1 dic 2025, 23:42:59
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                <a href="#">Categorías</a>
                <a href="#">Ofertas</a>
                
                <c:if test="${sessionScope.usuarioLogueado.rolUsuario == 'ADMIN'}">
                    <span style="color: var(--color-txt-secondary);">|</span>
                    <a href="admin/dashboard.jsp" style="color: #ff6b6b; font-weight: bold;">Panel Admin</a>
                </c:if>
            </nav>

            <div class="search_container">
                <input type="text" placeholder="Buscar productos...">
                <i data-lucide="search" style="width: 18px; color: var(--color-txt-secondary);"></i>
            </div>

            <div class="user_section">
                <c:choose>
                    <c:when test="${not empty sessionScope.usuarioLogueado}">
                        <span style="margin-right: 15px; font-size: 14px;">Hola, <b>${sessionScope.usuarioLogueado.nombre}</b></span>
                        <a class="login_btn" href="usuarios?accion=logout" style="color: #ff6b6b; font-weight: bold; font-size: 14px;">(Cerrar Sesión)</a>
                    </c:when>
                    <c:otherwise>
                        <a class="login_btn" href="login.jsp" style="font-weight: bold;">Iniciar Sesión</a>
                    </c:otherwise>
                </c:choose>
                <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
            </div>
        </header>

        <div class="contenedor">
            <div class="categorias">
                <h3 id="titulo_categorias">Categorías</h3>
                <a class="categorias_disponibles" href="">Audifonos</a><br>
                <a class="categorias_disponibles" href="">Monitores</a><br>
                <a class="categorias_disponibles" href="">Teclados</a><br>
                <a class="categorias_disponibles" href="">Ratones</a><br>
                <a class="categorias_disponibles" href="">Sillas</a>
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
                        <div class="general_product_card">
                            <a href="producto.html"><img src="https://ddtech.mx/assets/uploads/5997d0cf7ca7c87102910fcc679904d2.png" alt="P1"></a>
                            <div class="general_product_info"><p>GPU ASUS</p><p>$6,999.00</p></div>
                        </div>
                        <div class="general_product_card">
                            <a href="producto.html"><img src="https://ddtech.mx/assets/uploads/a37e74c2b15f984d31e6a4748886188c.png" alt="P2"></a>
                            <div class="general_product_info"><p>GPU GIGABYTE</p><p>$11,999.00</p></div>
                        </div>
                        <div class="general_product_card">
                            <a href="producto.html"><img src="https://ddtech.mx/assets/uploads/840df05686e37516f65155f8c0caeeaa.jpg" alt="P3"></a>
                            <div class="general_product_info"><p>Ventiladores</p><p>$399.00</p></div>
                        </div>
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
