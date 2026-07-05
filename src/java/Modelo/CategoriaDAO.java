package Modelo;

import Modelo.Categoria;
import Utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> listar() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT idCategoria, nombreCategoria, descripcion FROM Categoria ORDER BY nombreCategoria";

        try(Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()){
                lista.add(new Categoria(
                    rs.getInt("idCategoria"),
                    rs.getString("nombreCategoria"),
                    rs.getString("descripcion")
                ));
            }
        }
        return lista;
    }

    /** Busca una categoría por nombre (sin distinguir mayúsculas); si no existe, la crea. */
    public int buscarOCrear(String nombreCategoria) throws SQLException {
        String buscar = "SELECT idCategoria FROM Categoria WHERE LOWER(nombreCategoria) = LOWER(?) LIMIT 1";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(buscar)){

            ps.setString(1, nombreCategoria);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return rs.getInt("idCategoria");
            }
        }

        String insertar = "INSERT INTO Categoria (nombreCategoria, descripcion) VALUES (?, ?)";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(insertar, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, nombreCategoria);
            ps.setString(2, "Creada automáticamente");
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo crear la categoría");
    }

    public void insertar(Categoria c) throws SQLException {
        String sql = "INSERT INTO Categoria (nombreCategoria, descripcion) VALUES (?,?)";

        try(Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(sql)){

            ps.setString(1, c.getNombreCategoria());
            ps.setString(2, c.getDescripcion());
            ps.executeUpdate();
        }
    }
}