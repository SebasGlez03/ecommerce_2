/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOTest;

import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.enumeradores.Categoria;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import itson.ecommercepersistencia.implementaciones.ProductosDAO;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Beto_
 */
public class ProductosDAOTest {
    private IConexionBD conexion;
    private ProductosDAO productosDAO;
    private static final boolean BD_PRUEBA = true;

    @BeforeEach
    public void setUp() {
        conexion = new ConexionBD(BD_PRUEBA);
        conexion.getDatabase().getCollection("productos").drop();
        productosDAO = new ProductosDAO(conexion);
    }

    @AfterEach
    public void tearDown() {
        if (conexion != null) conexion.close();
    }

    @Test
    public void testAgregarProducto() throws PersistenciaException {
        System.out.println("agregarProducto");
        
        ProductoDTO nuevo = new ProductoDTO();
        nuevo.setNombre("Monitor 24");
        nuevo.setDescripcion("Monitor LED");
        nuevo.setPrecio(2500.00);
        nuevo.setStock(10);
        nuevo.setCategoria(Categoria.MONITORES);
        nuevo.setImagenUrl("http://img.com/monitor.jpg");

        ProductoDTO resultado = productosDAO.agregar(nuevo);

        assertNotNull(resultado.getId());
        assertEquals("Monitor 24", resultado.getNombre());
    }

    @Test
    public void testObtenerTodos() throws PersistenciaException {
        System.out.println("obtenerTodos");
        
        // Insertar 2 productos
        ProductoDTO p1 = new ProductoDTO(null, "P1", "Desc", 100.0, 5, Categoria.TECLADOS, "img1");
        ProductoDTO p2 = new ProductoDTO(null, "P2", "Desc", 200.0, 5, Categoria.RATONES, "img2");
        
        productosDAO.agregar(p1);
        productosDAO.agregar(p2);

        List<ProductoDTO> lista = productosDAO.obtenerTodos();
        
        assertEquals(2, lista.size());
    }
    
    @Test
    public void testBuscarConFiltros() throws PersistenciaException {
        System.out.println("buscarConFiltros");
        
        // Preparar escenario: 1 Laptop barata, 1 Laptop cara, 1 Silla
        ProductoDTO p1 = new ProductoDTO(null, "Laptop Dell", "Básica", 8000.0, 5, Categoria.LAPTOPS, "img");
        ProductoDTO p2 = new ProductoDTO(null, "Laptop Gamer MSI", "Pro", 25000.0, 2, Categoria.LAPTOPS, "img");
        ProductoDTO p3 = new ProductoDTO(null, "Silla Oficina", "Ergo", 3000.0, 10, Categoria.SILLAS, "img");
        
        productosDAO.agregar(p1);
        productosDAO.agregar(p2);
        productosDAO.agregar(p3);

        // Caso 1: Buscar por Nombre "Gamer"
        List<ProductoDTO> busquedaNombre = productosDAO.buscar("Gamer", null, null, null);
        assertEquals(1, busquedaNombre.size());
        assertEquals("Laptop Gamer MSI", busquedaNombre.get(0).getNombre());

        // Caso 2: Buscar por Categoría "LAPTOPS" y Precio máximo 10000
        List<ProductoDTO> busquedaCompleja = productosDAO.buscar(null, Categoria.LAPTOPS, null, 10000.0);
        assertEquals(1, busquedaCompleja.size());
        assertEquals("Laptop Dell", busquedaCompleja.get(0).getNombre());
    }

    @Test
    public void testActualizarProducto() throws PersistenciaException {
        System.out.println("actualizarProducto");
        
        // 1. Crear
        ProductoDTO original = new ProductoDTO(null, "Mouse Viejo", "Desc", 100.0, 10, Categoria.RATONES, "img");
        ProductoDTO insertado = productosDAO.agregar(original);
        
        // 2. Modificar objeto
        insertado.setNombre("Mouse Nuevo RGB");
        insertado.setPrecio(150.0);
        
        // 3. Actualizar en BD
        boolean exito = productosDAO.actualizar(insertado);
        
        // 4. Verificar
        assertTrue(exito, "El método actualizar debió retornar true");
        
        ProductoDTO actualizadoBD = productosDAO.obtenerPorId(insertado.getId());
        assertEquals("Mouse Nuevo RGB", actualizadoBD.getNombre());
        assertEquals(150.0, actualizadoBD.getPrecio());
    }

    @Test
    public void testEliminarProducto() throws PersistenciaException {
        System.out.println("eliminarProducto");
        
        ProductoDTO p = new ProductoDTO(null, "Borrarme", "...", 10.0, 1, Categoria.COMPONENTES, "...");
        ProductoDTO insertado = productosDAO.agregar(p);
        
        boolean exito = productosDAO.eliminar(insertado.getId());
        
        assertTrue(exito);
        assertNull(productosDAO.obtenerPorId(insertado.getId()));
    }
}
