package LibreriaOnline;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class LibreriaOnlineTest {

    private LibreriaOnline libreria;
    private Usuario usuario1;
    private Usuario usuario2;
    private Libro libro1;
    private Libro libro2;
    private JuegoMesa juego1;
    private JuegoMesa juego2;

    @Before
    public void setUp() {
        libreria = new LibreriaOnline();

        usuario1 = new Usuario("Juan", "Perez", LocalDate.of(1990, 5, 12),
                "12345678A", new TarjetaCredito("1111", "12/25", "123"));
        usuario2 = new Usuario("Ana", "Lopez", LocalDate.of(2005, 8, 20),
                "87654321B", new TarjetaCredito("2222", "06/23", "456"));

        libro1 = new Libro("Aventuras de Tom Sawyer", "Mark Twain", "ISBN123",
                15.99, "Aventuras", true);
        libro2 = new Libro("1984", "George Orwell", "ISBN456",
                12.99, "Ciencia Ficción", false);

        juego1 = new JuegoMesa("Catan", 10, "Estrategia");
        juego2 = new JuegoMesa("Dixit", 8, "Familiar");

        libreria.agregarUsuario(usuario1);
        libreria.agregarUsuario(usuario2);
        libreria.agregarProducto(libro1);
        libreria.agregarProducto(libro2);
        libreria.agregarProducto(juego1);
        libreria.agregarProducto(juego2);
    }

    @Test
    public void comprarProducto_UsuarioYProductoExistente_DeberiaAgregarProductoAUsuarioYActualizarVentas() {
        libreria.comprarProducto(usuario1, libro1);

        List<Producto> productosUsuario = usuario1.getProductos();
        assertTrue(productosUsuario.contains(libro1));

        assertEquals(1, libro1.getVentas());
    }

    @Test
    public void devolverProducto_ProductoCompradoHaceMenosDe2Semanas_DeberiaEliminarProductoDeUsuarioYActualizarVentas() {
        libreria.comprarProducto(usuario1, libro1);
        libreria.devolverProducto(usuario1, libro1);

        List<Producto> productosUsuario = usuario1.getProductos();
        assertFalse(productosUsuario.contains(libro1));

        assertEquals(0, libro1.getVentas());
    }

    @Test
    public void listarProductosPorTitulo_DeberiaRetornarListaOrdenadaPorTitulo() {
        List<Producto> listaProductos = libreria.listarProductosPorTitulo();

        assertEquals(libro1, listaProductos.get(0));
        assertEquals(libro2, listaProductos.get(1));
        assertEquals(juego1, listaProductos.get(2));
        assertEquals(juego2, listaProductos.get(3));
    }

    // Agrega más pruebas aquí para cubrir el resto de los métodos...

}
