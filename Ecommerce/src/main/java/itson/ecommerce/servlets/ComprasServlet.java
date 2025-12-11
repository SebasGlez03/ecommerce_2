package itson.ecommerce.servlets;

import itson.ecommercedominio.dtos.Carrito;
import itson.ecommercedominio.dtos.CompraDTO;
import itson.ecommercedominio.dtos.DetalleCompraDTO;
import itson.ecommercedominio.dtos.ItemCarrito;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercedominio.enumeradores.EstadoCompra;
import itson.ecommercedominio.enumeradores.RolUsuario;
import itson.ecommercenegocio.IComprasBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercenegocio.implementacion.ComprasBO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.ComprasDAO;
import itson.ecommercepersistencia.implementaciones.ProductosDAO;
import itson.ecommerce.utils.EmailService; // Asegúrate de tener esta clase o comenta la línea
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

@WebServlet(name = "ComprasServlet", urlPatterns = {"/compras"})
public class ComprasServlet extends HttpServlet {

    private IComprasBO comprasBO;
    private IConexionBD conexion;

    @Override
    public void init() throws ServletException {
        // Inicializamos la conexión y las capas
        this.conexion = new ConexionBD(false);
        this.comprasBO = new ComprasBO(
                new ComprasDAO(this.conexion),
                new ProductosDAO(this.conexion)
        );
    }

    @Override
    public void destroy() {
        if (this.conexion != null) {
            this.conexion.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        HttpSession session = request.getSession(false);
        UsuarioDTO usuario = (session != null) ? (UsuarioDTO) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 1. Ver Historial (Cliente)
        if ("historial".equals(accion)) {
            try {
                List<CompraDTO> historial = comprasBO.obtenerHistorialUsuario(usuario.getId());
                request.setAttribute("listaCompras", historial);
                request.getRequestDispatcher("historial.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("index.jsp");
            }
        } // 2. Listar Pedidos (Admin)
        else if ("adminListar".equals(accion)) {
            if (usuario.getRolUsuario() != RolUsuario.ADMIN) {
                response.sendRedirect("index.jsp");
                return;
            }
            try {
                List<CompraDTO> todas = comprasBO.obtenerTodasLasCompras();
                request.setAttribute("listaComprasAdmin", todas);
                request.getRequestDispatcher("admin/pedidos.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("admin/dashboard.jsp?error=ErrorAlListar");
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        // 1. Procesar Pago (Cliente)
        if ("pagar".equals(accion)) {
            procesarPago(request, response);
        } // 2. Actualizar Estado (Admin)
        else if ("actualizarEstado".equals(accion)) {
            try {
                String idCompra = request.getParameter("idCompra");
                String estadoStr = request.getParameter("nuevoEstado");
                EstadoCompra estado = EstadoCompra.valueOf(estadoStr);

                // Convertimos el ID
                ObjectId objectId = new ObjectId(idCompra);

                // Llamamos al BO
                comprasBO.actualizarEstado(objectId, estado);

                // IMPORTANTE: Redirigimos a la acción que CARGA la lista de nuevo
                response.sendRedirect("compras?accion=adminListar");

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("compras?accion=adminListar&error=FalloActualizacion");
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    private void procesarPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioLogueado");
        Carrito carrito = (Carrito) session.getAttribute("carrito");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        if (carrito == null || carrito.getCantidadItems() == 0) {
            response.sendRedirect("carrito.jsp?error=Carritovacio");
            return;
        }

        try {
            // Recoger datos del formulario
            String calle = request.getParameter("calle");
            String colonia = request.getParameter("colonia");
            String ciudad = request.getParameter("ciudad"); // Corregido el name (antes decía cp)
            String cp = request.getParameter("cp");
            String metodoPago = request.getParameter("metodoPago");

            String dirCompleta = String.format("%s, %s, %s, CP: %s", calle, colonia, ciudad, cp);

            // Convertir items
            List<DetalleCompraDTO> detalles = new ArrayList<>();
            for (ItemCarrito item : carrito.getListaItems()) {
                detalles.add(new DetalleCompraDTO(
                        item.getProductoId(),
                        item.getNombre(),
                        item.getCantidad(),
                        item.getPrecioUnitario()
                ));
            }

            // Armar CompraDTO
            CompraDTO compra = new CompraDTO();
            compra.setUsuarioId(usuario.getId());
            compra.setTotal(carrito.getTotal());
            compra.setDetalles(detalles);
            compra.setDireccionEnvio(dirCompleta);

            // Asignar método de pago o defecto
            if (metodoPago != null && !metodoPago.isEmpty()) {
                compra.setMetodoPago(metodoPago);
            } else {
                compra.setMetodoPago("Tarjeta");
            }

            compra.setEstado(EstadoCompra.PENDIENTE);

            // Llamar al BO
            CompraDTO compraRealizada = comprasBO.realizarCompra(compra);

            // --- ENVÍO DE CORREO (Opcional: comenta si falla) ---
            try {
                new Thread(() -> {
                    EmailService emailService = new EmailService();
                    emailService.enviarConfirmacionCompra(usuario.getEmail(), compraRealizada);
                }).start();
            } catch (Exception exMail) {
                System.out.println("No se pudo enviar el correo: " + exMail.getMessage());
            }
            // ---------------------------------------------------

            session.removeAttribute("carrito");

            request.setAttribute("compra", compraRealizada);
            request.getRequestDispatcher("confirmacion.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Encodeamos el mensaje para que viaje bien en la URL
            String errorMsg = java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
            response.sendRedirect("carrito.jsp?error=" + errorMsg);
        }
    }
}
