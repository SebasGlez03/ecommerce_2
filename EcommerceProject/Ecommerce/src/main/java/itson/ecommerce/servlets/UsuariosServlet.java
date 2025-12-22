/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package itson.ecommerce.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// IMPORTS DE TU NUEVA ARQUITECTURA
import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercedominio.enumeradores.RolUsuario; // Asegúrate de tener este Enum
import itson.ecommercenegocio.IUsuariosBO;
import itson.ecommercenegocio.implementacion.UsuariosBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.IUsuariosDAO;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.UsuariosDAO;
import java.util.List;

/**
 *
 * @author PC
 */
public class UsuariosServlet extends HttpServlet {

    private IUsuariosBO usuariosBO;
    private IConexionBD conexionDB;

    /**
     * El metodo init() se llama una sola vez cuando el Servlet es creado. Aqui
     * se crea la cadena de dependencias.
     *
     * @throws jakarta.servlet.ServletException
     */
    @Override
    public void init() throws ServletException {
        // 1. Crear la conexion a la BD (false= no es la BD de prueba)
        this.conexionDB = new ConexionBD(false);

        // 2. Inyectar conexion al DAO
        IUsuariosDAO usuariosDAO = new UsuariosDAO(this.conexionDB);

        // 3. Inyectar DAO al BO
        this.usuariosBO = new UsuariosBO(usuariosDAO);

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

        String accion = request.getParameter("accion");

        // Si no hay acción, mandamos al login y TERMINAMOS la ejecución con return
        if (accion == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        HttpSession session;
        switch (accion) {
            case "logout":
                this.manejarLogOut(request, response);
                break;

            case "verPerfil":
                // Lógica para mostrar perfil
                 
                session = request.getSession(false);
                if (session != null && session.getAttribute("usuarioLogueado") != null) {
                    request.getRequestDispatcher("perfil.jsp").forward(request, response);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;

            case "listarAdmin":
                // Verificar que sea ADMIN
               session = request.getSession(false);
                UsuarioDTO admin = (session != null) ? (UsuarioDTO) session.getAttribute("usuarioLogueado") : null;

                if (admin != null && admin.getRolUsuario() == RolUsuario.ADMIN) {
                    try {
                        List<UsuarioDTO> lista = usuariosBO.obtenerTodos();
                        request.setAttribute("listaUsuarios", lista);
                        request.getRequestDispatcher("admin/usuarios.jsp").forward(request, response);
                    } catch (NegocioException e) {
                        response.sendRedirect("admin/dashboard.jsp?error=" + e.getMessage());
                    }
                } else {
                    response.sendRedirect("index.jsp"); // Si no es admin, para fuera
                }
                break;

            default:
                // Cualquier otra acción desconocida manda al login
                response.sendRedirect("login.jsp");
                break;

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * Este metodo se activa cuando el formulario registro.jsp es enviado.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Obtenemos la accion del formulario (campo hidden o boton)
        String accion = request.getParameter("accion");

        // Logica de negocio
        if (accion == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        switch (accion) {
            case "registrar":
                this.manejarRegistro(request, response);
                break;
            case "login":
                this.manejarLogin(request, response);
                break;
            case "editar":
                manejarEdicion(request, response);
                break;
            case "cambiarEstado":
                cambiarEstadoUsuario(request, response);
                break;
            default:
                response.sendRedirect("login.jsp");
        }
    }

    private void manejarRegistro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtener los parametros del fomulario
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String password = request.getParameter("password");

        try {
            // 2. Logica de Negocio
            Usuario usuarioNuevo = new Usuario();
            usuarioNuevo.setNombre(nombre);
            usuarioNuevo.setEmail(email);
            usuarioNuevo.setContrasenia(password);
            usuarioNuevo.setDireccion(direccion);
            usuarioNuevo.setTelefono(telefono);
            usuarioNuevo.setEsActivo(true);
            usuarioNuevo.setRolUsuario(RolUsuario.CLIENTE); // Cliente por defecto

            UsuarioDTO usuarioRegistradoDTO = this.usuariosBO.crearUsuario(usuarioNuevo);

            // 3. Enviar respuesta de exito
            // El codigo comentado de la parte inferior muestra que el usuario se ha registrado correctamente
            // response.setContentType("text/html;charset=UTF-8");
            // try (PrintWriter out = response.getWriter()) {
            //     out.println("<!DOCTYPE html>");
            //     out.println("<html>");
            //     out.println("<head>");
            //     out.println("<title>Registro Exitoso</title>");
            //     out.println("<meta charset=\"UTF-8\">");
            //     out.println("<style>");
            //     out.println("body { font-family: Arial, sans-serif; display: grid; place-items: center; min-height: 90vh; background-color: #f9f9f9; }");
            //     out.println(".container { padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); text-align: center; }");
            //     out.println("h1 { color: #2a7dc7; }");
            //     out.println("p { font-size: 1.1em; }");
            //     out.println("a { font-size: 1em; text-decoration: none; margin-top: 20px; display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; border-radius: 5px; }");
            //     out.println("a:hover { background-color: #0056b3; }");
            //     out.println("</style>");
            //     out.println("</head>");
            //     out.println("<body>");
            //     out.println("<div class='container'>");
            //     out.println("<h1>¡Registro Exitoso!</h1>");
            //     out.println("<p>Bienvenido, <b>" + usuarioRegistradoDTO.getNombre() + "</b>.</p>");
            //     out.println("<p>Tu cuenta ha sido creada con el email: " + usuarioRegistradoDTO.getEmail() + ".</p>");
            //     out.println("<a href='login.jsp'>Iniciar Sesión Ahora</a>"); // Link a login
            //     out.println("</div>");
            //     out.println("</body>");
            //     out.println("</html>");
            // }
            request.setAttribute("mensajeExito", "Cuenta creada! Inicia Sesion.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
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
            UsuarioDTO usuarioDTO = this.usuariosBO.obtenerUsuarioPorCredenciales(email, password);

            // 3. Si las credenciales son correctas, iniciar sesion
            HttpSession session = request.getSession(true);

            // Guardamos el objeto Usuario completo en la sesion
            // Esto es util para mostrar su nombre en otras paginas
            session.setAttribute("usuarioLogueado", usuarioDTO);

            // Establecemos un tiempo de inactividad (ej. 30 minutos)
            session.setMaxInactiveInterval(30 * 60);

            // 4. Redirigir al inicio segun el rol
            if (usuarioDTO.getRolUsuario() == RolUsuario.ADMIN) {
                response.sendRedirect("admin/dashboard.jsp");
            } else {
                response.sendRedirect("index.jsp"); // Pagina principal
            }

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

    public void manejarLogOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destruye la sesion
        }
        response.sendRedirect("login.jsp");
    }

    private void manejarEdicion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UsuarioDTO usuarioActual = (UsuarioDTO) session.getAttribute("usuarioLogueado");

        if (usuarioActual == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 1. Obtener datos del form
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email"); // Ojo: usualmente el email no se edita fácil por temas de ID, pero aquí lo permitiremos
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String password = request.getParameter("password"); // Puede venir vacío

        try {
            // 2. Actualizar el objeto DTO (usamos el ID que ya tenemos en sesión)
            usuarioActual.setNombre(nombre);
            usuarioActual.setEmail(email);
            usuarioActual.setDireccion(direccion);
            usuarioActual.setTelefono(telefono);
            usuarioActual.setContrasenia(password); // El BO sabrá qué hacer si está vacío

            // 3. Llamar al BO
            UsuarioDTO usuarioActualizado = usuariosBO.actualizarUsuario(usuarioActual);

            // 4. IMPORTANTE: Actualizar la sesión con los nuevos datos para que se vea reflejado en el header
            // Como la contraseña ya viene hasheada del BO o nula, ten cuidado de no sobreescribir mal si necesitas la plana, 
            // pero para sesión el DTO está bien.
            session.setAttribute("usuarioLogueado", usuarioActualizado);

            // 5. Redirigir con éxito
            request.setAttribute("mensajeExito", "¡Perfil actualizado correctamente!");
            request.getRequestDispatcher("perfil.jsp").forward(request, response);

        } catch (NegocioException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("perfil.jsp").forward(request, response);
        }
    }

    private void cambiarEstadoUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idUsuario = request.getParameter("idUsuario");
        boolean nuevoEstado = Boolean.parseBoolean(request.getParameter("nuevoEstado"));

        try {
            usuariosBO.cambiarEstadoUsuario(idUsuario, nuevoEstado);
            response.sendRedirect("usuarios?accion=listarAdmin");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("usuarios?accion=listarAdmin&error=ErrorAlActualizar");
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
