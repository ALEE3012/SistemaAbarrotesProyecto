package Modelo;

public class MovimientoObserver implements Observer {

    @Override
    public void actualizar(int idProducto,
                           int stockActual,
                           int stockMinimo) {

        System.out.println(
            "Movimiento de inventario del producto "
            + idProducto +
            ". Nuevo stock: "
            + stockActual
        );

    }

}