package LibreriaOnline;
import java.time.LocalDate;
import java.util.*;

public class LibreriaOnline {
    private List<Usuario> usuarios;
    private List<Producto> productos;
    private Map<LocalDate, Double> ventasLibros;
    private Map<LocalDate, Double> ventasJuegos;

    public LibreriaOnline() {
        usuarios = new ArrayList<>();
        productos = new ArrayList<>();
        ventasLibros = new HashMap<>();
        ventasJuegos = new HashMap<>();
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void eliminarUsuario(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
    }

    public void comprarProducto(Usuario usuario, Producto producto) {
        if (usuarios.contains(usuario) && productos.contains(producto)) {
            usuario.agregarProducto(producto);
            producto.incrementarVentas();
            if (producto instanceof Libro) {
                double totalVentas = ventasLibros.getOrDefault(LocalDate.now(), 0.0);
                totalVentas += producto.getPrecio();
                ventasLibros.put(LocalDate.now(), totalVentas);
            } else if (producto instanceof JuegoMesa) {
                double totalVentas = ventasJuegos.getOrDefault(LocalDate.now(), 0.0);
                totalVentas += producto.getPrecio();
                ventasJuegos.put(LocalDate.now(), totalVentas);
            }
        }
    }

    public void devolverProducto(Usuario usuario, Producto producto) {
        if (usuario.getProductos().contains(producto) && usuario.puedeDevolverProducto(producto)) {
            usuario.eliminarProducto(producto);
            producto.decrementarVentas();
        }
    }

    public List<Producto> listarProductosPorTitulo() {
        List<Producto> listaProductos = new ArrayList<>(productos);
        listaProductos.sort(Comparator.comparing(Producto::getTitulo));
        return listaProductos;
    }

    public List<Producto> listarProductosVendidos() {
        List<Producto> listaProductos = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getVentas() > 0) {
                listaProductos.add(producto);
            }
        }
        return listaProductos;
    }

    public double obtenerIngresosLibrosPorMes(int mes, int año) {
        LocalDate fecha = LocalDate.of(año, mes, 1);
        return ventasLibros.getOrDefault(fecha, 0.0);
    }

    public double obtenerIngresosJuegosPorMes(int mes, int año) {
        LocalDate fecha = LocalDate.of(año, mes, 1);
        return ventasJuegos.getOrDefault(fecha, 0.0);
    }

    public int obtenerCantidadLibrosVendidosPorMes(int mes, int año) {
        LocalDate fecha = LocalDate.of(año, mes, 1);
        int totalVentas = 0;
        for (Producto producto : productos) {
            if (producto instanceof Libro && producto.getVentas() > 0) {
                totalVentas += ((Libro) producto).getVentasByDate(fecha);
            }
        }
        return totalVentas;
    }

    public int obtenerCantidadJuegosVendidosPorMes(int mes, int año) {
        LocalDate fecha = LocalDate.of(año, mes, 1);
        int totalVentas = 0;
        for (Producto producto : productos) {
            if (producto instanceof JuegoMesa && producto.getVentas() > 0) {
                totalVentas += ((JuegoMesa) producto).getVentasByDate(fecha);
            }
        }
        return totalVentas;
    }

    public List<Libro> obtenerRankingLibrosVendidosPorMes(int mes, int año) {
        LocalDate fecha = LocalDate.of(año, mes, 1);
        List<Libro> librosVendidos = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto instanceof Libro && producto.getVentas() > 0) {
                librosVendidos.add((Libro) producto);
            }
        }
        librosVendidos.sort(Comparator.comparingInt(l -> ((Libro) l).getVentasByDate(fecha)).reversed());
        return librosVendidos;
    }

    public List<JuegoMesa> obtenerRankingJuegosVendidosPorMes(int mes, int año) {
        LocalDate fecha = LocalDate.of(año, mes, 1);
        List<JuegoMesa> juegosVendidos = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto instanceof JuegoMesa && producto.getVentas() > 0) {
                juegosVendidos.add((JuegoMesa) producto);
            }
        }
        juegosVendidos.sort(Comparator.comparingInt(j -> ((JuegoMesa) j).getVentasByDate(fecha)).reversed());
        return juegosVendidos;
    }

    public List<Usuario> obtenerClientesConMasCompras(int mes, int año, int cantidad) {
        LocalDate fecha = LocalDate.of(año, mes, 1);
        Map<Usuario, Integer> comprasPorUsuario = new HashMap<>();
        for (Usuario usuario : usuarios) {
            int totalCompras = 0;
            for (Producto producto : usuario.getProductos()) {
                if (producto instanceof Libro) {
                    totalCompras += ((Libro) producto).getVentasByDate(fecha);
                } else if (producto instanceof JuegoMesa) {
                    totalCompras += ((JuegoMesa) producto).getVentasByDate(fecha);
                }
            }
            comprasPorUsuario.put(usuario, totalCompras);
        }
        List<Usuario> clientesConMasCompras = new ArrayList<>(comprasPorUsuario.keySet());
        clientesConMasCompras.sort(Comparator.comparing(comprasPorUsuario::get).reversed());
        return clientesConMasCompras.subList(0, Math.min(cantidad, clientesConMasCompras.size()));
    }

    public static void main(String[] args) {
        LibreriaOnline libreria = new LibreriaOnline();

        // Creación de usuarios
        Usuario usuario1 = new Usuario("Juan", "Perez", LocalDate.of(1990, 5, 12), "12345678A", new TarjetaCredito("1111", "12/25", "123"));
        Usuario usuario2 = new Usuario("Ana", "Lopez", LocalDate.of(2005, 8, 20), "87654321B", new TarjetaCredito("2222", "06/23", "456"));

        // Creación de productos
        Libro libro1 = new Libro("Aventuras de Tom Sawyer", "Mark Twain", "ISBN123", 15.99, "Aventuras", true);
        Libro libro2 = new Libro("1984", "George Orwell", "ISBN456", 12.99, "Ciencia Ficción", false);
        JuegoMesa juego1 = new JuegoMesa("Catan", 10, "Estrategia");
        JuegoMesa juego2 = new JuegoMesa("Dixit", 8, "Familiar");

        // Agregar usuarios y productos a la librería
        libreria.agregarUsuario(usuario1);
        libreria.agregarUsuario(usuario2);
        libreria.agregarProducto(libro1);
        libreria.agregarProducto(libro2);
        libreria.agregarProducto(juego1);
        libreria.agregarProducto(juego2);

        // Comprar productos
        libreria.comprarProducto(usuario1, libro1);
        libreria.comprarProducto(usuario1, juego1);
        libreria.comprarProducto(usuario2, libro2);
        libreria.comprarProducto(usuario2, juego2);

        // Listar productos por título
        List<Producto> productosPorTitulo = libreria.listarProductosPorTitulo();
        System.out.println("Listado de productos por título:");
        for (Producto producto : productosPorTitulo) {
            System.out.println(producto.getTitulo());
        }

        // Listar productos vendidos
        List<Producto> productosVendidos = libreria.listarProductosVendidos();
        System.out.println("\nListado de productos vendidos:");
        for (Producto producto : productosVendidos) {
            System.out.println(producto.getTitulo() + " - Ventas: " + producto.getVentas());
        }

        // Obtener ingresos de libros por mes y año
        double ingresosLibros = libreria.obtenerIngresosLibrosPorMes(7, 2023);
        System.out.println("\nIngresos de libros en julio 2023: " + ingresosLibros);

        // Obtener ingresos de juegos por mes y año
        double ingresosJuegos = libreria.obtenerIngresosJuegosPorMes(7, 2023);
        System.out.println("Ingresos de juegos en julio 2023: " + ingresosJuegos);

        // Obtener cantidad de libros vendidos por mes y año
        int cantidadLibros = libreria.obtenerCantidadLibrosVendidosPorMes(7, 2023);
        System.out.println("Cantidad de libros vendidos en julio 2023: " + cantidadLibros);

        // Obtener cantidad de juegos de mesa vendidos por mes y año
        int cantidadJuegos = libreria.obtenerCantidadJuegosVendidosPorMes(7, 2023);
        System.out.println("Cantidad de juegos de mesa vendidos en julio 2023: " + cantidadJuegos);

        // Obtener ranking de libros vendidos por mes y año
        List<Libro> rankingLibros = libreria.obtenerRankingLibrosVendidosPorMes(7, 2023);
        System.out.println("\nRanking de libros vendidos en julio 2023:");
        for (Libro libro : rankingLibros) {
            System.out.println(libro.getTitulo() + " - Ventas: " + libro.getVentas());
        }

        // Obtener ranking de juegos de mesa vendidos por mes y año
        List<JuegoMesa> rankingJuegos = libreria.obtenerRankingJuegosVendidosPorMes(7, 2023);
        System.out.println("\nRanking de juegos de mesa vendidos en julio 2023:");
        for (JuegoMesa juego : rankingJuegos) {
            System.out.println(juego.getTitulo() + " - Ventas: " + juego.getVentas());
        }

        // Obtener clientes con más compras en un mes
        List<Usuario> clientesConMasCompras = libreria.obtenerClientesConMasCompras(7, 2023, 2);
        System.out.println("\nClientes con más compras en julio 2023:");
        for (Usuario usuario : clientesConMasCompras) {
            System.out.println(usuario.getNombre() + " " + usuario.getApellidos());
        }
    }

	public List<Usuario> getUsuarios() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Producto> getProductos() {
		// TODO Auto-generated method stub
		return null;
	}
}

