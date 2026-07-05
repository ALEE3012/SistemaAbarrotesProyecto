package Modelo;

import java.sql.Date;

public class Inventario {

    private int idInventario;
    private int stockActual;
    private int stockMinimo;
    private Date fechaActualizacion;
    private String estadoStock; // OK / BAJO / AGOTADO
    private int idProducto;

    public Inventario(){}

    public Inventario(int idInventario, int stockActual, int stockMinimo,
                       Date fechaActualizacion, String estadoStock, int idProducto){
        this.idInventario = idInventario;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.fechaActualizacion = fechaActualizacion;
        this.estadoStock = estadoStock;
        this.idProducto = idProducto;
    }

    public int getIdInventario(){ return idInventario; }
    public void setIdInventario(int idInventario){ this.idInventario = idInventario; }

    public int getStockActual(){ return stockActual; }
    public void setStockActual(int stockActual){ this.stockActual = stockActual; }

    public int getStockMinimo(){ return stockMinimo; }
    public void setStockMinimo(int stockMinimo){ this.stockMinimo = stockMinimo; }

    public Date getFechaActualizacion(){ return fechaActualizacion; }
    public void setFechaActualizacion(Date fechaActualizacion){ this.fechaActualizacion = fechaActualizacion; }

    public String getEstadoStock(){ return estadoStock; }
    public void setEstadoStock(String estadoStock){ this.estadoStock = estadoStock; }

    public int getIdProducto(){ return idProducto; }
    public void setIdProducto(int idProducto){ this.idProducto = idProducto; }
}