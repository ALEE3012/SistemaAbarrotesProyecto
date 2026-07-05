<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.SQLException"%>
<%@page import="Modelo.VentaDAO"%>
<%@page import="Modelo.ProductoDAO"%>
<%@page import="Modelo.InventarioDAO"%>
<%
    double ventasHoy = 0;
    int pedidosMes = 0;
    int productosActivos = 0;
    int alertasStockBajo = 0;

    List<Object[]> ventasRecientes = new ArrayList<>();      // {idVenta, cliente, fecha, total, estado}
    List<Object[]> productosMasVendidos = new ArrayList<>(); // {nombre, unidades, total}
    List<Object[]> alertasInventario = new ArrayList<>();    // {idInventario, idProducto, nombre, stockActual, stockMinimo, fecha, estado}

    String[] etiquetasDias = new String[7];
    double[] totalesDias = new double[7];

    String errorCarga = null;

    try{
        VentaDAO ventaDAO = new VentaDAO();
        ProductoDAO productoDAO = new ProductoDAO();
        InventarioDAO inventarioDAO = new InventarioDAO();

        ventasHoy = ventaDAO.totalVentasDelDia();
        pedidosMes = ventaDAO.pedidosDelMes();
        productosActivos = productoDAO.listarConDetalle().size();

        List<Object[]> inventarioCompleto = inventarioDAO.listarConProducto();
        for(Object[] fila : inventarioCompleto){
            String estado = (String) fila[6];
            if(!"OK".equals(estado)){
                alertasStockBajo++;
                if(alertasInventario.size() < 6) alertasInventario.add(fila);
            }
        }

        ventasRecientes = ventaDAO.ventasRecientes(6);
        productosMasVendidos = ventaDAO.productosMasVendidos(5);

        // ---- ventas de los últimos 7 días (rellena con 0 los días sin ventas) ----
        Map<String, Double> mapaTotales = new LinkedHashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -6);

        java.text.SimpleDateFormat clave = new java.text.SimpleDateFormat("yyyy-MM-dd");
        for(int i = 0; i < 7; i++){
            mapaTotales.put(clave.format(cal.getTime()), 0.0);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        for(Object[] fila : ventaDAO.totalesUltimosDias(7)){
            java.sql.Date dia = (java.sql.Date) fila[0];
            String k = clave.format(dia);
            if(mapaTotales.containsKey(k)) mapaTotales.put(k, (Double) fila[1]);
        }

        String[] nombresDias = {"Dom","Lun","Mar","Mié","Jué","Vie","Sáb"};
        int i = 0;
        for(Map.Entry<String, Double> entrada : mapaTotales.entrySet()){
            java.util.Date d = clave.parse(entrada.getKey());
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            etiquetasDias[i] = nombresDias[c.get(Calendar.DAY_OF_WEEK) - 1];
            totalesDias[i] = entrada.getValue();
            i++;
        }

    } catch(Exception e){
        e.printStackTrace();
        errorCarga = "No se pudieron cargar los datos del dashboard: " + e.getMessage();
    }

    String ventaRegistrada = request.getParameter("ventaRegistrada");
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard | La Casa del Gato</title>

<link rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">

<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@500;600;700&family=Segoe+UI:wght@400;500&display=swap" rel="stylesheet">

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>

:root{
    --oro:#D4AF37; --oro-suave:#f0c84b; --fondo:#0b0b0d; --panel:#141416;
    --panel-borde:rgba(212,175,55,.18); --texto:#f2f0ea; --texto-tenue:#9a978f;
    --exito:#3fae67; --alerta:#c0392b; --advertencia:#d99a1f;
}

*{ margin:0; padding:0; box-sizing:border-box; font-family:'Segoe UI',sans-serif; }
body{ min-height:100vh; background:var(--fondo); color:var(--texto); display:flex; }
h1,h2,h3,.marca{ font-family:'Poppins',sans-serif; }

