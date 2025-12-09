<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Pago - Ecommerce</title>
        <!-- Fuente Roboto -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100..900;1,100..900&display=swap">
        <!-- Iconos Lucide -->
        <script src="https://unpkg.com/lucide@latest"></script>
        <link rel="stylesheet" href="estilos/styles_tarjeta.css">
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

        <div class="payment_container">
    
        <div class="payment_form_card">
            <h2 class="form_title">Tarjeta de débito o crédito</h2>

            <form id="formPago" action="compras" method="POST" class="form_grid">
                <input type="hidden" name="accion" value="pagar">
                <input type="hidden" name="metodoPago" value="Tarjeta">

                <input type="text" class="custom_input full_width" placeholder="Número de tarjeta" required pattern="\d{16}" title="16 dígitos">

                <input type="text" class="custom_input" placeholder="MM/YY" required>
                <input type="text" class="custom_input" placeholder="CVC" required>

                <label class="form_section_label full_width">Dirección de Envío</label>

                <input type="text" name="calle" class="custom_input full_width" placeholder="Calle y Número" required value="${sessionScope.usuarioLogueado.direccion}">

                <input type="text" name="colonia" class="custom_input full_width" placeholder="Colonia" required>

                <input type="text" name="ciudad" class="custom_input full_width" placeholder="Ciudad" required>

                <input type="text" name="cp" class="custom_input full_width" placeholder="Código Postal" required>
            </form>
        </div>

        <div class="payment_methods_col">
            <div class="pay_btn_container">
                <button type="submit" form="formPago" class="btn_pay">
                    Pagar $${sessionScope.carrito.total}
                </button>
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