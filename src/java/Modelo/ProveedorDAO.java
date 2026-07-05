package Modelo;

import Modelo.Proveedor;
import Utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO implements GenericDAO<Proveedor, Integer> {

    @Override
    public List<Proveedor> listar() throws SQLException {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT idProveedor, razonSocial, ruc, telefono, direccion, correo " +
                     "FROM Proveedor ORDER BY razonSocial";

        try(Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()){
                lista.add(new Proveedor(
                    rs.getInt("idProveedor"),
                    rs.getString("razonSocial"),
                    rs.getString("ruc"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getString("correo")
                ));
            }
        }
        return lista;
    }

    @Override
    public Proveedor buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT idProveedor, razonSocial, ruc, telefono, direccion, correo " +
                     "FROM Proveedor WHERE idProveedor = ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Proveedor(
                        rs.getInt("idProveedor"),
                        rs.getString("razonSocial"),
                        rs.getString("ruc"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("correo")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void insertar(Proveedor p) throws SQLException {
        String sql = "INSERT INTO Proveedor (razonSocial, ruc, telefono, direccion, correo) VALUES (?,?,?,?,?)";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setString(1, p.getRazonSocial());
            ps.setString(2, p.getRuc());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getDireccion());
            ps.setString(5, p.getCorreo());
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Proveedor p) throws SQLException {
        String sql = "UPDATE Proveedor SET razonSocial=?, ruc=?, telefono=?, direccion=?, correo=? WHERE idProveedor=?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setString(1, p.getRazonSocial());
            ps.setString(2, p.getRuc());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getDireccion());
            ps.setString(5, p.getCorreo());
            ps.setInt(6, p.getIdProveedor());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(Integer id) throws SQLException {
        String sql = "DELETE FROM Proveedor WHERE idProveedor = ?";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}