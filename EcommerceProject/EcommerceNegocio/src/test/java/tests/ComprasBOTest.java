/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tests;

import itson.ecommercedominio.dtos.CompraDTO;
import itson.ecommercedominio.dtos.DetalleCompraDTO;
import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.enumeradores.Categoria;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercenegocio.implementacion.ComprasBO;
import itson.ecommercepersistencia.IComprasDAO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.IProductosDAO;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.ComprasDAO;
import itson.ecommercepersistencia.implementaciones.ProductosDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Beto_
 */
public class ComprasBOTest {
    private IConexionBD conexion;
    private IComprasDAO comprasDAO;
    private IProductosDAO productosDAO;
    private ComprasBO comprasBO;
    
    private static final boolean BD_PRUEBA = true;

    @BeforeEach
    public void setUp() {
        conexion = new ConexionBD(BD_PRUEBA);
        
        // Limpiamos AMBAS colecciones para tener un escenario controlado
        conexion.getDatabase().getCollection("compras").drop();
        conexion.getDatabase().getCollection("productos").drop();
        
        comprasDAO = new ComprasDAO(conexion);
        productosDAO = new ProductosDAO(conexion);
        
        // Inyectamos ambos DAOs al BO de Compras
        comprasBO = new ComprasBO(comprasDAO, productosDAO);
    }

    @AfterEach
    public void tearDown() {
        if (conexion != null) conexion.close();
    }

    @Test
    public void testCompraExitosaDescuentaStock() throws Exception {
        System.out.println("TEST BO: Compra Exitosa y Descuento de Stock");

        // 1. PREPARACIÓN: Crear un producto con Stock = 10
        ProductoDTO productoOriginal = new ProductoDTO("Laptop", "Desc", 15000.0, 10, Categoria.LAPTOPS, "img", "momu");
        // Usamos el DAO directo para preparar el escenario rápido (sin pasar por validaciones del BO de productos)
        productoOriginal = productosDAO.agregar(productoOriginal);
        ObjectId idProd = productoOriginal.getId();

        // 2. PREPARACIÓN: Crear la intención de compra (DTO)
        List<DetalleCompraDTO> detalles = new ArrayList<>();
        // Compramos 3 Laptops
        detalles.add(new DetalleCompraDTO(idProd, "Laptop", 3, 15000.0));

        CompraDTO compra = new CompraDTO();
        compra.setUsuarioId(new ObjectId());
        compra.setTotal(45000.0);
        compra.setDetalles(detalles);
        compra.setDireccionEnvio("Casa");
        compra.setMetodoPago("Efectivo");

        // 3. EJECUCIÓN (Act)
        CompraDTO resultado = comprasBO.realizarCompra(compra);

        // 4. VERIFICACIÓN (Assert)
        assertNotNull(resultado.getId(), "La compra debe tener ID");
        
        // Verificar que el stock bajó a 7 (10 - 3)
        ProductoDTO productoActualizado = productosDAO.obtenerPorId(idProd);
        assertEquals(7, productoActualizado.getStock(), "El stock debió bajar de 10 a 7");
    }

    @Test
    public void testFallaCompraPorStockInsuficiente() throws Exception {
        System.out.println("TEST BO: Falla por Stock Insuficiente");

        // 1. Preparar producto con POCO stock (Solo 1)
        ProductoDTO producto = productosDAO.agregar(new ProductoDTO("Mouse", "...", 500.0, 1, Categoria.RATONES, "img", "omg"));
        
        // 2. Intentar comprar 5
        List<DetalleCompraDTO> detalles = new ArrayList<>();
        detalles.add(new DetalleCompraDTO(producto.getId(), "Mouse", 5, 500.0));

        CompraDTO compra = new CompraDTO();
        compra.setUsuarioId(new ObjectId());
        compra.setDetalles(detalles);
        // ... otros campos obligatorios para que no falle por validación nula ...
        compra.setTotal(2500.0); 
        compra.setDireccionEnvio("x"); 
        compra.setMetodoPago("x");

        // 3. Ejecutar y Esperar Error
        NegocioException excepcion = assertThrows(NegocioException.class, () -> {
            comprasBO.realizarCompra(compra);
        });

        // 4. Verificar mensaje
        assertTrue(excepcion.getMessage().contains("Stock insuficiente"), "El mensaje debe avisar del stock");
    }
}
