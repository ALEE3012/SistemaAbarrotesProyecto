
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>La Casa del Gato</title>

<link rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">

<style>

*{
    margin:0;
    padding:0;
    box-sizing:border-box;
    font-family:'Segoe UI',sans-serif;
}

body{
    min-height:100vh;
    display:flex;
    justify-content:center;
    align-items:center;
    background:url('Img/fondo.jpg') center center no-repeat;
    background-size:cover;
}

.overlay{
    position:fixed;
    top:0;
    left:0;
    width:100%;
    height:100%;
    background:rgba(0,0,0,.35);
}

.login-box{
    position:relative;
    width:450px;
    padding:40px;
    background:rgba(0,0,0,.45);
    backdrop-filter:blur(8px);
    border:2px solid #D4AF37;
    border-radius:20px;
    box-shadow:0 0 25px rgba(212,175,55,.35);
    z-index:10;
    animation:entrada 1s ease;
}

@keyframes entrada{
    from{
        opacity:0;
        transform:translateY(-30px);
    }
    to{
        opacity:1;
        transform:translateY(0);
    }
}

.logo-container{
    text-align:center;
    margin-bottom:15px;
}

.logo{
    width:120px;
    height:120px;
    border-radius:50%;
    border:4px solid #D4AF37;
    object-fit:cover;
    box-shadow:0 0 20px rgba(212,175,55,.5);
}

.nombre-tienda{
    color:#D4AF37;
    font-size:28px;
    font-weight:bold;
    text-align:center;
    margin-top:12px;
}

.subtitulo{
    color:#ffffff;
    text-align:center;
    margin-top:8px;
    margin-bottom:25px;
    font-size:14px;
}

.input-group{
    position:relative;
    margin-bottom:20px;
}

.input-group i{
    position:absolute;
    left:15px;
    top:16px;
    color:#D4AF37;
}

.input-group input{
    width:100%;
    padding:14px 14px 14px 45px;
    border:none;
    border-radius:10px;
    background:rgba(255,255,255,.08);
    color:white;
    font-size:15px;
}

.input-group input::placeholder{
    color:#d9d9d9;
}

.input-group input:focus{
    outline:none;
    border:1px solid #D4AF37;
}

.show-password{
    position:absolute;
    right:15px;
    top:16px;
    cursor:pointer;
    color:#D4AF37;
}

.btn-login{
    width:100%;
    padding:14px;
    border:none;
    border-radius:10px;
    background:#D4AF37;
    color:black;
    font-size:16px;
    font-weight:bold;
    cursor:pointer;
    transition:.3s;
}

.btn-login:hover{
    background:#f0c84b;
    transform:translateY(-2px);
    box-shadow:0 0 20px rgba(212,175,55,.6);
}

.footer{
    margin-top:25px;
    text-align:center;
    color:#cfcfcf;
    font-size:13px;
}

@media(max-width:500px){

    .login-box{
        width:90%;
        padding:30px;
    }

    .logo{
        width:100px;
        height:100px;
    }

    .nombre-tienda{
        font-size:24px;
    }
}

</style>
</head>
<body>

<div class="overlay"></div>

<div class="login-box">

    <div class="logo-container">
        <img src="Img/logo.png.jpeg" class="logo" alt="La Casa del Gato">
    </div>

    <div class="nombre-tienda">
        La Casa del Gato
    </div>

    <div class="subtitulo">
        Sistema de Gestión de Ventas e Inventario
    </div>

    <%
        String error = request.getParameter("error");

        if(error != null){
    %>

    <div style="
        background:#c0392b;
        color:white;
        padding:12px;
        border-radius:10px;
        margin-bottom:20px;
        text-align:center;
        font-weight:bold;">
        Usuario o contraseña incorrectos
    </div>

    <%
        }
    %>

    <form action="LoginServlet" method="post">

        <div class="input-group">
            <i class="fa-solid fa-user"></i>
            <input
                type="text"
                name="usuario"
                placeholder="Usuario"
                required>
        </div>

        <div class="input-group">

            <i class="fa-solid fa-lock"></i>

            <input
                type="password"
                id="password"
                name="password"
                placeholder="Contraseña"
                required>

            <span
                class="show-password"
                onclick="mostrarPassword()">
                <i class="fa-solid fa-eye"></i>
            </span>

        </div>

        <button type="submit" class="btn-login">
            INGRESAR
        </button>

    </form>

    <div class="footer">
        © 2026 La Casa del Gato
    </div>

</div>

<script>

function mostrarPassword(){

    let pass = document.getElementById("password");

    if(pass.type === "password"){
        pass.type = "text";
    }else{
        pass.type = "password";
    }
}

</script>

</body>
</html>
