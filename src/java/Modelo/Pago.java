
package Modelo;

import java.sql.Timestamp;

public class Pago {

    private int idPago;
    private String tipoPago;
    private double monto;
    private Timestamp fechaPago;
    private int idVenta;

    public Pago(){}

    public Pago(int idPago, String tipoPago, double monto,
            Timestamp fechaPago, int idVenta){

        this.idPago=idPago;
        this.tipoPago=tipoPago;
        this.monto=monto;
        this.fechaPago=fechaPago;
        this.idVenta=idVenta;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Timestamp getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Timestamp fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

}