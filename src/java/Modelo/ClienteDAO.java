package Modelo;

import Modelo.Cliente;
import Utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements GenericDAO<Cliente, Integer> {

    @Override
    public List<Cliente> listar() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT idCliente, nombres, apellidos, telefono, direccion FROM Cliente ORDER BY nombres";

        try(Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()){
                lista.add(new Cliente(
                    rs.getInt("idCliente"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("telefono"),
                    rs.getString("direccion")
                ));
            }
        }
        return lista;
    }

    @Override
    public Cliente buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT idCliente, nombres, apellidos, telefono, direccion FROM Cliente WHERE idCliente = ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                    );
                }
            }
        }
        return null;
    }

    /** Usado por VentaServlet para reutilizar un cliente ya registrado en vez de duplicarlo. */
    public Cliente buscarPorNombreCompleto(String nombres, String apellidos) throws SQLException {
        String sql = "SELECT idCliente, nombres, apellidos, telefono, direccion FROM Cliente " +
                     "WHERE LOWER(nombres) = LOWER(?) AND LOWER(apellidos) = LOWER(?) LIMIT 1";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                    );
                }
            }
        }
        return null;
    }

    /** Cumple el contrato de GenericDAO. Internamente reutiliza insertarYObtenerId(). */
    @Override
    public void insertar(Cliente c) throws SQLException {
        insertarYObtenerId(c);
    }

    /** Además de insertar, devuelve el id generado (lo necesitan los flujos que crean un cliente al vuelo). */
    public int insertarYObtenerId(Cliente c) throws SQLException {
        String sql = "INSERT INTO Cliente (nombres, apellidos, telefono, direccion) VALUES (?,?,?,?)";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, c.getNombres());
            ps.setString(2, c.getApellidos());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getDireccion());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo obtener el idCliente generado");
    }

    @Override
    public void actualizar(Cliente c) throws SQLException {
        String sql = "UPDATE Cliente SET nombres=?, apellidos=?, telefono=?, direccion=? WHERE idCliente=?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setString(1, c.getNombres());
            ps.setString(2, c.getApellidos());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getDireccion());
            ps.setInt(5, c.getIdCliente());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(Integer id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}