<%@page import="java.util.List"%>
<%@page import="Modelo.Cliente"%>
<%@page import="Modelo.ClienteDAO"%>
<%@page import="Modelo.ProductoDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    ClienteDAO clienteDAO = new ClienteDAO();
    ProductoDAO productoDAO = new ProductoDAO();

    List<Cliente> clientes = clienteDAO.listar();
    List<Object[]> productos = productoDAO.listarDisponiblesParaVenta();

    Object idUsuarioSesion = session.getAttribute("idUsuario");

    String msjErrorVenta = (String) session.getAttribute("mensajeError");
    if(msjErrorVenta != null){
        session.removeAttribute("mensajeError");
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Nueva Venta</title>

<link rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">

<link rel="stylesheet" href="css/Estilo.css">

</head>

<body>

<div class="contenedor angosto">

<a href="principal.jsp" class="btn-regresar">
<i class="fa-solid fa-arrow-left"></i> Regresar
</a>

<div class="cabecera">
<div class="titulo">
<div class="icono">
<i class="fa-solid fa-cart-plus"></i>
</div>

<div>
<h1>Nueva Venta</h1>
<p>Seleccione el cliente y el producto.</p>
</div>

</div>
</div>

<% if(msjErrorVenta!=null){ %>

<div class="mensaje-error">
<%=msjErrorVenta%>
</div>

<% } %>

<form action="VentaServlet" method="post">

<input
type="hidden"
name="idUsuario"
value="<%=idUsuarioSesion%>">

<!-- CLIENTE -->

<div class="panel">

<h2>
<i class="fa-solid fa-user"></i>
Cliente
</h2>

<div class="campo">

<label>Cliente</label>

<select
name="idCliente"
id="idCliente"
required>

<option value="">
Seleccione...
</option>

<%

for(Cliente c:clientes){

%>

<option value="<%=c.getIdCliente()%>">

<%=c.getNombres()%>
<%=c.getApellidos()%>

</option>

<%

}

%>

</select>

</div>

</div>

<!-- PRODUCTO -->

<div class="panel">

<h2>

<i class="fa-solid fa-box"></i>

Producto

</h2>

<div class="campo">

<label>Producto</label>

<select
name="idProducto"
id="idProducto"
required>

<option value="">
Seleccione...
</option>

<%

for(Object[] p:productos){

%>

<option
value="<%=p[0]%>"
data-precio="<%=p[2]%>"
data-stock="<%=p[3]%>">

<%=p[1]%>

</option>

<%

}

%>

</select>

</div>

<div class="grid-2" style="margin-top:15px;">

<div class="campo">

<label>Cantidad</label>

<input
type="number"
name="cantidad"
id="cantidad"
value="1"
min="1"
required>

</div>

<div class="campo">

<label>Precio</label>

<input
type="hidden"
name="precioVenta"
id="precioVenta">

<input
type="text"
id="precioMostrar"
readonly>
</div>

</div>

<div style="margin-top:15px">

<b>Total :</b>

<h2 id="total">
S/. 0.00
</h2>

<div class="campo" style="margin-top:20px;">
    <label>Forma de pago</label>

    <select name="tipoPago" required>
        <option value="">Seleccione...</option>
        <option value="EFECTIVO">Efectivo</option>
        <option value="YAPE">Yape</option>
        <option value="FIADO">Fiado</option>
    </select>
</div>

</div>

</div>

<div class="botones-finales">

<button
type="submit"
class="btn-guardar">

<i class="fa-solid fa-floppy-disk"></i>

Guardar Venta

</button>

</div>

</form>

</div>

<script>

const producto=document.getElementById("idProducto");
const cantidad=document.getElementById("cantidad");
const precio=document.getElementById("precioVenta");
const total=document.getElementById("total");

function calcular(){

let opcion=producto.options[producto.selectedIndex];

if(opcion.value==""){

precio.value="";
total.innerHTML="S/. 0.00";
return;

}

let p=parseFloat(opcion.dataset.precio);

document.getElementById("precioVenta").value = p.toFixed(2);
document.getElementById("precioMostrar").value = "S/. " + p.toFixed(2);

let c=parseInt(cantidad.value)||0;

total.innerHTML="S/. "+(p*c).toFixed(2);

}

producto.addEventListener("change",calcular);

cantidad.addEventListener("input",calcular);

</script>

</body>
</html>