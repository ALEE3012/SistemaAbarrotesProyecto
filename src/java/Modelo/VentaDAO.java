package Modelo;

import Modelo.Venta;
import Utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public int insertarYObtenerId(Venta v) throws SQLException {
        String sql = "INSERT INTO Venta (fechaVenta, total, estadoVenta, idCliente, idUsuario) VALUES (NOW(),?,?,?,?)";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setDouble(1, v.getTotal());
            ps.setString(2, v.getEstadoVenta());
            ps.setInt(3, v.getIdCliente());
            ps.setInt(4, v.getIdUsuario());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo obtener el idVenta generado");
    }

    /** Últimas ventas para el panel de reportes. Cada fila: {idVenta, cliente, fecha, total, estado} */
    public List<Object[]> ventasRecientes(int limite) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT v.idVenta, CONCAT(c.nombres,' ',c.apellidos) AS cliente, " +
                     "v.fechaVenta, v.total, v.estadoVenta " +
                     "FROM Venta v INNER JOIN Cliente c ON c.idCliente = v.idCliente " +
                     "ORDER BY v.fechaVenta DESC LIMIT ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, limite);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(new Object[]{
                        rs.getInt("idVenta"),
                        rs.getString("cliente"),
                        rs.getTimestamp("fechaVenta"),
                        rs.getDouble("total"),
                        rs.getString("estadoVenta")
                    });
                }
            }
        }
        return lista;
    }

    /** Total vendido por día en los últimos N días. Cada fila: {fecha (String), total} */
    public List<Object[]> totalesUltimosDias(int dias) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT DATE(fechaVenta) AS dia, SUM(total) AS total " +
                     "FROM Venta WHERE DATE(fechaVenta) >= DATE_SUB(CURDATE(), INTERVAL ?-1 DAY) " +
                     "GROUP BY DATE(fechaVenta) ORDER BY dia";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, dias);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(new Object[]{ rs.getDate("dia"), rs.getDouble("total") });
                }
            }
        }
        return lista;
    }

    /** Productos más vendidos por cantidad. Cada fila: {nombreProducto, unidadesVendidas, totalGenerado} */
    public List<Object[]> productosMasVendidos(int limite) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.nombreProducto, SUM(d.cantidad) AS unidades, SUM(d.subtotal) AS total " +
                     "FROM DetalleVenta d INNER JOIN Producto p ON p.idProducto = d.idProducto " +
                     "GROUP BY p.idProducto, p.nombreProducto ORDER BY unidades DESC LIMIT ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, limite);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(new Object[]{
                        rs.getString("nombreProducto"),
                        rs.getInt("unidades"),
                        rs.getDouble("total")
                    });
                }
            }
        }
        return lista;
    }

    public double totalVentasDelDia() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total),0) AS total FROM Venta WHERE DATE(fechaVenta) = CURDATE()";

        try(Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            if(rs.next()) return rs.getDouble("total");
        }
        return 0;
    }

    public int pedidosDelMes() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Venta " +
                     "WHERE MONTH(fechaVenta) = MONTH(CURDATE()) AND YEAR(fechaVenta) = YEAR(CURDATE())";

        try(Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            if(rs.next()) return rs.getInt("total");
        }
        return 0;
    }
}