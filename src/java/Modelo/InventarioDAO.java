package Modelo;

import Utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO {

    /** Calcula el estado de stock a partir de las cantidades. */
    public static String calcularEstado(int actual, int minimo){
        if(actual <= 0) return "AGOTADO";
        if(actual <= minimo) return "BAJO";
        return "OK";
    }

    /**
     * Devuelve el inventario ya unido con el nombre del producto.
     * Cada fila: {idInventario, idProducto, nombreProducto, stockActual, stockMinimo, fechaActualizacion, estadoStock}
     */
    public List<Object[]> listarConProducto() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT i.idInventario, i.idProducto, p.nombreProducto, i.stockActual, " +
                     "i.stockMinimo, i.fechaActualizacion, i.estadoStock " +
                     "FROM Inventario i INNER JOIN Producto p ON p.idProducto = i.idProducto " +
                     "ORDER BY i.estadoStock = 'OK', p.nombreProducto";

        try(Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()){
                lista.add(new Object[]{
                    rs.getInt("idInventario"),
                    rs.getInt("idProducto"),
                    rs.getString("nombreProducto"),
                    rs.getInt("stockActual"),
                    rs.getInt("stockMinimo"),
                    rs.getDate("fechaActualizacion"),
                    rs.getString("estadoStock")
                });
            }
        }
        return lista;
    }

    public boolean existeParaProducto(int idProducto) throws SQLException {
        String sql = "SELECT 1 FROM Inventario WHERE idProducto = ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, idProducto);
            try(ResultSet rs = ps.executeQuery()){
                return rs.next();
            }
        }
    }

    /** Crea la fila de inventario inicial cuando se registra un producto nuevo. */
    public void crearInventarioInicial(int idProducto, int stockInicial, int stockMinimo) throws SQLException {
        String sql = "INSERT INTO Inventario (stockActual, stockMinimo, fechaActualizacion, estadoStock, idProducto) " +
                     "VALUES (?, ?, CURDATE(), ?, ?)";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, stockInicial);
            ps.setInt(2, stockMinimo);
            ps.setString(3, calcularEstado(stockInicial, stockMinimo));
            ps.setInt(4, idProducto);
            ps.executeUpdate();
        }
    }

    /**
     * Suma (o resta, con delta negativo) unidades al stock de un producto.
     * El stock nunca baja de 0. Se usa tanto en ventas (delta negativo) como en compras (delta positivo).
     */
    public void ajustarStock(int idProducto, int cantidad) throws SQLException {

        Connection cn = Conexion.conectar();

        // Actualizar stock
        String sql = "UPDATE Inventario SET stockActual = stockActual + ? WHERE idProducto = ?";

        PreparedStatement ps = cn.prepareStatement(sql);

        ps.setInt(1, cantidad);
        ps.setInt(2, idProducto);

        ps.executeUpdate();

        // Obtener el nuevo stock y el stock mínimo
        String consulta = "SELECT stockActual, stockMinimo FROM Inventario WHERE idProducto = ?";

        PreparedStatement ps2 = cn.prepareStatement(consulta);

        ps2.setInt(1, idProducto);

        ResultSet rs = ps2.executeQuery();

        if(rs.next()){

            int nuevoStock = rs.getInt("stockActual");
            int stockMinimo = rs.getInt("stockMinimo");

            InventarioSubject subject = new InventarioSubject();

            subject.agregarObserver(new EstadoStockObserver());
            subject.agregarObserver(new MovimientoObserver());

            subject.notificarObservers(
                    idProducto,
                    nuevoStock,
                    stockMinimo
            );
        }

        rs.close();
        ps.close();
        ps2.close();
        cn.close();
    }

    public void actualizarStockMinimo(int idProducto, int nuevoMinimo) throws SQLException {
        String sql = "UPDATE Inventario SET stockMinimo = ? WHERE idProducto = ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, nuevoMinimo);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
        }
    }
    
    public void actualizarEstadoStock(int idProducto,
                                      int stockActual,
                                      int stockMinimo) throws SQLException {

        String estado = calcularEstado(stockActual, stockMinimo);

        String sql = "UPDATE Inventario SET estadoStock = ? WHERE idProducto = ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setString(1, estado);
            ps.setInt(2, idProducto);

            ps.executeUpdate();
        }
    }
}