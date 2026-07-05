package Modelo;

import java.util.ArrayList;
import java.util.List;

public class InventarioSubject implements Subject {

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void agregarObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void eliminarObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers(int idProducto,
                                   int stockActual,
                                   int stockMinimo) {

        for(Observer o : observers){
            o.actualizar(idProducto, stockActual, stockMinimo);
        }

    }

}
