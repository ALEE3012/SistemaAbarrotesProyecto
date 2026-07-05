package Modelo;

import java.sql.Timestamp;

public class Venta {

    private int idVenta;
    private Timestamp fechaVenta;
    private double total;
    private String estadoVenta;
    private int idCliente;
    private int idUsuario;

    public Venta(){}

    public Venta(int idVenta, Timestamp fechaVenta, double total, String estadoVenta, int idCliente, int idUsuario){
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estadoVenta = estadoVenta;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
    }

    public int getIdVenta(){ return idVenta; }
    public void setIdVenta(int idVenta){ this.idVenta = idVenta; }

    public Timestamp getFechaVenta(){ return fechaVenta; }
    public void setFechaVenta(Timestamp fechaVenta){ this.fechaVenta = fechaVenta; }

    public double getTotal(){ return total; }
    public void setTotal(double total){ this.total = total; }

    public String getEstadoVenta(){ return estadoVenta; }
    public void setEstadoVenta(String estadoVenta){ this.estadoVenta = estadoVenta; }

    public int getIdCliente(){ return idCliente; }
    public void setIdCliente(int idCliente){ this.idCliente = idCliente; }

    public int getIdUsuario(){ return idUsuario; }
    public void setIdUsuario(int idUsuario){ this.idUsuario = idUsuario; }
}