package Controlador;

import Modelo.VentasDAO;
import Modelo.VentasDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/DashboardVentas")
public class DashboardVentaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        VentasDAO dao = new VentasDAO();

        request.setAttribute("ventasHoy", dao.totalVentasHoy());
        request.setAttribute("transacciones", dao.totalTransacciones());

        request.getRequestDispatcher("dashboardVentas.jsp").forward(request, response);
    }
}