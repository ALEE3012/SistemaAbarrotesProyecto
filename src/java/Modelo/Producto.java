package Modelo;

import java.sql.Date;

public class Producto {

    private int idProducto;
    private String nombreProducto;
    private String descripcion;
    private double precioCompra;
    private double precioVenta;
    private Date fechaVencimiento;
    private int idCategoria;
    private Integer idProveedor; // puede ser null

    public Producto(){}

    public Producto(int idProducto, String nombreProducto, String descripcion,
                     double precioCompra, double precioVenta, Date fechaVencimiento,
                     int idCategoria, Integer idProveedor){
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.fechaVencimiento = fechaVencimiento;
        this.idCategoria = idCategoria;
        this.idProveedor = idProveedor;
    }

    public int getIdProducto(){ return idProducto; }
    public void setIdProducto(int idProducto){ this.idProducto = idProducto; }

    public String getNombreProducto(){ return nombreProducto; }
    public void setNombreProducto(String nombreProducto){ this.nombreProducto = nombreProducto; }

    public String getDescripcion(){ return descripcion; }
    public void setDescripcion(String descripcion){ this.descripcion = descripcion; }

    public double getPrecioCompra(){ return precioCompra; }
    public void setPrecioCompra(double precioCompra){ this.precioCompra = precioCompra; }

    public double getPrecioVenta(){ return precioVenta; }
    public void setPrecioVenta(double precioVenta){ this.precioVenta = precioVenta; }

    public Date getFechaVencimiento(){ return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento){ this.fechaVencimiento = fechaVencimiento; }

    public int getIdCategoria(){ return idCategoria; }
    public void setIdCategoria(int idCategoria){ this.idCategoria = idCategoria; }

    public Integer getIdProveedor(){ return idProveedor; }
    public void setIdProveedor(Integer idProveedor){ this.idProveedor = idProveedor; }
}