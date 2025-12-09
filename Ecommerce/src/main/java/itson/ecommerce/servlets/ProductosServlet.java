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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        try {
            // Asegúrate de tener el archivo config.properties creado en resources
            this.conexion = new ConexionBD(false);
            this.productosBO = new ProductosBO(new ProductosDAO(this.conexion));
            this.reseniasBO = new ReseniasBO(new ReseniasDAO(this.conexion));
        } catch (Exception e) {
            System.err.println("--- ERROR CRÍTICO EN INIT PRODUCTOS SERVLET ---");
            e.printStackTrace();
            // Esto hará que el servidor falle al iniciar si la BD no conecta, avisándote de inmediato
            throw new ServletException("No se pudo conectar a la Base de Datos", e);
        }
    }
    
    @Override
    public void destroy() {
        if(this.conexion != null) {
            this.conexion.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "listarAdmin":
                cargarListaAdmin(request, response);
                break;
            case "formulario":
                cargarFormulario(request, response);
                break;
            case "ver":
                verDetalleProducto(request, response);
                break;
            default:
                listarProductosPublico(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        if (accion == null) {
            response.sendRedirect("productos");
            return;
        }

        try {
            switch (accion) {
                case "crear":
                    guardarProducto(request, response);
                    break;
                case "editar":
                    actualizarProducto(request, response);
                    break;
                case "eliminar":
                    eliminarProducto(request, response);
                    break;
                default:
                    response.sendRedirect("productos");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Revisa la consola del servidor (Output en NetBeans)
            response.sendRedirect("admin/dashboard.jsp?error=" + e.getMessage());
        }
    }

    // --- MÉTODOS PRIVADOS PARA ORDENAR EL CÓDIGO ---

    private void cargarListaAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ProductoDTO> lista = productosBO.obtenerTodos();
            request.setAttribute("listaProductos", lista);
            request.getRequestDispatcher("/admin/productos.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // CORRECCIÓN: Codificar el mensaje para evitar HTTP 400
            String mensajeError = URLEncoder.encode("Error al cargar lista: " + e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect("admin/dashboard.jsp?error=" + mensajeError);
        }
    }

    private void cargarFormulario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) {
            try {
                ProductoDTO p = productosBO.obtenerProductoPorId(new ObjectId(id));
                request.setAttribute("producto", p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("/admin/productos_form.jsp").forward(request, response);
    }

    private void listarProductosPublico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String busqueda = request.getParameter("busqueda");
            String categoriaParam = request.getParameter("categoria");
            Categoria catEnum = null;

            if (categoriaParam != null && !categoriaParam.isEmpty()) {
                catEnum = Categoria.valueOf(categoriaParam);
            }

            List<ProductoDTO> productos;
            if ((busqueda != null && !busqueda.isEmpty()) || catEnum != null) {
                productos = productosBO.buscarProductos(busqueda, catEnum, null, null);
            } else {
                productos = productosBO.obtenerTodos();
            }

            request.setAttribute("listaProductos", productos);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception ne) {
            ne.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar catálogo");
        }
    }

    private void verDetalleProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idString = request.getParameter("id");
            if (idString == null || idString.isEmpty()) {
                response.sendRedirect("productos");
                return;
            }
            
            //Obtenemos el producto y las reseñas
            ObjectId idProducto = new ObjectId(idString);
            ProductoDTO producto = productosBO.obtenerProductoPorId(idProducto);
            List<ReseniaDTO> resenias = reseniasBO.obtenerReseniasPorProducto(idProducto);
            
            //Calculamos el promedio de reseñas
            double promedio = 0.0;
            if(resenias != null && !resenias.isEmpty()){
                double suma = 0;
                for (ReseniaDTO r : resenias) {
                    suma += r.getCalificacion();
                }
                promedio = suma/resenias.size();
            }
            
            //Lo guardamos en el DTO
            producto.setPromedioCalificacion(promedio);
            
            request.setAttribute("producto", producto);
            request.setAttribute("listaResenias", resenias);
            request.getRequestDispatcher("/producto.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("productos");
        }
    }

    // --- ACCIONES DE ESCRITURA (POST) ---

    private void guardarProducto(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProductoDTO nuevo = extraerProductoDeRequest(request);
        // Validar que NO tenga ID para creación
        productosBO.agregarProducto(nuevo);
        response.sendRedirect(request.getContextPath() + "/productos?accion=listarAdmin");
    }

    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idStr = request.getParameter("idProducto");
        ProductoDTO producto = extraerProductoDeRequest(request);
        producto.setId(new ObjectId(idStr)); // Asignamos ID para edición
        
        productosBO.actualizarProducto(producto);
        response.sendRedirect(request.getContextPath() + "/productos?accion=listarAdmin");
    }

    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idStr = request.getParameter("idProducto");
        productosBO.eliminarProducto(new ObjectId(idStr));
        response.sendRedirect(request.getContextPath() + "/productos?accion=listarAdmin");
    }

    // Método auxiliar para no repetir código de leer parámetros
    private ProductoDTO extraerProductoDeRequest(HttpServletRequest request) {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        Double precio = Double.parseDouble(request.getParameter("precio"));
        Integer stock = Integer.parseInt(request.getParameter("stock"));
        String imagenUrl = request.getParameter("imagenUrl");
        Categoria categoria = Categoria.valueOf(request.getParameter("categoria"));
        String especificaciones = request.getParameter("especificaciones");
        
        return new ProductoDTO(nombre, descripcion, precio, stock, categoria, imagenUrl, especificaciones);
    }
}
