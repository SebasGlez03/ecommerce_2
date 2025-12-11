package itson.ecommerce.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import itson.ecommercedominio.dtos.CompraDTO;
import itson.ecommercedominio.dtos.DetalleCompraDTO;

public class EmailService {

    // Configuración para GMAIL (requiere Contraseña de Aplicación)
    // O puedes usar Mailtrap para pruebas seguras
    private final String username = "cuauhtemoc.vasquez247284@potros.itson.edu.mx"; 
    private final String password = "btfn bwxq yeag fskg"; 

    public void enviarConfirmacionCompra(String destinatario, CompraDTO compra) {
        
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatario)
            );
            message.setSubject("Confirmación de Pedido #" + compra.getId());

            // Construimos el HTML del correo
            StringBuilder html = new StringBuilder();
            html.append("<h1>¡Gracias por tu compra!</h1>");
            html.append("<p>Tu pedido ha sido confirmado exitosamente.</p>");
            html.append("<h3>Detalle del pedido:</h3>");
            html.append("<ul>");
            
            for (DetalleCompraDTO detalle : compra.getDetalles()) {
                html.append("<li>")
                    .append(detalle.getNombreProducto())
                    .append(" - Cant: ").append(detalle.getCantidad())
                    .append(" - $").append(detalle.getPrecioUnitario())
                    .append("</li>");
            }
            html.append("</ul>");
            html.append("<h3>Total Pagado: $").append(compra.getTotal()).append("</h3>");
            html.append("<p>Enviaremos tus productos a: ").append(compra.getDireccionEnvio()).append("</p>");

            message.setContent(html.toString(), "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error al enviar correo: " + e.getMessage());
        }
    }
}