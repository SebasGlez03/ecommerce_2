<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pago - Ecommerce</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100..900;1,100..900&display=swap">
    <script src="https://unpkg.com/lucide@latest"></script>
    <link rel="stylesheet" href="estilos/styles_tarjeta.css">
    
    <style>
        /* Pequeños ajustes para ocultar/mostrar secciones */
        .hidden { display: none; }
        .info_text { color: white; margin-bottom: 15px; background: rgba(255,255,255,0.1); padding: 10px; border-radius: 5px;}
    </style>
</head>

<body>
    
    <%-- HEADER CORREGIDO (Igual al de index.jsp) --%>
    <header>
        <div class="logo">
            <a href="index.jsp">
                <img src="https://soendergaard.com/wp-content/uploads/2024/06/logoipsum-logo-29-1.png" alt="Logo">
            </a>
        </div>

        <nav class="nav_items">
            <a href="index.jsp">Seguir Comprando</a>
            <span style="color: var(--color-txt-secondary);">|</span>
            <a href="carrito.jsp">Volver al Carrito</a>
        </nav>

        <div class="user_section">
             <c:if test="${not empty sessionScope.usuarioLogueado}">
                <span style="color:white; margin-right:10px;">${sessionScope.usuarioLogueado.nombre}</span>
            </c:if>
            <img class="user_avatar" src="https://cdn-icons-png.flaticon.com/512/12225/12225881.png" alt="Avatar">
        </div>
    </header>

    <div class="payment_container">

        <%-- COLUMNA IZQUIERDA: FORMULARIO --%>
        <div class="payment_form_card">
            <h2 class="form_title" id="tituloMetodo">Tarjeta de débito o crédito</h2>

            <form id="formPago" action="compras" method="POST" class="form_grid">
                <input type="hidden" name="accion" value="pagar">
                
                <%-- INPUT OCULTO QUE GUARDARÁ EL MÉTODO SELECCIONADO --%>
                <input type="hidden" id="inputMetodoPago" name="metodoPago" value="Tarjeta">

                <%-- SECCIÓN TARJETA (Se oculta si no es tarjeta) --%>
                <div id="seccionTarjeta" class="full_width form_grid" style="margin:0; padding:0; gap:15px;">
                    <input type="text" id="cardNumber" class="custom_input full_width" placeholder="Número de tarjeta" 
                           pattern="\d{16}" title="16 dígitos" required>

                    <input type="text" id="cardDate" class="custom_input" placeholder="MM/YY" required>
                    <input type="text" id="cardCvc" class="custom_input" placeholder="CVC" required>
                </div>
                
                <%-- MENSAJES PARA OTROS MÉTODOS --%>
                <div id="infoTransferencia" class="full_width hidden">
                    <div class="info_text">
                        <p>Realiza tu transferencia a la cuenta CLABE: <b>123456789012345678</b></p>
                        <p>Banco: BBVA</p>
                        <p>Tu pedido se procesará una vez confirmado el pago.</p>
                    </div>
                </div>
                
                <div id="infoContraEntrega" class="full_width hidden">
                    <div class="info_text">
                        <p>Pagarás en efectivo al momento de recibir tu paquete en tu domicilio.</p>
                        <p>Por favor ten la cantidad exacta preparada.</p>
                    </div>
                </div>

                <%-- DIRECCIÓN (Siempre visible) --%>
                <label class="form_section_label full_width" style="margin-top: 20px;">Dirección de Envío</label>

                <input type="text" name="calle" class="custom_input full_width" placeholder="Calle y Número" required value="${sessionScope.usuarioLogueado.direccion}">
                <input type="text" name="colonia" class="custom_input full_width" placeholder="Colonia" required>
                <input type="text" name="ciudad" class="custom_input full_width" placeholder="Ciudad" required>
                <input type="text" name="cp" class="custom_input full_width" placeholder="Código Postal" required>
            </form>
        </div>

        <%-- COLUMNA DERECHA: SELECCIÓN DE MÉTODO --%>
        <div class="payment_methods_col">
            <div class="methods_group">
                <label style="color:white; margin-bottom:10px;">Selecciona un método:</label>
                
                <button type="button" class="method_btn active" onclick="seleccionarMetodo('Tarjeta', this)">
                     Tarjeta
                </button>
                
                <button type="button" class="method_btn secondary" onclick="seleccionarMetodo('Transferencia', this)">
                     Transferencia
                </button>
                
                <button type="button" class="method_btn secondary" onclick="seleccionarMetodo('Contra Entrega', this)">
                     Contra Entrega
                </button>
            </div>

            <div class="pay_btn_container">
                <button type="submit" form="formPago" class="btn_pay">
                    Confirmar Pedido ($${sessionScope.carrito.total})
                </button>
            </div>
        </div>

    </div>
        
    <footer class="footer">
        <p>&copy; 2025 Ecommerce. Todos los derechos reservados.</p>
    </footer>

    <script>
        lucide.createIcons();

        function seleccionarMetodo(metodo, btnElement) {
            // 1. Actualizar el input oculto que se envía al Servlet
            document.getElementById('inputMetodoPago').value = metodo;
            
            // 2. Cambiar título
            const titulo = document.getElementById('tituloMetodo');
            
            // 3. Obtener secciones
            const secTarjeta = document.getElementById('seccionTarjeta');
            const infoTrans = document.getElementById('infoTransferencia');
            const infoContra = document.getElementById('infoContraEntrega');
            
            // Inputs de tarjeta (para quitar el 'required' si no se usan)
            const inputsTarjeta = secTarjeta.querySelectorAll('input');

            // 4. Lógica de visualización
            if (metodo === 'Tarjeta') {
                titulo.innerText = "Tarjeta de débito o crédito";
                secTarjeta.classList.remove('hidden');
                infoTrans.classList.add('hidden');
                infoContra.classList.add('hidden');
                
                // Activar validación de inputs
                inputsTarjeta.forEach(input => input.setAttribute('required', ''));
                
            } else if (metodo === 'Transferencia') {
                titulo.innerText = "Transferencia Bancaria";
                secTarjeta.classList.add('hidden');
                infoTrans.classList.remove('hidden');
                infoContra.classList.add('hidden');
                
                // Desactivar validación de inputs de tarjeta
                inputsTarjeta.forEach(input => input.removeAttribute('required'));
                
            } else {
                titulo.innerText = "Pago Contra Entrega";
                secTarjeta.classList.add('hidden');
                infoTrans.classList.add('hidden');
                infoContra.classList.remove('hidden');
                
                // Desactivar validación de inputs de tarjeta
                inputsTarjeta.forEach(input => input.removeAttribute('required'));
            }

            // 5. Actualizar estilos de botones (Visual)
            // Quitar clase 'active' a todos y poner 'secondary'
            document.querySelectorAll('.method_btn').forEach(btn => {
                btn.classList.remove('active');
                btn.classList.add('secondary');
            });
            
            // Poner 'active' al presionado
            btnElement.classList.remove('secondary');
            btnElement.classList.add('active');
        }
    </script>
</body>
</html>