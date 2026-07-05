package Modelo;

import Modelo.DetalleVenta;
import Utilidades.Conexion;
import java.sql.*;

public class DetalleVentaDAO {

    public void insertar(DetalleVenta d) throws SQLException {
        String sql = "INSERT INTO DetalleVenta (cantidad, precioUnitario, subtotal, idVenta, idProducto) " +
                     "VALUES (?,?,?,?,?)";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, d.getCantidad());
            ps.setDouble(2, d.getPrecioUnitario());
            ps.setDouble(3, d.getSubtotal());
            ps.setInt(4, d.getIdVenta());
            ps.setInt(5, d.getIdProducto());
            ps.executeUpdate();
        }
    }
}