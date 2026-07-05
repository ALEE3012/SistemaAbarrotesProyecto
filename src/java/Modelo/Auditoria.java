package Modelo;

import java.sql.Timestamp;

public class Auditoria {

    private int idAuditoria;
    private String tablaAfectada;
    private String accion;
    private Integer idRegistroAfectado;
    private String detalle;
    private Timestamp fechaHora;
    private int idUsuario;

    public Auditoria(){}

    public Auditoria(int idAuditoria, String tablaAfectada, String accion, Integer idRegistroAfectado,
                      String detalle, Timestamp fechaHora, int idUsuario){
        this.idAuditoria = idAuditoria;
        this.tablaAfectada = tablaAfectada;
        this.accion = accion;
        this.idRegistroAfectado = idRegistroAfectado;
        this.detalle = detalle;
        this.fechaHora = fechaHora;
        this.idUsuario = idUsuario;
    }

    public int getIdAuditoria(){ return idAuditoria; }
    public void setIdAuditoria(int idAuditoria){ this.idAuditoria = idAuditoria; }

    public String getTablaAfectada(){ return tablaAfectada; }
    public void setTablaAfectada(String tablaAfectada){ this.tablaAfectada = tablaAfectada; }

    public String getAccion(){ return accion; }
    public void setAccion(String accion){ this.accion = accion; }

    public Integer getIdRegistroAfectado(){ return idRegistroAfectado; }
    public void setIdRegistroAfectado(Integer idRegistroAfectado){ this.idRegistroAfectado = idRegistroAfectado; }

    public String getDetalle(){ return detalle; }
    public void setDetalle(String detalle){ this.detalle = detalle; }

    public Timestamp getFechaHora(){ return fechaHora; }
    public void setFechaHora(Timestamp fechaHora){ this.fechaHora = fechaHora; }

    public int getIdUsuario(){ return idUsuario; }
    public void setIdUsuario(int idUsuario){ this.idUsuario = idUsuario; }
}