package Modelo;

public interface Subject {

    void agregarObserver(Observer observer);

    void eliminarObserver(Observer observer);

    void notificarObservers(int idProducto, int stockActual, int stockMinimo);

}
