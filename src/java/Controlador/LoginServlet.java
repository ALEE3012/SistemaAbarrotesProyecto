package Controlador;
import Modelo.UsuarioDAO;
import Modelo.UsuarioDAO;
import Modelo.Usuario;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String usuario =
                request.getParameter("usuario");
        String password =
                request.getParameter("password");
        UsuarioDAO dao = new UsuarioDAO();
        Usuario u =
                dao.validar(usuario, password);
        if (u != null) {
            HttpSession sesion =
                    request.getSession();
            sesion.setAttribute("idUsuario", u.getIdUsuario());
            response.sendRedirect("principal.jsp");
        } else {
            response.sendRedirect(
                    "login.jsp?error=1");
        }
    }
}