<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.ProveedorDAO"%>
<%@page import="Modelo.Proveedor"%>
<%
    ProveedorDAO proveedorDAO = new ProveedorDAO();
    List<Proveedor> proveedores = null;
    Proveedor proveedorEditar = null;
    String errorCarga = null;

    try{
        proveedores = proveedorDAO.listar();
        String idEditar = request.getParameter("editar");
        if(idEditar != null){
            proveedorEditar = proveedorDAO.buscarPorId(Integer.parseInt(idEditar));
        }
    } catch(Exception e){
        e.printStackTrace();
        errorCarga = "No se pudo cargar la lista de proveedores: " + e.getMessage();
    }

    String msjError = (String) session.getAttribute("mensajeError");
    if(msjError != null) session.removeAttribute("mensajeError");

    boolean modoEdicion = (proveedorEditar != null);
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Proveedores | La Casa del Gato</title>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/Estilo.css">
</head>
<body>

<div class="contenedor">

    <a href="principal.jsp" class="btn-regresar"><i class="fa-solid fa-arrow-left"></i> Regresar al Dashboard</a>

    <div class="cabecera">
        <div class="titulo">
            <div class="icono"><i class="fa-solid fa-truck-fast"></i></div>
            <div>
                <h1>Proveedores</h1>
                <p><%= proveedores != null ? proveedores.size() : 0 %> proveedores registrados</p>
            </div>
        </div>
    </div>

    <% if(errorCarga != null){ %><div class="mensaje-error"><%= errorCarga %></div><% } %>
    <% if(msjError != null){ %><div class="mensaje-error"><%= msjError %></div><% } %>

    <div class="panel">
        <h2><i class="fa-solid fa-<%= modoEdicion ? "pen" : "circle-plus" %>"></i> <%= modoEdicion ? "Editar proveedor" : "Nuevo proveedor" %></h2>

        <form action="/ProveedorServlet" method="post">
            <input type="hidden" name="accion" value="<%= modoEdicion ? "editar" : "agregar" %>">
            <% if(modoEdicion){ %>
            <input type="hidden" name="idProveedor" value="<%= proveedorEditar.getIdProveedor() %>">
            <% } %>

            <div class="grid-2">
                <div class="campo">
                    <label>Razón social</label>
                    <input type="text" name="razonSocial" required
                        value="<%= modoEdicion ? proveedorEditar.getRazonSocial() : "" %>">
                </div>
                <div class="campo">
                    <label>RUC</label>
                    <input type="text" name="ruc" required
                        value="<%= modoEdicion ? proveedorEditar.getRuc() : "" %>">
                </div>
                <div class="campo">
                    <label>Teléfono</label>
                    <input type="text" name="telefono"
                        value="<%= modoEdicion && proveedorEditar.getTelefono() != null ? proveedorEditar.getTelefono() : "" %>">
                </div>
                <div class="campo">
                    <label>Correo</label>
                    <input type="email" name="correo"
                        value="<%= modoEdicion && proveedorEditar.getCorreo() != null ? proveedorEditar.getCorreo() : "" %>">
                </div>
                <div class="campo" style="grid-column:1/-1;">
                    <label>Dirección</label>
                    <input type="text" name="direccion"
                        value="<%= modoEdicion && proveedorEditar.getDireccion() != null ? proveedorEditar.getDireccion() : "" %>">
                </div>
            </div>

            <div class="botones-finales">
                <button type="submit" class="btn-guardar">
                    <i class="fa-solid fa-floppy-disk"></i> <%= modoEdicion ? "Actualizar proveedor" : "Guardar proveedor" %>
                </button>
                <% if(modoEdicion){ %>
                <a href="proveedores.jsp" class="btn-limpiar">Cancelar</a>
                <% } %>
            </div>
        </form>
    </div>

    <div class="panel">
        <h2><i class="fa-solid fa-list"></i> Lista de proveedores</h2>

        <div class="tabla-wrap">
        <table>
            <thead>
                <tr><th>Razón social</th><th>RUC</th><th>Teléfono</th><th>Correo</th><th></th></tr>
            </thead>
            <tbody>
                <% if(proveedores == null || proveedores.isEmpty()){ %>
                <tr class="vacio"><td colspan="5">No hay proveedores registrados todavía</td></tr>
                <% } else {
                    for(Proveedor p : proveedores){
                %>
                <tr>
                    <td><%= p.getRazonSocial() %></td>
                    <td><%= p.getRuc() %></td>
                    <td><%= p.getTelefono() != null ? p.getTelefono() : "—" %></td>
                    <td><%= p.getCorreo() != null ? p.getCorreo() : "—" %></td>
                    <td>
                        <div class="acciones-fila">
                            <a href="proveedores.jsp?editar=<%= p.getIdProveedor() %>" class="btn-editar" title="Editar">
                                <i class="fa-solid fa-pen"></i>
                            </a>
                            <form action="/ProveedorServlet" method="post" onsubmit="return confirm('¿Eliminar a <%= p.getRazonSocial() %>?');" style="display:inline;">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="idProveedor" value="<%= p.getIdProveedor() %>">
                                <button type="submit" class="btn-eliminar" title="Eliminar"><i class="fa-solid fa-trash"></i></button>
                            </form>
                        </div>
                    </td>
                </tr>
                <% } } %>
            </tbody>
        </table>
        </div>
    </div>

</div>

</body>
</html>
