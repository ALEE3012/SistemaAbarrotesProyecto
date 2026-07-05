<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.AuditoriaDAO"%>
<%
    List<Object[]> registros = null;
    String errorCarga = null;

    try{
        registros = new AuditoriaDAO().listarConUsuario(100);
    } catch(Exception e){
        e.printStackTrace();
        errorCarga = "No se pudo cargar la bitácora de auditoría: " + e.getMessage();
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Auditoría | La Casa del Gato</title>

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
            <div class="icono"><i class="fa-solid fa-clipboard-list"></i></div>
            <div>
                <h1>Bitácora de auditoría</h1>
                <p>Últimas <%= registros != null ? registros.size() : 0 %> acciones registradas en el sistema</p>
            </div>
        </div>
    </div>

    <% if(errorCarga != null){ %><div class="mensaje-error"><%= errorCarga %></div><% } %>

    <div class="panel">
        <h2><i class="fa-solid fa-list"></i> Registro de operaciones</h2>
        <div class="sub">Quién hizo qué acción, sobre qué tabla y cuándo</div>

        <div class="tabla-wrap">
        <table>
            <thead>
                <tr><th>Fecha y hora</th><th>Usuario</th><th>Tabla</th><th>Acción</th><th>Detalle</th></tr>
            </thead>
            <tbody>
                <% if(registros == null || registros.isEmpty()){ %>
                <tr class="vacio"><td colspan="5">Aún no hay registros de auditoría</td></tr>
                <% } else {
                    java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    for(Object[] r : registros){
                        String accion = (String) r[2];
                        String claseBadge = "INSERTAR".equals(accion) ? "ok" : ("ELIMINAR".equals(accion) ? "agotado" : "bajo");
                %>
                <tr>
                    <td><%= fmt.format((java.sql.Timestamp) r[5]) %></td>
                    <td><%= r[6] %></td>
                    <td><%= r[1] %></td>
                    <td><span class="badge <%= claseBadge %>"><%= accion %></span></td>
                    <td><%= r[4] %></td>
                </tr>
                <% } } %>
            </tbody>
        </table>
        </div>
    </div>

</div>

</body>
</html>