.sidebar{
    width:260px; min-height:100vh; background:#0a0a0b;
    border-right:1px solid var(--panel-borde); display:flex; flex-direction:column;
    position:fixed; top:0; left:0; z-index:20; transition:.3s;
}
.marca{ display:flex; align-items:center; gap:12px; padding:26px 22px; border-bottom:1px solid var(--panel-borde); }
.marca img{ width:44px; height:44px; border-radius:50%; border:2px solid var(--oro); object-fit:cover; }
.marca .txt{ line-height:1.15; }
.marca .txt span{ display:block; font-size:10.5px; letter-spacing:1.5px; color:var(--texto-tenue); font-family:'Segoe UI',sans-serif; }
.marca .txt strong{ color:var(--oro); font-size:16px; }

.nav{ padding:18px 12px; flex:1; }
.nav .grupo-titulo{ font-size:11px; letter-spacing:1.5px; color:var(--texto-tenue); padding:14px 12px 8px; }
.nav a{
    display:flex; align-items:center; gap:12px; padding:11px 14px; margin-bottom:4px;
    border-radius:10px; color:#cfcdc6; text-decoration:none; font-size:14.5px; transition:.2s;
}
.nav a i{ width:18px; text-align:center; color:var(--oro); opacity:.85; }
.nav a:hover{ background:rgba(212,175,55,.08); color:#fff; }
.nav a.activo{
    background:linear-gradient(90deg,rgba(212,175,55,.18),rgba(212,175,55,.02));
    color:#fff; box-shadow:inset 3px 0 0 var(--oro);
}

.sidebar-pie{ padding:16px 20px 22px; border-top:1px solid var(--panel-borde); display:flex; align-items:center; gap:10px; }
.sidebar-pie img{ width:36px; height:36px; border-radius:50%; border:2px solid var(--oro); object-fit:cover; }
.sidebar-pie .u span{ display:block; font-size:11px; color:var(--texto-tenue); }
.sidebar-pie a{ margin-left:auto; color:var(--texto-tenue); font-size:15px; }

.contenido{ margin-left:260px; flex:1; min-height:100vh; display:flex; flex-direction:column; }

.topbar{
    height:74px; display:flex; align-items:center; justify-content:space-between; padding:0 30px;
    border-bottom:1px solid var(--panel-borde); position:sticky; top:0;
    background:rgba(11,11,13,.85); backdrop-filter:blur(10px); z-index:15;
}
.topbar h1{ font-size:19px; font-weight:600; }
.topbar .fecha{ font-size:12.5px; color:var(--texto-tenue); font-weight:400; }

.btn-nuevo{
    background:var(--oro); color:#0b0b0d; border:none; padding:10px 18px; border-radius:10px;
    font-weight:600; font-size:13.5px; cursor:pointer; display:flex; align-items:center; gap:8px;
    transition:.2s; text-decoration:none;
}
.btn-nuevo:hover{ background:var(--oro-suave); transform:translateY(-1px); box-shadow:0 6px 16px rgba(212,175,55,.25); }

.zona{ padding:28px 30px 40px; }

.mensaje-exito{
    background:rgba(63,174,103,.14); border:1px solid rgba(63,174,103,.35);
    color:var(--exito); padding:12px 16px; border-radius:10px; font-size:13px; margin-bottom:20px;
}
.mensaje-error{
    background:rgba(192,57,43,.14); border:1px solid rgba(192,57,43,.35);
    color:#e2685c; padding:12px 16px; border-radius:10px; font-size:13px; margin-bottom:20px;
}

.kpis{ display:grid; grid-template-columns:repeat(4,1fr); gap:20px; margin-bottom:26px; }
.kpi{ background:var(--panel); border:1px solid var(--panel-borde); border-radius:16px; padding:20px 22px; position:relative; overflow:hidden; }
.kpi::before{ content:""; position:absolute; top:0; right:0; width:90px; height:90px; background:radial-gradient(circle,rgba(212,175,55,.14),transparent 70%); }
.kpi .icono{ width:42px; height:42px; border-radius:11px; display:flex; align-items:center; justify-content:center; font-size:17px; background:rgba(212,175,55,.12); color:var(--oro); }
.kpi.alerta .icono{ background:rgba(192,57,43,.14); color:#e2685c; }
.kpi h3{ font-size:26px; margin-top:16px; font-weight:700; }
.kpi p{ font-size:12.5px; color:var(--texto-tenue); margin-top:4px; }

.grid-principal{ display:grid; grid-template-columns:1.6fr 1fr; gap:20px; margin-bottom:20px; }
.panel{ background:var(--panel); border:1px solid var(--panel-borde); border-radius:16px; padding:22px; }
.panel-cabecera{ display:flex; justify-content:space-between; align-items:center; margin-bottom:18px; }
.panel-cabecera h2{ font-size:15.5px; font-weight:600; }
.panel-cabecera .sub{ font-size:12px; color:var(--texto-tenue); margin-top:2px; }

.producto-fila{ display:flex; align-items:center; gap:12px; padding:11px 0; border-bottom:1px solid rgba(255,255,255,.05); }
.producto-fila:last-child{ border-bottom:none; }
.producto-icono{
    width:38px; height:38px; border-radius:10px; background:rgba(212,175,55,.1); color:var(--oro);
    display:flex; align-items:center; justify-content:center; font-size:15px; flex-shrink:0;
}
.producto-info{ flex:1; }
.producto-info h4{ font-size:13.5px; font-weight:500; }
.producto-cifra{ text-align:right; font-size:13px; font-weight:600; white-space:nowrap; }
.producto-cifra span{ display:block; font-size:11px; color:var(--texto-tenue); font-weight:400; }

.tabla-wrap{ overflow-x:auto; }
table{ width:100%; border-collapse:collapse; }
thead th{ text-align:left; font-size:11.5px; letter-spacing:.6px; color:var(--texto-tenue); padding:0 14px 12px; font-weight:600; border-bottom:1px solid var(--panel-borde); }
tbody td{ padding:13px 14px; font-size:13.5px; border-bottom:1px solid rgba(255,255,255,.05); }
tbody tr:last-child td{ border-bottom:none; }
tbody tr:hover{ background:rgba(212,175,55,.04); }
.vacio td{ text-align:center; color:var(--texto-tenue); padding:24px; font-size:13px; }

.cliente{ display:flex; align-items:center; gap:10px; }
.avatar-letra{
    width:32px; height:32px; border-radius:50%; background:rgba(212,175,55,.14); color:var(--oro);
    display:flex; align-items:center; justify-content:center; font-size:12.5px; font-weight:700;
}

.badge{ font-size:11.5px; font-weight:600; padding:5px 11px; border-radius:20px; white-space:nowrap; }
.badge.completado{ background:rgba(63,174,103,.14); color:var(--exito); }
.badge.pendiente{ background:rgba(217,154,31,.14); color:var(--advertencia); }
.badge.bajo{ background:rgba(217,154,31,.14); color:var(--advertencia); }
.badge.agotado{ background:rgba(192,57,43,.28); color:#ff9188; }

.dos-columnas{ display:grid; grid-template-columns:1.4fr 1fr; gap:20px; }
.ver-todo{ font-size:12.5px; color:var(--oro); text-decoration:none; font-weight:500; }

@media(max-width:1180px){
    .grid-principal, .dos-columnas{ grid-template-columns:1fr; }
    .kpis{ grid-template-columns:repeat(2,1fr); }
}
@media(max-width:860px){
    .sidebar{ left:-260px; }
    .sidebar.abierto{ left:0; }
    .contenido{ margin-left:0; }
    .kpis{ grid-template-columns:1fr; }
}
.btn-menu{ display:none; color:var(--texto); font-size:19px; background:none; border:none; cursor:pointer; }
@media(max-width:860px){ .btn-menu{ display:block; } }

</style>
</head>
<body>

<aside class="sidebar" id="sidebar">

    <div class="marca">
        <img src="Img/logo.png.jpeg" alt="La Casa del Gato">
        <div class="txt">
            <strong>La Casa del Gato</strong>
            <span>GESTIÓN DE VENTAS</span>
        </div>
    </div>

    <nav class="nav">
        <div class="grupo-titulo">PRINCIPAL</div>
        <a href="principal.jsp" class="activo"><i class="fa-solid fa-chart-pie"></i> Dashboard</a>

        <div class="grupo-titulo">OPERACIÓN</div>
        <a href="nueva_venta.jsp"><i class="fa-solid fa-cash-register"></i> Ventas</a>
        <a href="inventario.jsp"><i class="fa-solid fa-boxes-stacked"></i> Inventario</a>
        <a href="proveedores.jsp"><i class="fa-solid fa-truck-fast"></i> Proveedores</a>
        <a href="productos.jsp"><i class="fa-solid fa-basket-shopping"></i> Productos</a>
        <a href="clientes.jsp"><i class="fa-solid fa-users"></i> Clientes</a>

        <div class="grupo-titulo">ANÁLISIS</div>
        <a href="reportes.jsp"><i class="fa-solid fa-file-invoice-dollar"></i> Reportes</a>
        <a href="auditoria.jsp"><i class="fa-solid fa-clipboard-list"></i> Auditoría</a>
    </nav>

    <div class="sidebar-pie">
        <img src="Img/logo.png.jpeg" alt="Usuario">
        <div class="u"><strong>Admin</strong><span>Administrador</span></div>
        <a href="${pageContext.request.contextPath}/LogoutServlet">
    <i class="fa-solid fa-right-from-bracket"></i>
    Cerrar Sesión
</a>
    </div>

</aside>

<div class="contenido">

    <div class="topbar">
        <div style="display:flex;align-items:center;gap:16px;">
            <button class="btn-menu" onclick="document.getElementById('sidebar').classList.toggle('abierto')">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div>
                <h1>Panel General</h1>
                <div class="fecha" id="fechaHoy"></div>
            </div>
        </div>
        <a href="nueva_venta.jsp" class="btn-nuevo"><i class="fa-solid fa-plus"></i> Nueva venta</a>
    </div>

    <div class="zona">

        <% if("1".equals(ventaRegistrada)){ %>
        <div class="mensaje-exito"><i class="fa-solid fa-circle-check"></i> Venta registrada correctamente.</div>
        <% } %>

        <% if(errorCarga != null){ %>
        <div class="mensaje-error"><%= errorCarga %></div>
        <% } %>

        <div class="kpis">

            <div class="kpi">
                <div class="icono"><i class="fa-solid fa-sack-dollar"></i></div>
                <h3>S/ <%= String.format("%.2f", ventasHoy) %></h3>
                <p>Ventas de hoy</p>
            </div>

            <div class="kpi">
                <div class="icono"><i class="fa-solid fa-receipt"></i></div>
                <h3><%= pedidosMes %></h3>
                <p>Pedidos del mes</p>
            </div>

            <div class="kpi">
                <div class="icono"><i class="fa-solid fa-box"></i></div>
                <h3><%= productosActivos %></h3>
                <p>Productos activos</p>
            </div>

            <div class="kpi <%= alertasStockBajo > 0 ? "alerta" : "" %>">
                <div class="icono"><i class="fa-solid fa-triangle-exclamation"></i></div>
                <h3><%= alertasStockBajo %></h3>
                <p>Productos con stock bajo</p>
            </div>

        </div>

        <div class="grid-principal">

            <div class="panel">
                <div class="panel-cabecera">
                    <div>
                        <h2>Ventas e ingresos</h2>
                        <div class="sub">Últimos 7 días</div>
                    </div>
                </div>
                <div class="grafico-dashboard">
    <canvas id="graficoVentas"></canvas>
</div>
            </div>

            <div class="panel">
                <div class="panel-cabecera">
                    <div>
                        <h2>Productos más vendidos</h2>
                        <div class="sub">Histórico</div>
                    </div>
                </div>

                <% if(productosMasVendidos.isEmpty()){ %>
                    <p style="color:var(--texto-tenue);font-size:13px;">Todavía no hay ventas registradas.</p>
                <% } else {
                    for(Object[] p : productosMasVendidos){ %>
                <div class="producto-fila">
                    <div class="producto-icono"><i class="fa-solid fa-basket-shopping"></i></div>
                    <div class="producto-info"><h4><%= p[0] %></h4></div>
                    <div class="producto-cifra">S/ <%= String.format("%.2f", (Double) p[2]) %><span><%= p[1] %> uds</span></div>
                </div>
                <% } } %>
            </div>

        </div>

        <div class="dos-columnas">

            <div class="panel">
                <div class="panel-cabecera">
                    <div>
                        <h2>Ventas recientes</h2>
                        <div class="sub">Últimas transacciones registradas</div>
                    </div>
                    <a href="reportes.jsp" class="ver-todo">Ver todo</a>
                </div>

                <div class="tabla-wrap">
                <table>
                    <thead>
                        <tr><th>Cliente</th><th>Fecha</th><th>Total</th><th>Estado</th></tr>
                    </thead>
                    <tbody>
                        <% if(ventasRecientes.isEmpty()){ %>
                        <tr class="vacio"><td colspan="4">Aún no hay ventas registradas</td></tr>
                        <% } else {
                            java.text.SimpleDateFormat fmtFecha = new java.text.SimpleDateFormat("dd MMM, HH:mm", new Locale("es","ES"));
                            for(Object[] v : ventasRecientes){
                                String cliente = (String) v[1];
                                String iniciales = cliente.substring(0,1).toUpperCase();
                        %>
                        <tr>
                            <td><div class="cliente"><div class="avatar-letra"><%= iniciales %></div><%= cliente %></div></td>
                            <td><%= fmtFecha.format((java.sql.Timestamp) v[2]) %></td>
                            <td>S/ <%= String.format("%.2f", (Double) v[3]) %></td>
                            <td><span class="badge completado"><%= v[4] %></span></td>
                        </tr>
                        <% } } %>
                    </tbody>
                </table>
                </div>
            </div>

            <div class="panel">
                <div class="panel-cabecera">
                    <div>
                        <h2>Alertas de inventario</h2>
                        <div class="sub">Productos que requieren reposición</div>
                    </div>
                    <a href="inventario.jsp" class="ver-todo">Ver todo</a>
                </div>

                <div class="tabla-wrap">
                <table>
                    <thead>
                        <tr><th>Producto</th><th>Stock</th><th>Estado</th></tr>
                    </thead>
                    <tbody>
                        <% if(alertasInventario.isEmpty()){ %>
                        <tr class="vacio"><td colspan="3">Todo el inventario está en niveles normales</td></tr>
                        <% } else {
                            for(Object[] fila : alertasInventario){
                                String estado = (String) fila[6];
                                String claseBadge = "AGOTADO".equals(estado) ? "agotado" : "bajo";
                                String textoBadge = "AGOTADO".equals(estado) ? "Agotado" : "Stock bajo";
                        %>
                        <tr>
                            <td><%= fila[2] %></td>
                            <td><%= fila[3] %> uds</td>
                            <td><span class="badge <%= claseBadge %>"><%= textoBadge %></span></td>
                        </tr>
                        <% } } %>
                    </tbody>
                </table>
                </div>
            </div>

        </div>

    </div>

</div>

<script>

document.getElementById('fechaHoy').textContent = new Date().toLocaleDateString('es-PE',{
    weekday:'long', year:'numeric', month:'long', day:'numeric'
});

const etiquetas = [<% for(int i=0;i<7;i++){ %>"<%= etiquetasDias[i] %>"<%= i<6 ? "," : "" %><% } %>];
const totales = [<% for(int i=0;i<7;i++){ %><%= totalesDias[i] %><%= i<6 ? "," : "" %><% } %>];

const ctx = document.getElementById("graficoVentas");

new Chart(ctx,{
    type:"line",
    data:{
        labels:etiquetas,
        datasets:[{
            data:totales,
            borderColor:"#D4AF37",
            backgroundColor:"rgba(212,175,55,.20)",
            fill:true,
            tension:.35,
            borderWidth:3,
            pointRadius:4,
            pointHoverRadius:6
        }]
    },
    options:{
        responsive:true,
        maintainAspectRatio:false,
        plugins:{
            legend:{
                display:false
            }
        },
        interaction:{
            intersect:false,
            mode:"index"
        },
        scales:{
            x:{
                grid:{
                    display:false
                },
                ticks:{
                    color:"#9a978f"
                }
            },
            y:{
                beginAtZero:true,
                ticks:{
                    color:"#9a978f",
                    callback:(v)=>"S/ "+v
                }
            }
        }
    }
});

</script>

</body>
</html>
