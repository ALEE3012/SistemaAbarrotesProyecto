package Controlador;

import Modelo.Cliente;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import Modelo.ClienteDAO;
import Modelo.AuditoriaDAO;
import jakarta.servlet.http.*;

@WebServlet("/ClienteServlet")
public class ClientesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        ClienteDAO dao = new ClienteDAO();
        AuditoriaDAO auditoriaDAO = new AuditoriaDAO();

        Object idUsuarioObj = request.getSession().getAttribute("idUsuario");

        try {
            if("agregar".equals(accion)){
                Cliente c = new Cliente(
                    0,
                    request.getParameter("nombres"),
                    request.getParameter("apellidos"),
                    request.getParameter("telefono"),
                    request.getParameter("direccion")
                );
                int idGenerado = dao.insertarYObtenerId(c);

                if(idUsuarioObj != null){
                    auditoriaDAO.registrar("Cliente", "INSERTAR", idGenerado,
                        "Registro del cliente " + c.getNombreCompleto(), (Integer) idUsuarioObj);
                }

            } else if("editar".equals(accion)){
                int idCliente = Integer.parseInt(request.getParameter("idCliente"));
                Cliente c = new Cliente(
                    idCliente,
                    request.getParameter("nombres"),
                    request.getParameter("apellidos"),
                    request.getParameter("telefono"),
                    request.getParameter("direccion")
                );
                dao.actualizar(c);

                if(idUsuarioObj != null){
                    auditoriaDAO.registrar("Cliente", "ACTUALIZAR", idCliente,
                        "Edición de los datos de " + c.getNombreCompleto(), (Integer) idUsuarioObj);
                }

            } else if("eliminar".equals(accion)){
                int idCliente = Integer.parseInt(request.getParameter("idCliente"));
                dao.eliminar(idCliente);

                if(idUsuarioObj != null){
                    auditoriaDAO.registrar("Cliente", "ELIMINAR", idCliente,
                        "Eliminación del cliente #" + idCliente, (Integer) idUsuarioObj);
                }
            }

            response.sendRedirect("clientes.jsp");

        } catch(SQLException e){
            e.printStackTrace();
            request.getSession().setAttribute("mensajeError", "No se pudo completar la operación sobre el cliente.");
            response.sendRedirect("clientes.jsp");
        }
    }
}