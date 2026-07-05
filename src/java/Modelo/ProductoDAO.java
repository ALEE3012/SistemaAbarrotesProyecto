package Modelo;

import Modelo.Producto;
import Utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Object[]> listarConDetalle() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql =
            "SELECT p.idProducto, p.nombreProducto, c.nombreCategoria, p.precioCompra, p.precioVenta, " +
            "       pr.razonSocial, i.stockActual, i.stockMinimo, i.estadoStock " +
            "FROM Producto p " +
            "INNER JOIN Categoria c ON c.idCategoria = p.idCategoria " +
            "LEFT JOIN Proveedor pr ON pr.idProveedor = p.idProveedor " +
            "LEFT JOIN Inventario i ON i.idProducto = p.idProducto " +
            "ORDER BY p.nombreProducto";

        try(Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()){
                lista.add(new Object[]{
                    rs.getInt("idProducto"),
                    rs.getString("nombreProducto"),
                    rs.getString("nombreCategoria"),
                    rs.getDouble("precioCompra"),
                    rs.getDouble("precioVenta"),
                    rs.getString("razonSocial"),
                    (Object) rs.getObject("stockActual"),
                    (Object) rs.getObject("stockMinimo"),
                    rs.getString("estadoStock")
                });
            }
        }
        return lista;
    }

    /**
     * Lista simplificada para el buscador de Nueva Venta: solo productos que ya
     * tienen fila en Inventario. Cada fila: {idProducto, nombreProducto, precioVenta, stockActual}
     */
    public List<Object[]> listarDisponiblesParaVenta() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.idProducto, p.nombreProducto, p.precioVenta, i.stockActual " +
                     "FROM Producto p INNER JOIN Inventario i ON i.idProducto = p.idProducto " +
                     "ORDER BY p.nombreProducto";

        try(Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()){
                lista.add(new Object[]{
                    rs.getInt("idProducto"),
                    rs.getString("nombreProducto"),
                    rs.getDouble("precioVenta"),
                    rs.getInt("stockActual")
                });
            }
        }
        return lista;
    }

    /**
     * Busca UN producto (con su inventario) por nombre exacto (sin distinguir mayúsculas/minúsculas).
     * Usado por VentaServlet en el flujo de venta manual: el producto debe existir de antemano
     * (con inventario ya registrado); si no existe, se devuelve null y el servlet pide registrarlo
     * primero en Productos, en vez de crearlo al vuelo con datos incompletos.
     * Retorna: {idProducto, nombreProducto, precioVenta, stockActual} o null si no se encontró.
     */
public Object[] buscarPorIdParaVenta(int idProducto) throws SQLException {

    String sql = "SELECT p.idProducto, p.nombreProducto, p.precioVenta, i.stockActual "
               + "FROM Producto p "
               + "INNER JOIN Inventario i ON i.idProducto = p.idProducto "
               + "WHERE p.idProducto = ?";

    try(Connection cn = Conexion.conectar();
        PreparedStatement ps = cn.prepareStatement(sql)){

        ps.setInt(1, idProducto);

        try(ResultSet rs = ps.executeQuery()){

            if(rs.next()){

                return new Object[]{
                    rs.getInt("idProducto"),
                    rs.getString("nombreProducto"),
                    rs.getDouble("precioVenta"),
                    rs.getInt("stockActual")
                };

            }

        }

    }

    return null;
}

    public Producto buscarPorId(int id) throws SQLException {
        String sql = "SELECT idProducto, nombreProducto, descripcion, precioCompra, precioVenta, " +
                     "fechaVencimiento, idCategoria, idProveedor FROM Producto WHERE idProducto = ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Integer idProveedor = (Integer) rs.getObject("idProveedor");
                    return new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombreProducto"),
                        rs.getString("descripcion"),
                        rs.getDouble("precioCompra"),
                        rs.getDouble("precioVenta"),
                        rs.getDate("fechaVencimiento"),
                        rs.getInt("idCategoria"),
                        idProveedor
                    );
                }
            }
        }
        return null;
    }

    /** Usado por VentaServlet: busca un producto por nombre; si no existe, lo crea (con su inventario). */
    public int buscarOCrearPorNombre(String nombreProducto, int idCategoria,
                                      double precioCompra, double precioVenta,
                                      Integer idProveedor, int stockInicial, int stockMinimo) throws SQLException {

        String buscar = "SELECT idProducto FROM Producto "
              + "WHERE LOWER(nombreProducto)=LOWER(?) "
              + "LIMIT 1";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(buscar)){

            ps.setString(1, nombreProducto);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return rs.getInt("idProducto");
            }
        }

        int idProductoNuevo = insertarYObtenerId(new Producto(
            0, nombreProducto, null, precioCompra, precioVenta, null, idCategoria, idProveedor
        ));

        new InventarioDAO().crearInventarioInicial(idProductoNuevo, stockInicial, stockMinimo);

        return idProductoNuevo;
    }

    public int insertarYObtenerId(Producto p) throws SQLException {
        String sql = "INSERT INTO Producto (nombreProducto, descripcion, precioCompra, precioVenta, " +
                     "fechaVencimiento, idCategoria, idProveedor) VALUES (?,?,?,?,?,?,?)";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecioCompra());
            ps.setDouble(4, p.getPrecioVenta());
            ps.setDate(5, p.getFechaVencimiento());
            ps.setInt(6, p.getIdCategoria());
            if(p.getIdProveedor() != null){
                ps.setInt(7, p.getIdProveedor());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo obtener el idProducto generado");
    }

    public void actualizar(Producto p) throws SQLException {

        String sql =
            "UPDATE Producto SET " +
            "nombreProducto=?, " +
            "descripcion=?, " +
            "precioCompra=?, " +
            "precioVenta=?, " +
            "fechaVencimiento=?, " +
            "idCategoria=?, " +
            "idProveedor=? " +
            "WHERE idProducto=?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecioCompra());
            ps.setDouble(4, p.getPrecioVenta());
            ps.setDate(5, p.getFechaVencimiento());
            ps.setInt(6, p.getIdCategoria());

            if(p.getIdProveedor() != null){
                ps.setInt(7, p.getIdProveedor());
            }else{
                ps.setNull(7, Types.INTEGER);
            }

            ps.setInt(8, p.getIdProducto());

            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        // se borra primero el inventario asociado por la FK idProducto UNIQUE
        try(Connection cn = Conexion.conectar();
            PreparedStatement ps1 = cn.prepareStatement("DELETE FROM Inventario WHERE idProducto = ?");
            PreparedStatement ps2 = cn.prepareStatement("DELETE FROM Producto WHERE idProducto = ?")){

            ps1.setInt(1, id);
            ps1.executeUpdate();

            ps2.setInt(1, id);
            ps2.executeUpdate();
        }
    }
}