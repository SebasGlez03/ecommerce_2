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
import itson.ecommercedominio.dtos.ReseniaDTO;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercedominio.enumeradores.RolUsuario;
import itson.ecommercenegocio.IReseniaBO;
import itson.ecommercenegocio.implementacion.ReseniasBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.ReseniasDAO;
import itson.ecommercepersistencia.implementaciones.UsuariosDAO;
import jakarta.servlet.annotation.WebServlet;
import org.bson.types.ObjectId;

/**
 * Servlet para manejar la creación y moderación de reseñas.
 */
@WebServlet(name = "ReseniasServlet", urlPatterns = {"/resenias"})
public class ReseniasServlet extends HttpServlet {

    private IReseniaBO reseniasBO;
    private IConexionBD conexion;

    @Override
    public void init() throws ServletException {
        // 1. Inicializar conexión (false = BD real)
        this.conexion = new ConexionBD(false);
        this.reseniasBO = new ReseniasBO(
                new ReseniasDAO(conexion),
                new UsuariosDAO(conexion)
        );
        System.out.println("ReseniasServlet inicializado.");
    }

    @Override
    public void destroy() {
        if (this.conexion != null) {
            this.conexion.close();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("producto.html"); // Redirigir por defecto
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Por seguridad, redirigir al inicio si intentan acceder por GET directamente
        response.sendRedirect("index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        if (accion == null) {
            response.sendRedirect("index.jsp");
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
                response.sendRedirect("index.jsp");
        }
    }

    private void agregarResenia(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 1. Verificar sesión
        HttpSession session = request.getSession(false);
        UsuarioDTO usuario = (session != null) ? (UsuarioDTO) session.getAttribute("usuarioLogueado") : null;

        //2. Verificar que el usuario no es nulo
        if (usuario == null) {
            // Guardar la URL actual para volver después del login (opcional)
            response.sendRedirect("login.jsp");
            return;
        }

        //3. Verificar que nomaas los no admin puedan crear reseñas
        if (usuario.getRolUsuario() == RolUsuario.ADMIN) {
            //Si lo es regresamos un errror o una advertencia
            String referer = request.getHeader("Referer");
            response.sendRedirect((referer != null ? referer : "index.jsp") + "&error=" + "Los administradores no pueden dejar reseñas. ");
            return;
        }

        // 4. Obtener parámetros del formulario
        String comentario = request.getParameter("comentario");
        String calificacionStr = request.getParameter("calificacion");
        String idProductoStr = request.getParameter("idProducto"); // ¡Importante!

        try {
            // Conversiones
            int calificacion = Integer.parseInt(calificacionStr);
            ObjectId idProducto = new ObjectId(idProductoStr);

            // 5. Llenar TODOS los atributos
            ReseniaDTO nuevaReseniaDTO = new ReseniaDTO();
            nuevaReseniaDTO.setId(new ObjectId()); // Generar ID nuevo
            nuevaReseniaDTO.setIdUsuario(usuario.getId());
            nuevaReseniaDTO.setIdProducto(idProducto);
            nuevaReseniaDTO.setComentario(comentario);
            nuevaReseniaDTO.setCalificacion(calificacion);
            nuevaReseniaDTO.setFecha(new Date());

            // 6. Llamar al BO para cerear la reseña
            reseniasBO.crearResenia(nuevaReseniaDTO);

            // 7. Redirigir al producto
            // Usamos 'referer' para volver exactamente a la página donde estábamos
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer : "productos?accion=ver&id=" + idProductoStr);

        } catch (NumberFormatException e) {
            // Error de formato en números
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Calificación inválida.");
        } catch (IllegalArgumentException e) {
            // Error en ObjectId
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto inválido.");
        } catch (NegocioException e) {
            // Error de lógica de negocio (ej. comentario vacío)
            request.setAttribute("error", e.getMessage());
            // Idealmente reenviar al JSP, pero como es un POST desde detalle, 
            // un redirect con error en parámetro o sesión es más simple por ahora:
            String referer = request.getHeader("Referer");
            response.sendRedirect((referer != null ? referer : "index.jsp") + "&error=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error inesperado al guardar reseña.");
        }
    }

    private void eliminarResenia(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idResenia = request.getParameter("idResenia");

        HttpSession session = request.getSession(false);
        UsuarioDTO usuarioLogueado = (session != null) ? (UsuarioDTO) session.getAttribute("usuarioLogueado") : null;

        try {
            // El BO valida permisos (Admin o Dueño) y formato de ID
            reseniasBO.eliminarResenia(idResenia, usuarioLogueado);

            // Redirigir
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer : "index.jsp");

        } catch (NegocioException e) {
            // Si no tiene permisos o no existe
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp");
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
