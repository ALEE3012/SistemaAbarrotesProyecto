package Modelo;

import java.sql.SQLException;

public class EstadoStockObserver implements Observer {

    @Override
    public void actualizar(int idProducto,
                           int stockActual,
                           int stockMinimo) {

        try{

            InventarioDAO dao = new InventarioDAO();

            dao.actualizarEstadoStock(
                    idProducto,
                    stockActual,
                    stockMinimo
            );

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

}