/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package itson.ecommerce.servlets;

import itson.ecommercedominio.dtos.Carrito;
import itson.ecommercedominio.dtos.ItemCarrito;
import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercenegocio.IProductosBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercenegocio.implementacion.ProductosBO;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.ProductosDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
@WebServlet(name = "CarritoServlet", urlPatterns = {"/carrito"})
public class CarritoServlet extends HttpServlet {
    private IProductosBO productosBO;
    
    public void init(){
        ConexionBD conexion = new ConexionBD(false);
        this.productosBO = new ProductosBO(new ProductosDAO(conexion));
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
            out.println("<title>Servlet CarritoServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CarritoServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
        //Redirige a la vista del carrito
        request.getRequestDispatcher("carrito.jsp").forward(request, response);
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
        
        //Si la acción es agregar, agregamos el producto al carrito
        if ("agregar".equals(accion)) {
            agregarProducto(request, response);
        } else if ("eliminar".equals(accion)) {
            // TODO: Implementar lógica de eliminación (Req 3.1)
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
    
    private void agregarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        HttpSession session = request.getSession();
        
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if(carrito == null){
            carrito = new Carrito(new HashMap<>());
            session.setAttribute("carrito", carrito);
        }
        
        try{
            String idProd = request.getParameter("idProducto");
            int cantidad = Integer.parseInt(idProd);
            
            //Obtenemos los datos de la BD
            ProductoDTO prod = productosBO.obtenerProductoPorId(new ObjectId(idProd));
            
            //Si el producto si existe (q debería) creamos un item con él
            if(prod != null){
                ItemCarrito item = new ItemCarrito(
                    prod.getId(),
                    prod.getNombre(),
                    prod.getImagenUrl(),
                    prod.getPrecio(),
                    cantidad
                );
                
                //Agregamos el producto y actualizamos la sesión
                carrito.agregarItem(item);
                session.setAttribute("carrito", carrito);
            }
            
            //Redirigimos al carrito
            response.sendRedirect("carrito");
        }catch(NegocioException ne){
            ne.printStackTrace();
            response.sendRedirect("index.jsp");
        }
    }
}
