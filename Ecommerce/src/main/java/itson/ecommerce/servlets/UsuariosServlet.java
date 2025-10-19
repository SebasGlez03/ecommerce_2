/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package itson.ecommerce.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author PC
 */
public class UsuariosServlet extends HttpServlet {

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
            out.println("<title>Servlet UsuariosServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UsuariosServlet at " + request.getContextPath() + "</h1>");
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

        // 3. Imprimir en la consola del servidor para depurar
        // Esto aparecera en la terminal del servidor de aplicaciones (Tomcat)
        System.out.println("--- Nuevo Registro Recibido ---");
        System.out.println("Nombre: " + nombre);
        System.out.println("Email: " + email);
        System.out.println("Direccion: " + direccion);
        // NOTA: No imprimr contraseña porque puede que nos digan que hay problemas de seguridad
//        System.out.println("Contrasenia: " + password);
        System.out.println("-------------------------------");

        /*
            4. TODO: Logica de Negocio
            Aqui es donde se deberia:
            a. Validar datos (ej. que el email no este vacio, que la contrasenia sea segura, etc).
            b. Crear un objeto de el modelo, como new Usuario(...);
            c. Llamar a una clase DAO para guardar el usuario en la base de datos.
         */
        
        // 5. Por ahora, solo enviaremos una respuesta simple de exito al navegador.
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Registro Exitoso</title>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
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
            out.println("<p>Bienvenido, <b>" + nombre + "</b>.</p>");
            out.println("<p>Tu cuenta ha sido creada con el email: " + email + ".</p>");
            // Mostrar la direccion solo si el usuario la ingreso
            if (direccion != null && !direccion.trim().isEmpty()) {
                out.println("<p>Dirección registrada: " + direccion + "</p>");
            }
            out.println("<a href='index.html'>Volver al Inicio</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
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
