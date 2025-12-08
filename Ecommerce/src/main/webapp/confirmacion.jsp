<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/c" %>
<%@taglib prefix="fmt" uri="http://jakarta.ee/tags/fmt" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ConfirmaciÃ³n - Ecommerce</title>
    <!-- Fuente Roboto -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100..900;1,100..900&display=swap">
    <!-- Iconos Lucide -->
    <script src="https://unpkg.com/lucide@latest"></script>
    <link rel="stylesheet" href="estilos/styles_confirmacion.css">
</head>

<body>

    <header>
        <div class="logo">
            <a href="index.html">
                <img src="https://soendergaard.com/wp-content/uploads/2024/06/logoipsum-logo-29-1.png" alt="Logo">
            </a>
        </div>

        <nav class="nav_items">
            <a href="">Item 1</a>
            <a href="">Item 2</a>
            <a href="">Item 3</a>
        </nav>

        <div class="search_container">
            <input type="text" placeholder="">
            <i data-lucide="search" style="width: 18px; color: var(--color-txt-secondary);"></i>
        </div>

        <div class="user_section">
            <a class="login_btn" href="login.html">Iniciar SesiÃ³n</a>
            <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
        </div>
    </header>

    <div class="confirmation_container">
        
        <div class="confirmation_card">
            <h1 class="page_title">ConfirmaciÃ³n del pedido</h1>

            <!-- SecciÃ³n Productos -->
            <label class="section_label">Detalle de los productos</label>
            <hr class="section_divider">

            <table class="products_table">
                <thead>
                    <tr>
                        <th style="width: 15%;">Imagen</th>
                        <th style="width: 50%;">DescripciÃ³n del producto</th>
                        <th style="width: 15%;">Cantidad</th>
                        <th style="width: 20%;">Precio Unitario</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detalle" items="${compra.detalles}">
                        <tr>
                            <td class="prod_img_cell">
                                <i data-lucide="package" style="color: #ccc;"></i>
                            </td>
                            <td>
                                <div class="prod_desc">
                                    ${detalle.nombreProducto}
                                </div>
                            </td>
                            <td class="prod_qty">${detalle.cantidad}</td>
                            <td class="prod_price">$${detalle.precioUnitario}</td>
                        </tr>
                    </c:forEach>
                </tbody>

                <div class="info_box" style="font-size: 18px;">
                    Total: $${compra.total}
                </div>

                <div class="info_box">
                    Dirección: ${compra.direccionEnvio}
                </div>
            </table>
            
            <hr class="section_divider" style="margin-top: -31px; position: relative; z-index: 1; border-top: 1px solid white;">

            <!-- SecciÃ³n Inferior -->
            <div class="bottom_details_grid">
                
                <!-- Columna Detalles de Pago -->
                <div class="payment_col">
                    <label class="section_label">Detalle del pago</label>
                    <hr class="section_divider" style="margin-top: 5px; margin-bottom: 15px; border-width: 1px;">

                    <div class="info_box">
                        Tipo de pago: Tarjeta
                    </div>
                    
                    <div class="info_box">
                        Cuenta: ****1234
                    </div>

                    <div class="info_box">
                        DirecciÃ³n: Kino 1012, entre Allende y Colima
                    </div>
                </div>

                <!-- Columna Total -->
                <div class="total_col">
                    <label class="section_label">Total:</label>
                    <hr class="section_divider" style="margin-top: 5px; margin-bottom: 15px; border-width: 1px; visibility: hidden;"> <!-- Espaciador invisible para alinear -->

                    <div class="info_box" style="font-size: 18px;">
                        $8,900
                    </div>
                </div>

            </div>

        </div>

        <!-- BotÃ³n Fuera de la Card -->
        <div class="submit_container">
            <button class="btn_submit">Enviar Pedido</button>
        </div>

    </div>

    <footer class="footer">
        <p>&copy; 2025 Ecommerce. Todos los derechos reservados.</p>
        <p>Hecho por Equipo #2</p>
    </footer>

    <script>
        lucide.createIcons();
    </script>
</body>

</html>