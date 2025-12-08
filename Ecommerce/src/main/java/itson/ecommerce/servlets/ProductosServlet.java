/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package itson.ecommerce.servlets;

import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.dtos.ReseniaDTO;
import itson.ecommercedominio.enumeradores.Categoria;
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

        // Lógica pública
        if (accion != null && accion.equals("ver")) {
            verDetalleProducto(request, response);
        } 
        // Lógica de ADMIN
        else if ("listarAdmin".equals(accion)) {
             // Validar sesión de admin aquí si ocupamos seguridad extra
             try {
                List<ProductoDTO> lista = productosBO.obtenerTodos();
                request.setAttribute("listaProductos", lista);
                // Redirige al JSP de tabla de administración, NO al index
                request.getRequestDispatcher("admin/productos.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("admin/dashboard.jsp?error=ErrorListar");
            }
        } else if ("formulario".equals(accion)) {
             String id = request.getParameter("id");
             if(id != null && !id.isEmpty()){
                 try {
                    ProductoDTO p = productosBO.obtenerProductoPorId(new ObjectId(id));
                    request.setAttribute("producto", p);
                 } catch (Exception e) { e.printStackTrace(); }
             }
             request.getRequestDispatcher("admin/productos_form.jsp").forward(request, response);
        } 
        // Default (Catálogo público)
        else {
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
        request.setCharacterEncoding("UTF-8"); // Importante para caracteres especiales
        String accion = request.getParameter("accion");

        if (accion == null) {
            response.sendRedirect("productos");
            return;
        }

        switch (accion) {
            case "crear":
                guardarProducto(request, response);
                break;
            case "editar":
                editarProducto(request, response);
                break;
            case "eliminar":
                eliminarProducto(request, response);
                break;
            default:
                response.sendRedirect("productos");
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
    
    private void guardarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Recoger parámetros
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            Double precio = Double.valueOf(request.getParameter("precio"));
            Integer stock = Integer.valueOf(request.getParameter("stock"));
            String imagenUrl = request.getParameter("imagenUrl");
            String categoriaStr = request.getParameter("categoria");
            
            // Asumiendo que tienes el Enum Categoria importado
            Categoria categoria = Categoria.valueOf(categoriaStr);

            ProductoDTO nuevo = new ProductoDTO(nombre, descripcion, precio, stock, categoria, imagenUrl);
            productosBO.agregarProducto(nuevo);
            
            // Redirigir al listado de admin (necesitas crear el JSP admin/productos.jsp)
            response.sendRedirect("productos?accion=listarAdmin"); 
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin/dashboard.jsp?error=" + e.getMessage());
        }
    }
    
    private void editarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idStr = request.getParameter("idProducto");
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            Double precio = Double.valueOf(request.getParameter("precio"));
            Integer stock = Integer.valueOf(request.getParameter("stock"));
            String imagenUrl = request.getParameter("imagenUrl");
            Categoria categoria = Categoria.valueOf(request.getParameter("categoria"));

            ProductoDTO producto = new ProductoDTO(new ObjectId(idStr), nombre, descripcion, precio, stock, categoria, imagenUrl);
            productosBO.actualizarProducto(producto);
            
            response.sendRedirect("productos?accion=listarAdmin");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin/dashboard.jsp?error=" + e.getMessage());
        }
    }
    
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idStr = request.getParameter("idProducto");
            productosBO.eliminarProducto(new ObjectId(idStr));
            response.sendRedirect("productos?accion=listarAdmin");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("productos?accion=listarAdmin&error=ErrorEliminar");
        }
    }

    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {    
            //1. Extraemos los valores de la request
            String idStr = request.getParameter("id");
            
            //2. Obtenemos el producto por id
            ProductoDTO buscado = productosBO.obtenerProductoPorId(new ObjectId(idStr));
            
            //3. Actualizamos el producto con el buscado
            productosBO.actualizarProducto(buscado);

            //4. Redirigimos a la lista de admin (asumiendo que ya está esa vista)
            //iíresponse.sendRedirect("admin/productos.jsp?exito=creado");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin/productos_form.jsp?error=" + e.getMessage());
        }
    }
    
    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            String busqueda = request.getParameter("busqueda");
            String categoriaParam = request.getParameter("categoria");
            Categoria catEnum = null;
            
            if(categoriaParam != null && !categoriaParam.isEmpty()){
                catEnum = Categoria.valueOf(categoriaParam);
            }
            
            List<ProductoDTO> productos;
            if (busqueda != null && !busqueda.isEmpty()) {
                // Buscamos por nombre (aquí se puede agregar lógica para categoría y precio y así)
                productos = productosBO.buscarProductos(busqueda, catEnum, null, null);
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
