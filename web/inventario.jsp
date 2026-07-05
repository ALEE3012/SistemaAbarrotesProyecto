<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.InventarioDAO"%>
<%
    InventarioDAO inventarioDAO = new InventarioDAO();
    List<Object[]> inventario = null; // {idInventario, idProducto, nombreProducto, stockActual, stockMinimo, fecha, estado}
    String errorCarga = null;

    try{
        inventario = inventarioDAO.listarConProducto();
    } catch(Exception e){
        e.printStackTrace();
        errorCarga = "No se pudo cargar el inventario: " + e.getMessage();
    }

    String msjError = (String) session.getAttribute("mensajeError");
    if(msjError != null) session.removeAttribute("mensajeError");

    int totalBajo = 0, totalAgotado = 0;
    if(inventario != null){
        for(Object[] f : inventario){
            String est = (String) f[6];
            if("BAJO".equals(est)) totalBajo++;
            if("AGOTADO".equals(est)) totalAgotado++;
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inventario | La Casa del Gato</title>

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
            <div class="icono"><i class="fa-solid fa-boxes-stacked"></i></div>
            <div>
                <h1>Inventario</h1>
                <p><%= inventario != null ? inventario.size() : 0 %> productos con inventario · <%= totalBajo %> con stock bajo · <%= totalAgotado %> agotados</p>
            </div>
        </div>
        <a href="productos.jsp" class="btn-nuevo" style="text-decoration:none;"><i class="fa-solid fa-plus"></i> Nuevo producto</a>
    </div>

    <% if(errorCarga != null){ %><div class="mensaje-error"><%= errorCarga %></div><% } %>
    <% if(msjError != null){ %><div class="mensaje-error"><%= msjError %></div><% } %>

    <div class="mensaje-error" style="background:rgba(212,175,55,.1);border-color:var(--panel-borde);color:var(--texto-tenue);">
        <i class="fa-solid fa-circle-info"></i> El inventario se crea automáticamente al registrar un producto nuevo. Aquí solo se ajusta el stock (entradas/salidas) y el stock mínimo.
    </div>

    <div class="panel">
        <h2><i class="fa-solid fa-list"></i> Stock por producto</h2>

        <div class="tabla-wrap">
        <table>
            <thead>
                <tr><th>Producto</th><th>Stock actual</th><th>Stock mínimo</th><th>Actualizado</th><th>Estado</th><th>Ajustar</th></tr>
            </thead>
            <tbody>
                <% if(inventario == null || inventario.isEmpty()){ %>
                <tr class="vacio"><td colspan="6">No hay productos con inventario todavía</td></tr>
                <% } else {
                    for(Object[] f : inventario){
                        int idProducto = (Integer) f[1];
                        String nombre = (String) f[2];
                        int stockActual = (Integer) f[3];
                        int stockMinimo = (Integer) f[4];
                        String estado = (String) f[6];
                        String claseBadge = "AGOTADO".equals(estado) ? "agotado" : ("BAJO".equals(estado) ? "bajo" : "ok");
                        String textoBadge = "AGOTADO".equals(estado) ? "Agotado" : ("BAJO".equals(estado) ? "Stock bajo" : "Disponible");
                %>
                <tr>
                    <td><%= nombre %></td>
                    <td><%= stockActual %> uds</td>
                    <td>
                        <form action="/InventarioServlet" method="post" style="display:flex;gap:6px;align-items:center;">
                            <input type="hidden" name="accion" value="minimo">
                            <input type="hidden" name="idProducto" value="<%= idProducto %>">
                            <input type="number" name="stockMinimo" min="0" value="<%= stockMinimo %>"
                                style="width:70px;padding:6px 8px;border-radius:7px;border:1px solid var(--panel-borde);background:rgba(255,255,255,.05);color:var(--texto);">
                            <button type="submit" class="btn-editar" title="Guardar mínimo"><i class="fa-solid fa-check"></i></button>
                        </form>
                    </td>
                    <td><%= f[5] %></td>
                    <td><span class="badge <%= claseBadge %>"><%= textoBadge %></span></td>
                    <td>
                        <form action="InventarioServlet" method="post" style="display:flex;gap:6px;align-items:center;">
                            <input type="hidden" name="accion" value="ajustar">
                            <input type="hidden" name="idProducto" value="<%= idProducto %>">
                            <select name="tipo" style="padding:8px;border-radius:7px;border:1px solid var(--panel-borde);background:rgba(255,255,255,.05);color:var(--texto);font-size:12.5px;">
                                <option value="entrada">+ Entrada</option>
                                <option value="salida">− Salida</option>
                            </select>
                            <input type="number" name="cantidad" min="1" value="1" required
                                style="width:64px;padding:6px 8px;border-radius:7px;border:1px solid var(--panel-borde);background:rgba(255,255,255,.05);color:var(--texto);">
                            <button type="submit" class="btn-agregar" style="padding:8px 12px;"><i class="fa-solid fa-rotate"></i></button>
                        </form>
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
