/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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

/**
 *
 * @author Beto_
 */
@WebServlet(name = "ComprasServlet", urlPatterns = {"/compras"})
public class ComprasServlet extends HttpServlet {
    private IComprasBO comprasBO;
    private IConexionBD conexion;
    
    @Override
    public void init() throws ServletException {
        this.conexion = new ConexionBD(false);
        this.comprasBO = new ComprasBO(new ComprasDAO(this.conexion), new ProductosDAO(this.conexion));
    }
    
    @Override
    public void destroy() {
        if(this.conexion != null) {
            this.conexion.close();
        }
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ComprasServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ComprasServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

        if ("historial".equals(accion)) {
            try {
                List<CompraDTO> historial = comprasBO.obtenerHistorialUsuario(usuario.getId());
                request.setAttribute("listaCompras", historial);
                request.getRequestDispatcher("historial.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("index.jsp");
            }
        } 
        // NUEVA LÓGICA ADMIN
        else if ("adminListar".equals(accion)) {
            if (usuario.getRolUsuario() != RolUsuario.ADMIN) {
                response.sendRedirect("index.jsp");
                return;
            }
            try {
                // Asegúrate de haber agregado este método al BO como se indicó arriba
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

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        
        if ("pagar".equals(accion)) {
            procesarPago(request, response);
        } 
        // NUEVA LÓGICA ADMIN
        else if ("actualizarEstado".equals(accion)) {
            try {
                String idCompra = request.getParameter("idCompra");
                String estadoStr = request.getParameter("nuevoEstado");
                EstadoCompra estado = EstadoCompra.valueOf(estadoStr);
                
                comprasBO.actualizarEstado(new ObjectId(idCompra), estado);
                
                response.sendRedirect("compras?accion=adminListar");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("compras?accion=adminListar&error=NoSePudoActualizar");
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void procesarPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioLogueado");
        Carrito carrito = (Carrito) session.getAttribute("carrito");

        // 1. Validaciones
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        if (carrito == null || carrito.getCantidadItems() == 0) {
            response.sendRedirect("carrito.jsp?error=vacio");
            return;
        }

        try {
            // 2. Convertir items del Carrito a DetalleCompraDTO
            List<DetalleCompraDTO> detalles = new ArrayList<>();
            for (ItemCarrito item : carrito.getListaItems()) {
                detalles.add(new DetalleCompraDTO(
                    item.getProductoId(),
                    item.getNombre(),
                    item.getCantidad(),
                    item.getPrecioUnitario()
                ));
            }

            // 3. Armar el objeto CompraDTO
            CompraDTO compra = new CompraDTO();
            compra.setUsuarioId(usuario.getId());
            compra.setTotal(carrito.getTotal());
            compra.setDetalles(detalles);
            compra.setDireccionEnvio(usuario.getDireccion()); // Usamos la dirección del perfil
            compra.setMetodoPago("Tarjeta Simulada");
            compra.setEstado(EstadoCompra.PENDIENTE);

            // 4. Llamar al BO (Esto guarda la compra y descuenta el stock)
            CompraDTO compraRealizada = comprasBO.realizarCompra(compra);

            // 5. Éxito: Limpiar carrito y mandar a confirmación
            session.removeAttribute("carrito");
            
            // Pasamos el objeto compra al JSP de confirmación
            request.setAttribute("compra", compraRealizada);
            request.getRequestDispatcher("confirmacion.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Si falla (ej. stock insuficiente), volvemos al carrito con el error
            response.sendRedirect("carrito.jsp?error=" + e.getMessage());
        }
    }
}
