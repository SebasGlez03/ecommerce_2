/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOTest;

import itson.ecommercedominio.dtos.CompraDTO;
import itson.ecommercedominio.dtos.DetalleCompraDTO;
import itson.ecommercedominio.enumeradores.EstadoCompra;
import itson.ecommercepersistencia.IComprasDAO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import itson.ecommercepersistencia.implementaciones.ComprasDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Beto_
 */
public class ComprasDAOTest {
    private IConexionBD conexion;
    private IComprasDAO comprasDAO;
    
    // Bandera para usar la BD de pruebas definida en ConexionBD
    private static final boolean BD_PRUEBA = true;

    @BeforeEach
    public void setUp() {
        conexion = new ConexionBD(BD_PRUEBA);
        
        // Limpiamos la colección de compras antes de iniciar cada prueba
        // para asegurar un entorno limpio.
        conexion.getDatabase().getCollection("compras").drop();
        
        comprasDAO = new ComprasDAO(conexion);
    }

    @AfterEach
    public void tearDown() {
        if (conexion != null) {
            conexion.close();
        }
    }

    @Test
    public void testCrearCompraExitoso() throws PersistenciaException {
        System.out.println("crearCompra");

        ObjectId idUsuario = new ObjectId();
        ObjectId idProducto = new ObjectId();

        // 1. Preparar: Crear detalles de la compra (simulando carrito)
        List<DetalleCompraDTO> detalles = new ArrayList<>();
        detalles.add(new DetalleCompraDTO(idProducto, "Monitor Gamer", 2, 3500.00));
        // El subtotal sería 7000.00

        // 2. Preparar: Crear el DTO de la compra
        CompraDTO nuevaCompra = new CompraDTO();
        nuevaCompra.setUsuarioId(idUsuario);
        nuevaCompra.setDireccionEnvio("Calle Test 123, Colonia Centro");
        nuevaCompra.setMetodoPago("Tarjeta de Crédito");
        nuevaCompra.setTotal(7000.00);
        nuevaCompra.setDetalles(detalles);
        nuevaCompra.setEstado(EstadoCompra.PENDIENTE);

        try {
            // 3. Act: Llamar al DAO
            CompraDTO resultado = comprasDAO.crear(nuevaCompra);

            // 4. Assert: Verificar resultados
            assertNotNull(resultado, "La compra retornada no debe ser nula");
            assertNotNull(resultado.getId(), "El ID de la compra debe generarse");
            assertNotNull(resultado.getFechaCompra(), "La fecha de compra debe generarse automáticamente");
            
            assertEquals(idUsuario, resultado.getUsuarioId());
            assertEquals(7000.00, resultado.getTotal());
            assertEquals(EstadoCompra.PENDIENTE, resultado.getEstado());
            
            // Verificar que los detalles se guardaron
            assertEquals(1, resultado.getDetalles().size());
            assertEquals("Monitor Gamer", resultado.getDetalles().get(0).getNombreProducto());

        } catch (Exception e) {
            fail("No debió lanzar excepción: " + e.getMessage());
        }
    }

    @Test
    public void testObtenerPorUsuario() throws PersistenciaException {
        System.out.println("obtenerPorUsuario");

        ObjectId usuarioObjetivo = new ObjectId();
        ObjectId otroUsuario = new ObjectId();

        // 1. Preparar: Insertar compras para el usuario objetivo
        CompraDTO compra1 = new CompraDTO();
        compra1.setUsuarioId(usuarioObjetivo);
        compra1.setTotal(100.0);
        compra1.setDetalles(new ArrayList<>()); // Lista vacía
        compra1.setDireccionEnvio("Dir A");
        compra1.setMetodoPago("Efectivo");

        CompraDTO compra2 = new CompraDTO();
        compra2.setUsuarioId(usuarioObjetivo);
        compra2.setTotal(200.0);
        compra2.setDetalles(new ArrayList<>());
        compra2.setDireccionEnvio("Dir B");
        compra2.setMetodoPago("Tarjeta");

        // 2. Preparar: Insertar compra para OTRO usuario (para verificar filtro)
        CompraDTO compraRuido = new CompraDTO();
        compraRuido.setUsuarioId(otroUsuario);
        compraRuido.setTotal(999.0);
        compraRuido.setDetalles(new ArrayList<>());
        compraRuido.setDireccionEnvio("Dir C");
        compraRuido.setMetodoPago("Paypal");

        // Guardamos en BD
        comprasDAO.crear(compra1);
        comprasDAO.crear(compra2);
        comprasDAO.crear(compraRuido);

        // 3. Act: Consultar solo las de usuarioObjetivo
        List<CompraDTO> historial = comprasDAO.obtenerPorUsuario(usuarioObjetivo);

        // 4. Assert
        assertNotNull(historial);
        assertEquals(2, historial.size(), "Debería encontrar exactamente 2 compras del usuario objetivo");
        
        // Verificar que NO traiga la compra de 999.0
        boolean existeRuido = historial.stream().anyMatch(c -> c.getTotal().equals(999.0));
        assertFalse(existeRuido, "No debería traer compras de otros usuarios");
    }
}
