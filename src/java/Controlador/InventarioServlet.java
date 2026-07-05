package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import Modelo.InventarioDAO;
import Modelo.AuditoriaDAO;
import jakarta.servlet.http.*;

@WebServlet("/InventarioServlet")
public class InventarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        InventarioDAO dao = new InventarioDAO();
        AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
        Object idUsuarioObj = request.getSession().getAttribute("idUsuario");

        try {
            if("ajustar".equals(accion)){
                int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                String tipo = request.getParameter("tipo"); // "entrada" o "salida"

                int delta = "salida".equals(tipo) ? -cantidad : cantidad;
                dao.ajustarStock(idProducto, delta);

                if(idUsuarioObj != null){
                    auditoriaDAO.registrar("Inventario", "ACTUALIZAR", idProducto,
                        ("salida".equals(tipo) ? "Salida" : "Entrada") + " de " + cantidad + " unidades",
                        (Integer) idUsuarioObj);
                }

            } else if("minimo".equals(accion)){
                int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                int nuevoMinimo = Integer.parseInt(request.getParameter("stockMinimo"));
                dao.actualizarStockMinimo(idProducto, nuevoMinimo);

                if(idUsuarioObj != null){
                    auditoriaDAO.registrar("Inventario", "ACTUALIZAR", idProducto,
                        "Cambio de stock mínimo a " + nuevoMinimo, (Integer) idUsuarioObj);
                }
            }

            response.sendRedirect("inventario.jsp");

        } catch(SQLException | NumberFormatException e){
            e.printStackTrace();
            request.getSession().setAttribute("mensajeError",
                "No se pudo actualizar el inventario: " + e.getMessage());
            response.sendRedirect("inventario.jsp");
        }
    }
}