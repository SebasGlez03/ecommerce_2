/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tests;

import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.enumeradores.Categoria;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercenegocio.implementacion.ProductosBO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.IProductosDAO;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.ProductosDAO;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Beto_
 */
public class ProductosBOTest {
    private IConexionBD conexion;
    private IProductosDAO productosDAO;
    private ProductosBO productosBO;
    
    // Usamos la BD de prueba para no ensuciar la real
    private static final boolean BD_PRUEBA = true;

    @BeforeEach
    public void setUp() {
        conexion = new ConexionBD(BD_PRUEBA);
        
        // Limpiamos la colección antes de cada prueba para tener un entorno limpio
        conexion.getDatabase().getCollection("productos").drop();
        
        productosDAO = new ProductosDAO(conexion);
        productosBO = new ProductosBO(productosDAO);
    }

    @AfterEach
    public void tearDown() {
        if (conexion != null) conexion.close();
    }

    @Test
    public void testAgregarProductoExitoso() {
        System.out.println("TEST BO: Agregar Producto Exitoso");
        
        ProductoDTO nuevo = new ProductoDTO();
        nuevo.setNombre("Silla Gamer");
        nuevo.setDescripcion("Ergonómica");
        nuevo.setPrecio(3500.00);
        nuevo.setStock(5);
        nuevo.setCategoria(Categoria.SILLAS);
        nuevo.setImagenUrl("http://img.com/silla.jpg");

        try {
            ProductoDTO resultado = productosBO.agregarProducto(nuevo);
            
            assertNotNull(resultado.getId(), "El ID se debe generar");
            assertEquals("Silla Gamer", resultado.getNombre());
            
        } catch (NegocioException e) {
            fail("No debió fallar: " + e.getMessage());
        }
    }

    @Test
    public void testValidacionPrecioNegativo() {
        System.out.println("TEST BO: Validación Precio Negativo");
        
        ProductoDTO p = new ProductoDTO("Mala Silla", "...", -100.00, 5, Categoria.SILLAS, "img", "mumu");

        NegocioException excepcion = assertThrows(NegocioException.class, () -> {
            productosBO.agregarProducto(p);
        });

        assertEquals("El precio debe ser mayor a 0.", excepcion.getMessage());
    }

    @Test
    public void testValidacionStockNegativo() {
        System.out.println("TEST BO: Validación Stock Negativo");
        
        ProductoDTO p = new ProductoDTO("Mala Silla", "...", 100.00, -5, Categoria.SILLAS, "img", "mumu");

        NegocioException excepcion = assertThrows(NegocioException.class, () -> {
            productosBO.agregarProducto(p);
        });

        assertEquals("El stock no puede ser negativo.", excepcion.getMessage());
    }

    @Test
    public void testValidacionNombreVacio() {
        System.out.println("TEST BO: Validación Nombre Vacío");
        
        ProductoDTO p = new ProductoDTO("", "...", 100.00, 5, Categoria.SILLAS, "img", "mumu");

        NegocioException excepcion = assertThrows(NegocioException.class, () -> {
            productosBO.agregarProducto(p);
        });

        assertEquals("El nombre del producto es obligatorio.", excepcion.getMessage());
    }
}
