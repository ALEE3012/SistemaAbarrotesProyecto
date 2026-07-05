package Modelo;

public class DetalleVenta {

    private int idDetalleVenta;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private int idVenta;
    private int idProducto;

    public DetalleVenta(){}

    public DetalleVenta(int idDetalleVenta, int cantidad, double precioUnitario,
                         double subtotal, int idVenta, int idProducto){
        this.idDetalleVenta = idDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
    }

    public int getIdDetalleVenta(){ return idDetalleVenta; }
    public void setIdDetalleVenta(int idDetalleVenta){ this.idDetalleVenta = idDetalleVenta; }

    public int getCantidad(){ return cantidad; }
    public void setCantidad(int cantidad){ this.cantidad = cantidad; }

    public double getPrecioUnitario(){ return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario){ this.precioUnitario = precioUnitario; }

    public double getSubtotal(){ return subtotal; }
    public void setSubtotal(double subtotal){ this.subtotal = subtotal; }

    public int getIdVenta(){ return idVenta; }
    public void setIdVenta(int idVenta){ this.idVenta = idVenta; }

    public int getIdProducto(){ return idProducto; }
    public void setIdProducto(int idProducto){ this.idProducto = idProducto; }
}