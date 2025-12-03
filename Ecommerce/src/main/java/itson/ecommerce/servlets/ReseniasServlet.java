package itson.ecommerce.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Imports de tus capas
import itson.ecommercedominio.Resenia;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercenegocio.IReseniaBO;
import itson.ecommercenegocio.implementacion.ReseniasBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.ReseniasDAO;
import org.bson.types.ObjectId;

/**
 * Servlet para manejar la creación y moderación de reseñas.
 */
public class ReseniasServlet extends HttpServlet {

    private IReseniaBO reseniasBO;
    private IConexionBD conexionDB;

    @Override
    public void init() throws ServletException {
        // 1. Inicializar conexión (false = BD real)
        this.conexionDB = new ConexionBD(false);

        // 2. Inyectar dependencias: Conexión -> DAO -> BO
        ReseniasDAO reseniasDAO = new ReseniasDAO(this.conexionDB);
        this.reseniasBO = new ReseniasBO(reseniasDAO);

        System.out.println("ReseniasServlet inicializado.");
    }

    @Override
    public void destroy() {
        if (this.conexionDB != null) {
            this.conexionDB.close();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("producto.html"); // Redirigir por defecto
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        if (accion == null) {
            processErrorRequest(response, "Acción no especificada.");
            return;
        }

        switch (accion) {
            case "agregar":
                agregarResenia(request, response);
                break;
            case "eliminar":
                eliminarResenia(request, response);
                break;
            default:
                processErrorRequest(response, "Acción desconocida.");
        }
    }

    private void agregarResenia(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 1. Verificar sesión
        HttpSession session = request.getSession(false);
        UsuarioDTO usuario = (session != null) ? (UsuarioDTO) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            // Si no hay usuario logueado, mandar al login
            response.sendRedirect("login.html");
            return;
        }

        // 2. Obtener parámetros del formulario
        String comentario = request.getParameter("comentario");
        String calificacionStr = request.getParameter("calificacion");
        
        // NOTA: Idealmente aquí recibirías el "idProducto" también, pero 
        // tu clase Resenia.java actual tiene ese campo comentado.

        try {
            int calificacion = Integer.parseInt(calificacionStr);

            // 3. Crear objeto de Dominio
            Resenia nuevaResenia = new Resenia();
            nuevaResenia.setId(new ObjectId()); // Generar ID nuevo
            nuevaResenia.setIdUsuario(usuario.getId());
            nuevaResenia.setComentario(comentario);
            nuevaResenia.setCalificacion(calificacion);
            nuevaResenia.setFecha(new Date()); // Fecha actual

            // 4. Llamar a Negocio
            reseniasBO.crearResenia(nuevaResenia);

            // 5. Redirigir al producto (usamos referer para volver a la misma página)
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer : "producto.html");

        } catch (NumberFormatException e) {
            processErrorRequest(response, "La calificación debe ser un número entero.");
        } catch (NegocioException e) {
            processErrorRequest(response, "Error al guardar la reseña: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            processErrorRequest(response, "Ocurrió un error inesperado.");
        }
    }

    private void eliminarResenia(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String idResenia = request.getParameter("idResenia");

            // Obtenemos al usuario de la sesión actual
            HttpSession session = request.getSession(false);
            UsuarioDTO usuarioLogueado = (session != null) ? (UsuarioDTO) session.getAttribute("usuarioLogueado") : null;

            try {
                // Llamamos al método unificado en el BO
                reseniasBO.eliminarResenia(idResenia, usuarioLogueado);

                // Redirigir de vuelta a la página anterior (el producto)
                String referer = request.getHeader("Referer");
                response.sendRedirect(referer != null ? referer : "index.html");

            } catch (NegocioException e) {
                e.printStackTrace();
                // Podrías redirigir a una página de error o enviar un 403
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            }
        }

    // Método auxiliar para mostrar errores simples
    protected void processErrorRequest(HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Error</title></head><body>");
            out.println("<h1>Error</h1>");
            out.println("<p>" + mensaje + "</p>");
            out.println("<a href='javascript:history.back()'>Volver</a>");
            out.println("</body></html>");
        }
    }
}