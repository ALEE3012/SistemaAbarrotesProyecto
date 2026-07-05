package Controlador;

import Modelo.Producto;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import Modelo.ProductoDAO;
import Modelo.InventarioDAO;
import Modelo.CategoriaDAO;
import Modelo.AuditoriaDAO;
import jakarta.servlet.http.*;

@WebServlet("/ProductoServlet")
public class ProductoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        ProductoDAO productoDAO = new ProductoDAO();
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
        Object idUsuarioObj = request.getSession().getAttribute("idUsuario");

        try {
            if("agregar".equals(accion) || "editar".equals(accion)){

                String nombreProducto = request.getParameter("nombreProducto");
                String descripcion = request.getParameter("descripcion");
                double precioCompra = Double.parseDouble(request.getParameter("precioCompra"));
                double precioVenta = Double.parseDouble(request.getParameter("precioVenta"));

                String fechaVenceStr = request.getParameter("fechaVencimiento");
                Date fechaVencimiento = (fechaVenceStr != null && !fechaVenceStr.isEmpty())
                        ? Date.valueOf(fechaVenceStr) : null;

                // categoría: si escribieron una nueva, se crea; si no, se usa la seleccionada
                String categoriaNueva = request.getParameter("categoriaNueva");
                int idCategoria;
                if(categoriaNueva != null && !categoriaNueva.trim().isEmpty()){
                    idCategoria = categoriaDAO.buscarOCrear(categoriaNueva.trim());
                } else {
                    idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                }

                // proveedor es opcional
                String idProveedorStr = request.getParameter("idProveedor");
                Integer idProveedor = (idProveedorStr != null && !idProveedorStr.isEmpty())
                        ? Integer.parseInt(idProveedorStr) : null;

                if("agregar".equals(accion)){
                    Producto p = new Producto(0, nombreProducto, descripcion, precioCompra,
                            precioVenta, fechaVencimiento, idCategoria, idProveedor);
                    int idProducto = productoDAO.insertarYObtenerId(p);

                    int stockInicial = Integer.parseInt(request.getParameter("stockInicial"));
                    int stockMinimo = Integer.parseInt(request.getParameter("stockMinimo"));
                    new InventarioDAO().crearInventarioInicial(idProducto, stockInicial, stockMinimo);

                    if(idUsuarioObj != null){
                        auditoriaDAO.registrar("Producto", "INSERTAR", idProducto,
                            "Registro del producto " + nombreProducto + " (stock inicial: " + stockInicial + ")",
                            (Integer) idUsuarioObj);
                    }

                } else {
                    int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                    Producto p = new Producto(idProducto, nombreProducto, descripcion, precioCompra,
                            precioVenta, fechaVencimiento, idCategoria, idProveedor);
                    productoDAO.actualizar(p);

                    if(idUsuarioObj != null){
                        auditoriaDAO.registrar("Producto", "ACTUALIZAR", idProducto,
                            "Edición del producto " + nombreProducto, (Integer) idUsuarioObj);
                    }
                }

            } else if("eliminar".equals(accion)){
                int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                productoDAO.eliminar(idProducto);

                if(idUsuarioObj != null){
                    auditoriaDAO.registrar("Producto", "ELIMINAR", idProducto,
                        "Eliminación del producto #" + idProducto, (Integer) idUsuarioObj);
                }
            }

            response.sendRedirect("productos.jsp");

        } catch(SQLException | IllegalArgumentException e){
            e.printStackTrace();
            request.getSession().setAttribute("mensajeError",
                "No se pudo completar la operación sobre el producto: " + e.getMessage());
            response.sendRedirect("productos.jsp");
        }
    }
}