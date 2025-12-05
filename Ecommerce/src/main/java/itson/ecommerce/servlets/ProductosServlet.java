/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package itson.ecommerce.servlets;

import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.dtos.ReseniaDTO;
import itson.ecommercenegocio.IProductosBO;
import itson.ecommercenegocio.IReseniaBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercenegocio.implementacion.ProductosBO;
import itson.ecommercenegocio.implementacion.ReseniasBO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.ProductosDAO;
import itson.ecommercepersistencia.implementaciones.ReseniasDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
@WebServlet(name = "ProductosServlet", urlPatterns = {"/productos"})
public class ProductosServlet extends HttpServlet {
    
    private IProductosBO productosBO;
    private IReseniaBO reseniasBO;
    private IConexionBD conexion;
    
    @Override
    public void init() throws ServletException {
        this.conexion = new ConexionBD(false);
        this.productosBO = new ProductosBO(new ProductosDAO(this.conexion));
        this.reseniasBO = new ReseniasBO(new ReseniasDAO(conexion));
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
            out.println("<title>Servlet ProductosServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductosServlet at " + request.getContextPath() + "</h1>");
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
        
        if (accion != null && accion.equals("ver")) {
            verDetalleProducto(request, response);
        } else {
            listarProductos(request, response);
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
        processRequest(request, response);
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
    
    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            String busqueda = request.getParameter("busqueda");
            
            List<ProductoDTO> productos;
            if (busqueda != null && !busqueda.isEmpty()) {
                // Buscamos por nombre (aquí se puede agregar lógica para categoría y precio y así)
                productos = productosBO.buscarProductos(busqueda, null, null, null);
            } else {
                productos = productosBO.obtenerTodos();
            }
            
            request.setAttribute("listaProductos", productos);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }catch(NegocioException ne){
            ne.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar productos");
        }
    }
    
    private void verDetalleProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idString = request.getParameter("id");
            if (idString == null || idString.isEmpty()) {
                response.sendRedirect("productos");
                return;
            }
            
            ObjectId idProducto = new ObjectId(idString);
            
            //1. Obtenemos el producto
            ProductoDTO producto = productosBO.obtenerProductoPorId(idProducto);
            
            //2. Obtenemos reseñas (Ahora sí devuelve una lista)
            List<ReseniaDTO> resenias = reseniasBO.obtenerReseniasPorProducto(idProducto);
            
            //3. Enviamos a la vista
            request.setAttribute("producto", producto);
            request.setAttribute("listaResenias", resenias);
            
            request.getRequestDispatcher("producto.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("productos");
        }
    }
}