class Usuario {
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String dni;
    private TarjetaCredito tarjetaCredito;
    private List<Producto> productos;

    public Usuario(String nombre, String apellidos, LocalDate fechaNacimiento, String dni, TarjetaCredito tarjetaCredito) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.tarjetaCredito = tarjetaCredito;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public boolean puedeDevolverProducto(Producto producto) {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaCompra = null;
        for (Map.Entry<LocalDate, Integer> entry : producto.getVentasPorFecha().entrySet()) {
            if (entry.getValue() > 0) {
                fechaCompra = entry.getKey();
                break;
            }
        }
        if (fechaCompra != null) {
            LocalDate fechaLimiteDevolucion = fechaCompra.plusWeeks(2);
            return fechaActual.isBefore(fechaLimiteDevolucion) || fechaActual.isEqual(fechaLimiteDevolucion);
        }
        return false;
    }
}

abstract class Producto {
    protected String titulo;
    protected double precio;
    protected int ventas;
    protected Map<LocalDate, Integer> ventasPorFecha;

    public Producto(String titulo, double precio) {
    	if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.titulo = titulo;
        this.precio = precio;
        this.ventas = 0;
        this.ventasPorFecha = new HashMap<>();
    }

    public void incrementarVentas() {
        ventas++;
        LocalDate fechaActual = LocalDate.now();
        ventasPorFecha.put(fechaActual, ventasPorFecha.getOrDefault(fechaActual, 0) + 1);
    }

