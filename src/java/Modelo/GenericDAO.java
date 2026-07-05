package Modelo;

import java.sql.SQLException;
import java.util.List;


public interface GenericDAO<T, ID> {

    List<T> listar() throws SQLException;

    T buscarPorId(ID id) throws SQLException;

    void insertar(T entidad) throws SQLException;

    void actualizar(T entidad) throws SQLException;

    void eliminar(ID id) throws SQLException;
}