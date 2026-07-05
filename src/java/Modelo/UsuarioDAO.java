package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario validar(String usuario, String password) {

        Usuario u = null;

        String sql = "SELECT * FROM Usuario "
                   + "WHERE LOWER(TRIM(nombreUsuario)) = LOWER(TRIM(?)) "
                   + "AND TRIM(contrasena) = TRIM(?)";

        try (
                Connection cn = ConexionSingleton.getConexion();
            PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                u = new Usuario();

                u.setIdUsuario(rs.getInt("idUsuario"));
                u.setNombreUsuario(rs.getString("nombreUsuario"));
                u.setCorreo(rs.getString("correo"));
                u.setRol(rs.getString("rol"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }
}