    public void decrementarVentas() {
        if (ventas > 0) {
            ventas--;
            LocalDate fechaActual = LocalDate.now();
            ventasPorFecha.put(fechaActual, ventasPorFecha.getOrDefault(fechaActual, 0) - 1);
        }
    }

    public int getVentas() {
        return ventas;
    }

    public abstract int getVentasByDate(LocalDate fecha);

    public void setVentasByDate(LocalDate fecha, int ventas) {
        ventasPorFecha.put(fecha, ventas);
    }

    public String getTitulo() {
        return titulo;
    }

    public double getPrecio() {
        return precio;
    }

    public Map<LocalDate, Integer> getVentasPorFecha() {
        return ventasPorFecha;
    }
}

class Libro extends Producto {
    private String autor;
    private String identificador;
    private String categoria;
    private boolean nuevo;

    public Libro(String titulo, String autor, String identificador, double precio, String categoria, boolean nuevo) {
        super(titulo, precio);
        this.autor = autor;
        this.identificador = identificador;
        this.categoria = categoria;
        this.nuevo = nuevo;
    }

    public String getAutor() {
        return autor;
    }

    @Override
    public int getVentasByDate(LocalDate fecha) {
        return ventasPorFecha.getOrDefault(fecha, 0);
    }
}

class JuegoMesa extends Producto {
    private int edadRecomendada;
    private String tematica;

    public JuegoMesa(String titulo, int edadRecomendada, String tematica) {
        super(titulo, 0); // Precio de juego de mesa no especificado en el enunciado
        this.edadRecomendada = edadRecomendada;
        this.tematica = tematica;
    }

    public int getEdadRecomendada() {
        return edadRecomendada;
    }

    @Override
    public int getVentasByDate(LocalDate fecha) {
        return ventasPorFecha.getOrDefault(fecha, 0);
    }
}

class TarjetaCredito {
    private String numeroTarjeta;
    private String fechaExpiracion;
    private String codigoSeguridad;

    public TarjetaCredito(String numeroTarjeta, String fechaExpiracion, String codigoSeguridad) {
        this.numeroTarjeta = numeroTarjeta;
        this.fechaExpiracion = fechaExpiracion;
        this.codigoSeguridad = codigoSeguridad;
    }
}
