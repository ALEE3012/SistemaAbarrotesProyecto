package Modelo;

import Utilidades.Conexion;
import java.sql.*;

public class VentasDAO {

    public double totalVentasHoy() {

        double total = 0;

        try {
            Connection cn = Conexion.conectar();

            String sql = "SELECT SUM(total) FROM Venta WHERE DATE(fechaVenta) = CURDATE()";

            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public int totalTransacciones() {

        int total = 0;

        try {
            Connection cn = Conexion.conectar();

            String sql = "SELECT COUNT(*) FROM Venta";

            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
}