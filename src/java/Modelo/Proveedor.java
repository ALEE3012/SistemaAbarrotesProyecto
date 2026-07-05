package Modelo;

public class Proveedor {

    private int idProveedor;
    private String razonSocial;
    private String ruc;
    private String telefono;
    private String direccion;
    private String correo;

    public Proveedor(){}

    public Proveedor(int idProveedor, String razonSocial, String ruc, String telefono, String direccion, String correo){
        this.idProveedor = idProveedor;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
    }

    public int getIdProveedor(){ return idProveedor; }
    public void setIdProveedor(int idProveedor){ this.idProveedor = idProveedor; }

    public String getRazonSocial(){ return razonSocial; }
    public void setRazonSocial(String razonSocial){ this.razonSocial = razonSocial; }

    public String getRuc(){ return ruc; }
    public void setRuc(String ruc){ this.ruc = ruc; }

    public String getTelefono(){ return telefono; }
    public void setTelefono(String telefono){ this.telefono = telefono; }

    public String getDireccion(){ return direccion; }
    public void setDireccion(String direccion){ this.direccion = direccion; }

    public String getCorreo(){ return correo; }
    public void setCorreo(String correo){ this.correo = correo; }
}