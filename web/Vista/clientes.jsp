<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.SQLException"%>
<%@page import="Modelo.ClienteDAO"%>
<%@page import="Modelo.Cliente"%>
<%
    ClienteDAO clienteDAO = new ClienteDAO();
    List<Cliente> clientes = null;
    Cliente clienteEditar = null;
    String errorCarga = null;

    try{
        clientes = clienteDAO.listar();
        String idEditar = request.getParameter("editar");
        if(idEditar != null){
            clienteEditar = clienteDAO.buscarPorId(Integer.parseInt(idEditar));
        }
    } catch(Exception e){
        e.printStackTrace();
        errorCarga = "No se pudo cargar la lista de clientes: " + e.getMessage();
    }

    String msjError = (String) session.getAttribute("mensajeError");
    if(msjError != null) session.removeAttribute("mensajeError");

    boolean modoEdicion = (clienteEditar != null);
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Clientes | La Casa del Gato</title>

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
            <div class="icono"><i class="fa-solid fa-users"></i></div>
            <div>
                <h1>Clientes</h1>
                <p><%= clientes != null ? clientes.size() : 0 %> clientes registrados</p>
            </div>
        </div>
    </div>

    <% if(errorCarga != null){ %><div class="mensaje-error"><%= errorCarga %></div><% } %>
    <% if(msjError != null){ %><div class="mensaje-error"><%= msjError %></div><% } %>

    <!-- ============ FORMULARIO ============ -->
    <div class="panel">
        <h2><i class="fa-solid fa-<%= modoEdicion ? "user-pen" : "user-plus" %>"></i> <%= modoEdicion ? "Editar cliente" : "Nuevo cliente" %></h2>

        <form action="ClienteServlet" method="post">
            <input type="hidden" name="accion" value="<%= modoEdicion ? "editar" : "agregar" %>">
            <% if(modoEdicion){ %>
            <input type="hidden" name="idCliente" value="<%= clienteEditar.getIdCliente() %>">
            <% } %>

            <div class="grid-2">
                <div class="campo">
                    <label>Nombres</label>
                    <input type="text" name="nombres" required
                        value="<%= modoEdicion ? clienteEditar.getNombres() : "" %>">
                </div>
                <div class="campo">
                    <label>Apellidos</label>
                    <input type="text" name="apellidos" required
                        value="<%= modoEdicion ? clienteEditar.getApellidos() : "" %>">
                </div>
                <div class="campo">
                    <label>Teléfono</label>
                    <input type="text" name="telefono"
                        value="<%= modoEdicion && clienteEditar.getTelefono() != null ? clienteEditar.getTelefono() : "" %>">
                </div>
                <div class="campo">
                    <label>Dirección</label>
                    <input type="text" name="direccion"
                        value="<%= modoEdicion && clienteEditar.getDireccion() != null ? clienteEditar.getDireccion() : "" %>">
                </div>
            </div>

            <div class="botones-finales">
                <button type="submit" class="btn-guardar">
                    <i class="fa-solid fa-floppy-disk"></i> <%= modoEdicion ? "Actualizar cliente" : "Guardar cliente" %>
                </button>
                <% if(modoEdicion){ %>
                <a href="clientes.jsp" class="btn-limpiar">Cancelar</a>
                <% } %>
            </div>
        </form>
    </div>

    <!-- ============ LISTA ============ -->
    <div class="panel">
        <h2><i class="fa-solid fa-list"></i> Lista de clientes</h2>

        <div class="tabla-wrap">
        <table>
            <thead>
                <tr><th>Nombres</th><th>Apellidos</th><th>Teléfono</th><th>Dirección</th><th></th></tr>
            </thead>
            <tbody>
                <% if(clientes == null || clientes.isEmpty()){ %>
                <tr class="vacio"><td colspan="5">No hay clientes registrados todavía</td></tr>
                <% } else {
                    for(Cliente c : clientes){
                %>
                <tr>
                    <td><%= c.getNombres() %></td>
                    <td><%= c.getApellidos() %></td>
                    <td><%= c.getTelefono() != null ? c.getTelefono() : "—" %></td>
                    <td><%= c.getDireccion() != null ? c.getDireccion() : "—" %></td>
                    <td>
                        <div class="acciones-fila">
                            <a href="clientes.jsp?editar=<%= c.getIdCliente() %>" class="btn-editar" title="Editar">
                                <i class="fa-solid fa-pen"></i>
                            </a>
                            <form action="ClienteServlet" method="post" onsubmit="return confirm('¿Eliminar a <%= c.getNombreCompleto() %>?');" style="display:inline;">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="idCliente" value="<%= c.getIdCliente() %>">
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
