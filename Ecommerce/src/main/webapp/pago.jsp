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
                <a class="login_btn" href="login.html">Iniciar Sesión</a>
                <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
            </div>
        </header>

        <div class="payment_container">
            
            <!-- Lado Izquierdo: Formulario -->
            <div class="payment_form_card">
                <h2 class="form_title">Tarjeta de débito o crédito</h2>

                <form action="#" class="form_grid">
                    <!-- Tarjeta -->
                    <input type="text" class="custom_input full_width" placeholder="Número de tarjeta">
                    
                    <input type="text" class="custom_input" placeholder="Fecha de vencimiento">
                    <input type="text" class="custom_input" placeholder="CSC">

                    <!-- Dirección Label -->
                    <label class="form_section_label full_width">Dirección de la tarjeta</label>

                    <!-- Nombres -->
                    <input type="text" class="custom_input" placeholder="Nombre">
                    <input type="text" class="custom_input" placeholder="Apellidos">

                    <!-- Dirección Completa -->
                    <input type="text" class="custom_input full_width" placeholder="Dirección">
                    
                    <input type="text" class="custom_input full_width" placeholder="Colonia">
                    
                    <input type="text" class="custom_input full_width" placeholder="Ciudad">
                    
                    <input type="text" class="custom_input full_width" placeholder="Estado">
                    
                    <input type="text" class="custom_input full_width" placeholder="Código Postal">
                    
                    <input type="email" class="custom_input full_width" placeholder="Correo Electrónico">
                </form>
            </div>

            <!-- Lado Derecho: Opciones de Pago -->
            <div class="payment_methods_col">
                <div class="methods_group">
                    <button class="method_btn active">Tarjeta</button>
                    <button class="method_btn secondary">Transferencia</button>
                    <button class="method_btn secondary">Contra Entrega</button>
                </div>

                <div class="pay_btn_container">
                    <form action="compras" method="POST">
                        <input type="hidden" name="accion" value="pagar">
                        <button type="submit" class="btn_pay">Pagar $${sessionScope.carrito.total}</button>
                    </form>
                </div>
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