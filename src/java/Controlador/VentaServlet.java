package Controlador;

import Modelo.Cliente;
import Modelo.ClienteDAO;
import Modelo.DetalleVenta;
import Modelo.DetalleVentaDAO;
import Modelo.InventarioDAO;
import Modelo.ProductoDAO;
import Modelo.Venta;
import Modelo.VentaDAO;
import Modelo.Pago;
import Modelo.PagoDAO;
import Modelo.AuditoriaDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/VentaServlet")
public class VentaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // ===========================
            // Usuario
            // ===========================

            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

            // ===========================
            // Cliente
            // ===========================

            int idCliente = Integer.parseInt(request.getParameter("idCliente"));

            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if(cliente == null){
                request.getSession().setAttribute("mensajeError", "Cliente no encontrado.");
                response.sendRedirect("nueva_venta.jsp");
                return;
            }

            // ===========================
            // Producto
            // ===========================

            int idProducto = Integer.parseInt(request.getParameter("idProducto"));

            ProductoDAO productoDAO = new ProductoDAO();

            Object[] producto = productoDAO.buscarPorIdParaVenta(idProducto);

            if(producto == null){

                request.getSession().setAttribute(
                        "mensajeError",
                        "Producto no encontrado.");

                response.sendRedirect("nueva_venta.jsp");
                return;
            }

            // ===========================
            // Cantidad
            // ===========================

            int cantidad = Integer.parseInt(request.getParameter("cantidad"));

            if(cantidad <= 0){

                request.getSession().setAttribute(
                        "mensajeError",
                        "La cantidad debe ser mayor a cero.");

                response.sendRedirect("nueva_venta.jsp");
                return;

            }

            // ===========================
            // Datos producto
            // ===========================

            double precioVenta = (Double) producto[2];

            int stockActual = (Integer) producto[3];

            if(cantidad > stockActual){

                request.getSession().setAttribute(
                        "mensajeError",
                        "Stock insuficiente. Disponible: "
                                + stockActual);

                response.sendRedirect("nueva_venta.jsp");
                return;

            }

            double subtotal = precioVenta * cantidad;

            // Obtener el tipo de pago
            String tipoPago = request.getParameter("tipoPago");

            // Definir el estado de la venta
            String estadoVenta;

            if(tipoPago.equals("FIADO")){
                estadoVenta = "FIADO";
            }else{
                estadoVenta = "COMPLETADO";
            }

            // ===========================
            // Registrar Venta
            // ===========================

            Venta venta = new Venta(
                    0,
                    null,
                    subtotal,
                    estadoVenta,
                    idCliente,
                    idUsuario);

            VentaDAO ventaDAO = new VentaDAO();

            int idVenta = ventaDAO.insertarYObtenerId(venta);

            // ===========================
            // Detalle
            // ===========================

            DetalleVenta detalle = new DetalleVenta(
                    0,
                    cantidad,
                    precioVenta,
                    subtotal,
                    idVenta,
                    idProducto);

            DetalleVentaDAO detalleDAO = new DetalleVentaDAO();

            detalleDAO.insertar(detalle);

            Pago pago = new Pago(
                    0,
                    tipoPago,
                    subtotal,
                    new Timestamp(System.currentTimeMillis()),
                    idVenta
            );

            PagoDAO pagoDAO = new PagoDAO();

            pagoDAO.insertar(pago);

            // ===========================
            // Actualizar Stock
            // ===========================

            InventarioDAO inventarioDAO = new InventarioDAO();

            inventarioDAO.ajustarStock(idProducto, -cantidad);

            // ===========================
            // Auditoría
            // ===========================

            new AuditoriaDAO().registrar(
                    "Venta",
                    "INSERTAR",
                    idVenta,
                    "Venta registrada a " + cliente.getNombreCompleto() +
                    " por S/ " + String.format("%.2f", subtotal) +
                    " (" + tipoPago + ")",
                    idUsuario);

            response.sendRedirect("principal.jsp?ventaRegistrada=1");

        } catch (SQLException | NumberFormatException e) {

            e.printStackTrace();

            request.getSession().setAttribute(
                    "mensajeError",
                    "Ocurrió un error: " + e.getMessage());

            response.sendRedirect("nueva_venta.jsp");

        }

    }

}