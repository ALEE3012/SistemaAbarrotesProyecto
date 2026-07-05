<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="Modelo.VentaDAO"%>
<%@page import="Modelo.InventarioDAO"%>
<%
    List<Object[]> ventasRecientes = new ArrayList<>();
    List<Object[]> productosMasVendidos = new ArrayList<>();
    List<Object[]> inventarioBajo = new ArrayList<>();
    String[] etiquetasDias = new String[30];
    double[] totalesDias = new double[30];
    String errorCarga = null;

    try{
        VentaDAO ventaDAO = new VentaDAO();
        InventarioDAO inventarioDAO = new InventarioDAO();

        ventasRecientes = ventaDAO.ventasRecientes(15);
        productosMasVendidos = ventaDAO.productosMasVendidos(8);

        for(Object[] f : inventarioDAO.listarConProducto()){
            if(!"OK".equals((String) f[6])) inventarioBajo.add(f);
        }

        Map<String, Double> mapaTotales = new LinkedHashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -29);
        java.text.SimpleDateFormat clave = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.SimpleDateFormat etiqueta = new java.text.SimpleDateFormat("dd/MM");
        for(int i = 0; i < 30; i++){
            mapaTotales.put(clave.format(cal.getTime()), 0.0);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        for(Object[] fila : ventaDAO.totalesUltimosDias(30)){
            java.sql.Date dia = (java.sql.Date) fila[0];
            String k = clave.format(dia);
            if(mapaTotales.containsKey(k)) mapaTotales.put(k, (Double) fila[1]);
        }
        int i = 0;
        for(Map.Entry<String, Double> entrada : mapaTotales.entrySet()){
            java.util.Date d = clave.parse(entrada.getKey());
            etiquetasDias[i] = etiqueta.format(d);
            totalesDias[i] = entrada.getValue();
            i++;
        }

    } catch(Exception e){
        e.printStackTrace();
        errorCarga = "No se pudieron cargar los reportes: " + e.getMessage();
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Reportes | La Casa del Gato</title>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/Estilo.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<div class="contenedor">

    <a href="principal.jsp" class="btn-regresar"><i class="fa-solid fa-arrow-left"></i> Regresar al Dashboard</a>

    <div class="cabecera">
        <div class="titulo">
            <div class="icono"><i class="fa-solid fa-file-invoice-dollar"></i></div>
            <div>
                <h1>Reportes</h1>
                <p>Ventas de los últimos 30 días</p>
            </div>
        </div>
    </div>

    <% if(errorCarga != null){ %><div class="mensaje-error"><%= errorCarga %></div><% } %>

    <div class="panel">
        <h2><i class="fa-solid fa-chart-line"></i> Ingresos por día</h2>
        <div class="sub">Últimos 30 días</div>
        <canvas id="graficoVentas" height="10" width="30"></canvas>
    </div>

    <div class="grid-2">

        <div class="panel">
            <h2><i class="fa-solid fa-star"></i> Productos más vendidos</h2>
            <div class="tabla-wrap">
            <table>
                <thead><tr><th>Producto</th><th>Unidades</th><th>Total generado</th></tr></thead>
                <tbody>
                    <% if(productosMasVendidos.isEmpty()){ %>
                    <tr class="vacio"><td colspan="3">Aún no hay ventas registradas</td></tr>
                    <% } else { for(Object[] p : productosMasVendidos){ %>
                    <tr>
                        <td><%= p[0] %></td>
                        <td><%= p[1] %> uds</td>
                        <td>S/ <%= String.format("%.2f", (Double) p[2]) %></td>
                    </tr>
                    <% } } %>
                </tbody>
            </table>
            </div>
        </div>

        <div class="panel">
            <h2><i class="fa-solid fa-triangle-exclamation"></i> Inventario con alerta</h2>
            <div class="tabla-wrap">
            <table>
                <thead><tr><th>Producto</th><th>Stock</th><th>Estado</th></tr></thead>
                <tbody>
                    <% if(inventarioBajo.isEmpty()){ %>
                    <tr class="vacio"><td colspan="3">Todo el inventario está en niveles normales</td></tr>
                    <% } else { for(Object[] f : inventarioBajo){
                        String estado = (String) f[6];
                        String claseBadge = "AGOTADO".equals(estado) ? "agotado" : "bajo";
                        String textoBadge = "AGOTADO".equals(estado) ? "Agotado" : "Stock bajo";
                    %>
                    <tr>
                        <td><%= f[2] %></td>
                        <td><%= f[3] %> uds</td>
                        <td><span class="badge <%= claseBadge %>"><%= textoBadge %></span></td>
                    </tr>
                    <% } } %>
                </tbody>
            </table>
            </div>
        </div>

    </div>

    <div class="panel">
        <h2><i class="fa-solid fa-receipt"></i> Últimas ventas</h2>
        <div class="tabla-wrap">
        <table>
            <thead><tr><th>#</th><th>Cliente</th><th>Fecha</th><th>Total</th><th>Estado</th></tr></thead>
            <tbody>
                <% if(ventasRecientes.isEmpty()){ %>
                <tr class="vacio"><td colspan="5">Aún no hay ventas registradas</td></tr>
                <% } else {
                    java.text.SimpleDateFormat fmtFecha = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                    for(Object[] v : ventasRecientes){
                %>
                <tr>
                    <td>#<%= v[0] %></td>
                    <td><%= v[1] %></td>
                    <td><%= fmtFecha.format((java.sql.Timestamp) v[2]) %></td>
                    <td>S/ <%= String.format("%.2f", (Double) v[3]) %></td>
                    <td><span class="badge ok"><%= v[4] %></span></td>
                </tr>
                <% } } %>
            </tbody>
        </table>
        </div>
    </div>

</div>
<div class="contenedor-grafico">
    <canvas id="graficoVentas"></canvas>
</div>
<script>
const etiquetas = [<% for(int i=0;i<30;i++){ %>"<%= etiquetasDias[i] %>"<%= i<29 ? "," : "" %><% } %>];
const totales   = [<% for(int i=0;i<30;i++){ %><%= totalesDias[i] %><%= i<29 ? "," : "" %><% } %>];


const canvas = document.getElementById("graficoVentas");

if (canvas) {
    const ctx = canvas.getContext("2d");

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: etiquetas,
            datasets: [{
                label: 'Ingresos',
                data: totales,
                borderColor: '#D4AF37',
                backgroundColor: 'rgba(212,175,55,0.2)',
                fill: true,
                tension: 0.35
            }]
        },

    });
}
</script>

</body>
</html>
