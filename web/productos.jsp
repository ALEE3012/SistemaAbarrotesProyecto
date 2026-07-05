<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.ProductoDAO"%>
<%@page import="Modelo.CategoriaDAO"%>
<%@page import="Modelo.ProveedorDAO"%>
<%@page import="Modelo.Producto"%>
<%@page import="Modelo.Categoria"%>
<%@page import="Modelo.Proveedor"%>
<%
    ProductoDAO productoDAO = new ProductoDAO();
    CategoriaDAO categoriaDAO = new CategoriaDAO();
    ProveedorDAO proveedorDAO = new ProveedorDAO();

    List<Object[]> productos = null;   // listado con detalle
    List<Categoria> categorias = null;
    List<Proveedor> proveedores = null;
    Producto productoEditar = null;
    String errorCarga = null;

    try{
        productos   = productoDAO.listarConDetalle();
        categorias  = categoriaDAO.listar();
        proveedores = proveedorDAO.listar();

        String idEditar = request.getParameter("editar");
        if(idEditar != null){
            productoEditar = productoDAO.buscarPorId(Integer.parseInt(idEditar));
        }
    } catch(Exception e){
        e.printStackTrace();
        errorCarga = "No se pudo cargar productos: " + e.getMessage();
    }

    String msjError = (String) session.getAttribute("mensajeError");
    if(msjError != null) session.removeAttribute("mensajeError");

    boolean modoEdicion = (productoEditar != null);
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Productos | La Casa del Gato</title>

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
            <div class="icono"><i class="fa-solid fa-basket-shopping"></i></div>
            <div>
                <h1>Productos</h1>
                <p><%= productos != null ? productos.size() : 0 %> productos registrados</p>
            </div>
        </div>
    </div>

    <% if(errorCarga != null){ %><div class="mensaje-error"><%= errorCarga %></div><% } %>
    <% if(msjError != null){ %><div class="mensaje-error"><%= msjError %></div><% } %>

    <!-- ============ FORMULARIO ============ -->
    <div class="panel">
        <h2><i class="fa-solid fa-<%= modoEdicion ? "pen" : "circle-plus" %>"></i> <%= modoEdicion ? "Editar producto" : "Nuevo producto" %></h2>
        <% if(!modoEdicion){ %>
        <div class="sub">Si escribes una categoría que no existe, se crea automáticamente. El stock inicial genera el registro de inventario.</div>
        <% } %>

        <form action="/ProductoServlet" method="post">
            <input type="hidden" name="accion" value="<%= modoEdicion ? "editar" : "agregar" %>">
            <% if(modoEdicion){ %>
            <input type="hidden" name="idProducto" value="<%= productoEditar.getIdProducto() %>">
            <% } %>

            <div class="grid-2">
                <div class="campo">
                    <label>Nombre del producto</label>
                    <input type="text" name="nombreProducto" required
                        value="<%= modoEdicion ? productoEditar.getNombreProducto() : "" %>">
                </div>
                <div class="campo">
                    <label>Descripción</label>
                    <input type="text" name="descripcion"
                        value="<%= modoEdicion && productoEditar.getDescripcion() != null ? productoEditar.getDescripcion() : "" %>">
                </div>

                <div class="campo">
                    <label>Precio de compra (S/)</label>
                    <input type="number" step="0.01" min="0" name="precioCompra" required
                        value="<%= modoEdicion ? productoEditar.getPrecioCompra() : "" %>">
                </div>
                <div class="campo">
                    <label>Precio de venta (S/)</label>
                    <input type="number" step="0.01" min="0" name="precioVenta" required
                        value="<%= modoEdicion ? productoEditar.getPrecioVenta() : "" %>">
                </div>

                <div class="campo">
                    <label>Categoría existente</label>
                    <select name="idCategoria">
                        <option value="">— Elegir de la lista —</option>
                        <% if(categorias != null) for(Categoria c : categorias){ %>
                        <option value="<%= c.getIdCategoria() %>"
                            <%= (modoEdicion && productoEditar.getIdCategoria() == c.getIdCategoria()) ? "selected" : "" %>>
                            <%= c.getNombreCategoria() %>
                        </option>
                        <% } %>
                    </select>
                </div>
                <div class="campo">
                    <label>… o categoría nueva</label>
                    <input type="text" name="categoriaNueva" placeholder="Déjalo vacío si eliges de la lista">
                </div>

                <div class="campo">
                    <label>Proveedor (opcional)</label>
                    <select name="idProveedor">
                        <option value="">— Sin proveedor —</option>
                        <% if(proveedores != null) for(Proveedor p : proveedores){ %>
                        <option value="<%= p.getIdProveedor() %>"
                            <%= (modoEdicion && productoEditar.getIdProveedor() != null && productoEditar.getIdProveedor() == p.getIdProveedor()) ? "selected" : "" %>>
                            <%= p.getRazonSocial() %>
                        </option>
                        <% } %>
                    </select>
                </div>
                <div class="campo">
                    <label>Fecha de vencimiento (opcional)</label>
                    <input type="date" name="fechaVencimiento"
                        value="<%= (modoEdicion && productoEditar.getFechaVencimiento() != null) ? productoEditar.getFechaVencimiento().toString() : "" %>">
                </div>

                <% if(!modoEdicion){ %>
                <div class="campo">
                    <label>Stock inicial</label>
                    <input type="number" min="0" name="stockInicial" value="0" required>
                </div>
                <div class="campo">
                    <label>Stock mínimo</label>
                    <input type="number" min="0" name="stockMinimo" value="5" required>
                </div>
                <% } %>
            </div>

            <div class="botones-finales">
                <button type="submit" class="btn-guardar">
                    <i class="fa-solid fa-floppy-disk"></i> <%= modoEdicion ? "Actualizar producto" : "Guardar producto" %>
                </button>
                <% if(modoEdicion){ %>
                <a href="productos.jsp" class="btn-limpiar">Cancelar</a>
                <% } %>
            </div>
            <% if(modoEdicion){ %>
            <small style="display:block;margin-top:10px;">El stock se administra desde la página de <a href="inventario.jsp" style="color:var(--oro);">Inventario</a>.</small>
            <% } %>
        </form>
    </div>

    <!-- ============ LISTA ============ -->
    <div class="panel">
        <h2><i class="fa-solid fa-list"></i> Lista de productos</h2>

        <div class="tabla-wrap">
        <table>
            <thead>
                <tr>
                    <th>Producto</th><th>Categoría</th><th>P. Compra</th><th>P. Venta</th>
                    <th>Proveedor</th><th>Stock</th><th></th>
                </tr>
            </thead>
            <tbody>
                <% if(productos == null || productos.isEmpty()){ %>
                <tr class="vacio"><td colspan="7">No hay productos registrados todavía</td></tr>
                <% } else {
                    for(Object[] p : productos){
                        Integer stock = (Integer) p[6];
                        String estado = (String) p[8];
                        String claseBadge = estado == null ? "" : ("AGOTADO".equals(estado) ? "agotado" : ("BAJO".equals(estado) ? "bajo" : "ok"));
                %>
                <tr>
                    <td><%= p[1] %></td>
                    <td><%= p[2] %></td>
                    <td>S/ <%= String.format("%.2f", (Double) p[3]) %></td>
                    <td>S/ <%= String.format("%.2f", (Double) p[4]) %></td>
                    <td><%= p[5] != null ? p[5] : "—" %></td>
                    <td>
                        <% if(stock != null){ %>
                        <span class="badge <%= claseBadge %>"><%= stock %> uds</span>
                        <% } else { %>
                        <span class="badge bajo">Sin inventario</span>
                        <% } %>
                    </td>
                    <td>
                        <div class="acciones-fila">
                            <a href="productos.jsp?editar=<%= p[0] %>" class="btn-editar" title="Editar"><i class="fa-solid fa-pen"></i></a>
                            <form action="/ProductoServlet" method="post" onsubmit="return confirm('¿Eliminar este producto?');" style="display:inline;">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="idProducto" value="<%= p[0] %>">
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
