package Controlador;

import Modelo.Proveedor;
import Modelo.ProveedorDAO;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ProveedorServlet")
public class ProveedorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        ProveedorDAO dao = new ProveedorDAO();

        try {
            if("agregar".equals(accion)){
                Proveedor p = new Proveedor(
                    0,
                    request.getParameter("razonSocial"),
                    request.getParameter("ruc"),
                    request.getParameter("telefono"),
                    request.getParameter("direccion"),
                    request.getParameter("correo")
                );
                dao.insertar(p);

            } else if("editar".equals(accion)){
                Proveedor p = new Proveedor(
                    Integer.parseInt(request.getParameter("idProveedor")),
                    request.getParameter("razonSocial"),
                    request.getParameter("ruc"),
                    request.getParameter("telefono"),
                    request.getParameter("direccion"),
                    request.getParameter("correo")
                );
                dao.actualizar(p);

            } else if("eliminar".equals(accion)){
                dao.eliminar(Integer.parseInt(request.getParameter("idProveedor")));
            }

            response.sendRedirect("proveedores.jsp");

        } catch(SQLException e){
            e.printStackTrace();
            request.getSession().setAttribute("mensajeError", "No se pudo completar la operación sobre el proveedor.");
            response.sendRedirect("proveedores.jsp");
        }
    }
}