<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <title>Confirmación - Ecommerce</title>
    <link rel="stylesheet" href="estilos/styles_confirmacion.css">
    <script src="https://unpkg.com/lucide@latest"></script>
</head>
<body>
    <div class="confirmation_container">
        <div class="confirmation_card">
            <h1 class="page_title">Confirmación del pedido #${compra.id}</h1>

            <label class="section_label">Detalle de los productos</label>
            <hr class="section_divider">

            <table class="products_table">
                <thead>
                    <tr>
                        <th style="width: 60%;">Producto</th>
                        <th style="width: 15%;">Cantidad</th>
                        <th style="width: 25%;">Precio Unitario</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detalle" items="${compra.detalles}">
                        <tr>
                            <td>
                                <div class="prod_desc">${detalle.nombreProducto}</div>
                            </td>
                            <td class="prod_qty">${detalle.cantidad}</td>
                            <td class="prod_price">$${detalle.precioUnitario}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <div class="bottom_details_grid">
                <div class="payment_col">
                    <label class="section_label">Detalle del envío</label>
                    <hr class="section_divider">
                    
                    <div class="info_box">
                        Método: ${compra.metodoPago}
                    </div>
                    <div class="info_box">
                        Dirección: ${compra.direccionEnvio}
                    </div>
                    <div class="info_box">
                        Estado: ${compra.estado}
                    </div>
                </div>

                <div class="total_col">
                    <label class="section_label">Total Pagado:</label>
                    <hr class="section_divider">
                    <div class="info_box" style="font-size: 24px; color: #4ade80;">
                        $${compra.total}
                    </div>
                </div>
            </div>
        </div>

        <div class="submit_container">
            <a href="index.jsp" class="btn_submit" style="text-decoration:none;">Volver al Inicio</a>
        </div>
    </div>
</body>
</html>