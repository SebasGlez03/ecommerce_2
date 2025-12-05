/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package itson.ecommerce.servlets;

import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercenegocio.IProductosBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercenegocio.implementacion.ProductosBO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.ProductosDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Beto_
 */
@WebServlet(name = "ProductosServlet", urlPatterns = {"/producto"})
public class ProductosServlet extends HttpServlet {
    
    private IProductosBO productosBO;
    private IConexionBD conexion;
    
    @Override
    public void init() throws ServletException {
        this.conexion = new ConexionBD(false);
        this.productosBO = new ProductosBO(new ProductosDAO(this.conexion));
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
        
        if (accion == null || accion.isEmpty()) {
            //pend
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
            //1. Obtenemos la lista de la capa de Negocio
            List<ProductoDTO> productos = productosBO.obtenerTodos();

            //2. Guardamos la lista en el request para q el JSP la vea
            request.setAttribute("listaProductos", productos);

            //3. Enviamos al index.jsp o al catalogo.jsp
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }catch(NegocioException ne){
            ne.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar productos");
        }
    }
}
