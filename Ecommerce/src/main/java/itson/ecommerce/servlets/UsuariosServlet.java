/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package itson.ecommerce.servlets;

import Docs.Usuario;
import Excepciones.NegocioException;
import Interfaces.IConexionDB;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import BO.IUsuarioBO;
import BO.UsuarioBO;
import ConexionDB.ConexionBD;
import DAO.UsuarioDAO;

/**
 *
 * @author PC
 */
public class UsuariosServlet extends HttpServlet {

    private IUsuarioBO usuarioBO;
    private IConexionDB conexionDB;

    /**
     * El metodo init() se llama una sola vez cuando el Servlet es creado. Aqui
     * se crea la cadena de dependencias.
     */
    @Override
    public void init() throws ServletException {
        // 1. Crear la conexion a la BD (false= no es la BD de prueba)
        this.conexionDB = new ConexionBD(false);

        // 2. Crear el DAO. pasandole la conexion
        UsuarioDAO usuarioDAO = new UsuarioDAO(this.conexionDB);

        // 3. Crear el BO, pasandole el DAO
        this.usuarioBO = new UsuarioBO(usuarioDAO);

        System.out.println("UsuarioServlet inicializado y onectado a la BD.");
    }

    /**
     * El metodo destroy() se llama cuando el servidor se apaga. Es buena
     * practica cerrar la conexion aqui.
     */
    @Override
    public void destroy() {
        if (this.conexionDB != null) {
            this.conexionDB.close();
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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UsuariosServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UsuariosServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Muestra una pagina de error simple al usuario
     *
     * @param response La respuesta http
     * @param mensaje El mensaje de error a mostrar
     * @throws ServletException
     * @throws IOException
     */
    protected void processErrorRequest(HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Error</title>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; display: grid; place-items: center; min-height: 90vh; background-color: #f9f9f9; }");
            out.println(".container { padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); text-align: center; border-left: 5px solid #e74c3c; }");
            out.println("h1 { color: #e74c3c; }"); // Color rojo para error
            out.println("p { font-size: 1.1em; }");
            out.println("a { font-size: 1em; text-decoration: none; margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; border-radius: 5px; }");
            out.println("a:hover { background-color: #0056b3; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Oops! Ha ocurrido un error</h1>");
            out.println("<p>" + mensaje + "</p>");
            out.println("<a href='javascript:history.back()'>Volver atrás</a>"); // Enlace para volver
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * Si el usuario intenta navegar directamente a /usuarios en su navegador
     * (GET), es mejor redirigirlo al formulario de registro.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("registro.html");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * Este metodo se activa cuando el formulario registro.html es enviado.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Establecer la codificacion de caracteres para manejar acentos y ñ
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 2. Obtener los parametros del formulario
        // La cadena DEBE coincidir con el atribto "nombre" del <input> en registro.html
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String direccion = request.getParameter("direccion");
        String password = request.getParameter("password");
        String accion = request.getParameter("accion");

        // 3. Imprimir en la consola del servidor para depurar
        // Esto aparecera en la terminal del servidor de aplicaciones (Tomcat)
        System.out.println("--- Nuevo Registro Recibido ---");
        System.out.println("Nombre: " + nombre);
        System.out.println("Email: " + email);
        System.out.println("Direccion: " + direccion);
        // NOTA: No imprimr contraseña porque puede que nos digan que hay problemas de seguridad
//        System.out.println("Contrasenia: " + password);
        System.out.println("-------------------------------");

        // 4. Logica de negocio
        if (accion == null) {
            processErrorRequest(response, "Accion desconocida.");
            return;
        }

        switch (accion) {
            case "registrar":
                this.manejarRegistro(request, response);
                break;
            case "login":
                this.manejarLogin(request, response);
                break;
            default:
                processErrorRequest(response, "Accion desconocida.");
        }
    }

    private void manejarRegistro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtener los parametros del fomulario
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String direccion = request.getParameter("direccion");
        String password = request.getParameter("password");

        try {
            // 2. Logica de Negocio
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setContrasenia(password);
            usuario.setDireccion(direccion);
            usuario.setEsActivo(true);
            usuario.setRolUsuario(Docs.RolUsuario.CLIENTE); // Cliente por defecto

            Usuario usuarioRegistrado = this.usuarioBO.crearUsuario(usuario);

            // 3. Enviar respuesta de exito
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Registro Exitoso</title>");
                out.println("<meta charset=\"UTF-8\">");
                out.println("<style>");
                out.println("body { font-family: Arial, sans-serif; display: grid; place-items: center; min-height: 90vh; background-color: #f9f9f9; }");
                out.println(".container { padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); text-align: center; }");
                out.println("h1 { color: #2a7dc7; }");
                out.println("p { font-size: 1.1em; }");
                out.println("a { font-size: 1em; text-decoration: none; margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; border-radius: 5px; }");
                out.println("a:hover { background-color: #0056b3; }");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container'>");
                out.println("<h1>¡Registro Exitoso!</h1>");
                out.println("<p>Bienvenido, <b>" + usuarioRegistrado.getNombre() + "</b>.</p>");
                out.println("<p>Tu cuenta ha sido creada con el email: " + usuarioRegistrado.getEmail() + ".</p>");
                out.println("<a href='login.html'>Iniciar Sesión Ahora</a>"); // Link a login
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (NegocioException e) {
            // 4. Manejar error de la logica de negocio (ej. email ya existe)
            System.err.println("Error en BO al registrar: " + e.getMessage());
            processErrorRequest(response, "Error al registrar: " + e.getMessage());
        } catch (Exception e) {
            // 5. Manejar cualquier otro error
            System.err.println("Error inesperado al registrar: " + e.getMessage());
            e.printStackTrace();
            processErrorRequest(response, "Ocurrio un error inesperado.");
        }
    }

    private void manejarLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtener parametros
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // 2. Logica de Negocio: Autenticar
            Usuario usuario = this.usuarioBO.obtenerUsuarioPorCredenciales(email, password);

            // 3. Si las credenciales son correctas, iniciar sesion
            HttpSession session = request.getSession(true);

            // Guardamos el objeto Usuario completo en la sesion
            // Esto es util para mostrar su nombre en otras paginas
            session.setAttribute("usuarioLogueado", usuario);

            // Establecemos un tiempo de inactividad (ej. 30 minutos)
            session.setMaxInactiveInterval(30 * 60);

            // 4. Redirigir al inicio
            response.sendRedirect("index.html");
        } catch (NegocioException e) {
            // 5. Manejar error de BO (ej. Credenciales incorrectas, Usuario inactivo)
            System.err.println("Error en BO al iniciar sesion: " + e.getMessage());
            processErrorRequest(response, "Error en BO al iniciar sesion: " + e.getMessage());
        } catch (Exception e) {
            // 6. Manejar cualquier otro error
            System.err.println("Error inesperado al iniciar sesion: " + e.getMessage());
            e.printStackTrace();
            processErrorRequest(response, "Ocurrio un error inesperado.");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet para manejar el registro de nuevos usuarios";
    }// </editor-fold>

}
