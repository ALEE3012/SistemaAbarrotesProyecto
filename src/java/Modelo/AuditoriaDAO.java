package Modelo;

import Utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Registra en la tabla Auditoria cada acción relevante del sistema (alta, edición, baja),
 * indicando quién la hizo y cuándo. Se llama desde los Servlets después de una operación exitosa.
 */
public class AuditoriaDAO {

    public void registrar(String tablaAfectada, String accion, Integer idRegistroAfectado,
                           String detalle, int idUsuario) throws SQLException {

        String sql = "INSERT INTO Auditoria (tablaAfectada, accion, idRegistroAfectado, detalle, fechaHora, idUsuario) " +
                     "VALUES (?,?,?,?,NOW(),?)";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setString(1, tablaAfectada);
            ps.setString(2, accion);
            if(idRegistroAfectado != null){
                ps.setInt(3, idRegistroAfectado);
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, detalle);
            ps.setInt(5, idUsuario);
            ps.executeUpdate();
        }
    }

    /** Bitácora completa, con el nombre del usuario que hizo cada acción, para mostrarla en pantalla. */
    public List<Object[]> listarConUsuario(int limite) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT a.idAuditoria, a.tablaAfectada, a.accion, a.idRegistroAfectado, " +
                     "a.detalle, a.fechaHora, u.nombreUsuario " +
                     "FROM Auditoria a INNER JOIN Usuario u ON u.idUsuario = a.idUsuario " +
                     "ORDER BY a.fechaHora DESC LIMIT ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, limite);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(new Object[]{
                        rs.getInt("idAuditoria"),
                        rs.getString("tablaAfectada"),
                        rs.getString("accion"),
                        rs.getObject("idRegistroAfectado"),
                        rs.getString("detalle"),
                        rs.getTimestamp("fechaHora"),
                        rs.getString("nombreUsuario")
                    });
                }
            }
        }
        return lista;
    }
}