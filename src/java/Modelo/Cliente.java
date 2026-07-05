package Modelo;

public class Cliente {

    private int idCliente;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String direccion;

    public Cliente(){}

    public Cliente(int idCliente, String nombres, String apellidos, String telefono, String direccion){
        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getIdCliente(){ return idCliente; }
    public void setIdCliente(int idCliente){ this.idCliente = idCliente; }

    public String getNombres(){ return nombres; }
    public void setNombres(String nombres){ this.nombres = nombres; }

    public String getApellidos(){ return apellidos; }
    public void setApellidos(String apellidos){ this.apellidos = apellidos; }

    public String getTelefono(){ return telefono; }
    public void setTelefono(String telefono){ this.telefono = telefono; }

    public String getDireccion(){ return direccion; }
    public void setDireccion(String direccion){ this.direccion = direccion; }

    public String getNombreCompleto(){ return nombres + " " + apellidos; }
}