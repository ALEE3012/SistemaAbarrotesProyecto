package Modelo;

import Utilidades.Conexion;
import java.sql.*;

public class PagoDAO {

    public void insertar(Pago pago) throws SQLException{

        String sql="INSERT INTO Pago(tipoPago,monto,fechaPago,idVenta) VALUES(?,?,NOW(),?)";

        try(Connection cn=Conexion.conectar();
            PreparedStatement ps=cn.prepareStatement(sql)){

            ps.setString(1,pago.getTipoPago());
            ps.setDouble(2,pago.getMonto());
            ps.setInt(3,pago.getIdVenta());

            ps.executeUpdate();
        }
    }

}